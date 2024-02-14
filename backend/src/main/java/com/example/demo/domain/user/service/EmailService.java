package com.example.demo.domain.user.service;

import com.example.demo.domain.user.exception.EmailAlreadyExistsException;
import com.example.demo.domain.user.exception.EmailVerificationCodeNotFoundException;
import com.example.demo.domain.user.exception.MaxVerificationAttemptsExceededException;
import com.example.demo.domain.user.exception.PendingEmailNotFoundException;
import com.example.demo.domain.user.mapper.EmailMapper;
import com.example.demo.domain.user.model.EmailVerification;
import com.example.demo.domain.user.model.PendingEmail;
import com.example.demo.domain.user.repository.PendingEmailsRepository;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.redis.EmailRedisRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class EmailService {
    private static final int MAX_VERIFICATION_ATTEMPTS = 5;

    @Value("${spring.mail.username}")
    private String FROM_EMAIL_ADDRESS;

    private final UserRepository userRepository;
    private final PendingEmailsRepository pendingEmailsRepository;
    private final EmailMapper emailMapper;
    private final EmailRedisRepository emailRedisRepository;
    private final JavaMailSender mailSender;

    public PendingEmail registerPendingEmail(PendingEmail pendingEmail) {

        if (userRepository.existsByEmail(pendingEmail.getEmail())) {
            throw new EmailAlreadyExistsException("이미 사용중인 이메일입니다.");
        }

        if (pendingEmailsRepository.existsByEmail(pendingEmail.getEmail())) {
            throw new EmailAlreadyExistsException("이미 사용중인 이메일입니다.");
        }

        pendingEmail.setAuthCount(0);

        return Optional.of(pendingEmail)
                .map(emailMapper::domainToEntity)
                .map(pendingEmailsRepository::save)
                .map(emailMapper::entityToDomain)
                .orElseThrow();
    }

    public EmailVerification sendEmailVerification(EmailVerification domain) throws MessagingException {
        String email = domain.getEmail();

        // 이미 사용중인 이메일인지 확인
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("이미 사용중인 이메일입니다.");
        }

        // 보류 이메일에 있는지 확인
        PendingEmail pendingEmail = pendingEmailsRepository.findByEmail(email)
                .map(emailMapper::entityToDomain)
                .orElseThrow(() -> new PendingEmailNotFoundException("보류 이메일에 존재하지 않습니다."));

        // 인증 횟수 5번 초과 확인
        if (pendingEmail.getAuthCount() >= MAX_VERIFICATION_ATTEMPTS) {
            throw new MaxVerificationAttemptsExceededException("인증 횟수 5번을 초과하였습니다.");
        }

        // 이메일 인증 코드 생성 및 저장
        String verificationCode = emailRedisRepository.setVerification(email);

        // 이메일 전송
        MimeMessage message = mailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("이메일 인증코드");
        message.setText("인증코드: " + verificationCode);
        message.setFrom(FROM_EMAIL_ADDRESS);

        mailSender.send(message);

        // 인증 횟수 증가
        pendingEmail.setAuthCount(pendingEmail.getAuthCount() + 1);
        pendingEmailsRepository.save(emailMapper.domainToEntity(pendingEmail));

        return domain;
    }

    public boolean checkEmailVerification(EmailVerification domain) {

        // 이메일 인증 코드 확인
        if(!emailRedisRepository.hasVerification(domain.getEmail())) {
            throw new EmailVerificationCodeNotFoundException("인증코드가 존재하지 않습니다. 이메일 인증을 다시 진행해주세요.");
        }

        // 보류 이메일에 있는지 확인
        PendingEmail pendingEmail = pendingEmailsRepository.findByEmail(domain.getEmail())
                .map(emailMapper::entityToDomain)
                .orElseThrow(() -> new PendingEmailNotFoundException("보류 이메일에 존재하지 않습니다."));

        // 인증 코드 저장
        pendingEmail.setAuthCode(domain.getCode());
        pendingEmailsRepository.save(emailMapper.domainToEntity(pendingEmail));

        // 캐시 메모리 인증 코드 삭제
        emailRedisRepository.deleteVerification(domain.getEmail());

        return true;
    }
}