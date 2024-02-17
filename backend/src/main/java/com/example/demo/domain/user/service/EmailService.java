package com.example.demo.domain.user.service;

import com.example.demo.domain.user.exception.EmailAlreadyExistsException;
import com.example.demo.domain.user.exception.EmailVerificationCodeNotFoundException;
import com.example.demo.domain.user.exception.MaxVerificationAttemptsExceededException;
import com.example.demo.domain.user.exception.PendingEmailNotFoundException;
import com.example.demo.domain.user.mapper.EmailMapper;
import com.example.demo.domain.user.model.EmailVerification;
import com.example.demo.domain.user.model.PendingEmail;
import com.example.demo.domain.user.model.User;
import com.example.demo.domain.user.repository.PendingEmailsRepository;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.redis.EmailRedisRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.security.SecureRandom;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class EmailService {
    private static final int MAX_VERIFICATION_ATTEMPTS = 5;
    private static final int RESET_PASSWORD_LENGTH = 10;

    @Value("${spring.mail.username}")
    private String FROM_EMAIL_ADDRESS;

    private final UserRepository userRepository;
    private final PendingEmailsRepository pendingEmailsRepository;
    private final EmailMapper emailMapper;
    private final EmailRedisRepository emailRedisRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    public Boolean registerPendingEmail(PendingEmail pendingEmail) {

        if (userRepository.existsByEmail(pendingEmail.getEmail())) {
            throw new EmailAlreadyExistsException("이미 사용중인 이메일입니다.");
        }

        return true;
    }

    public EmailVerification sendEmailVerification(EmailVerification domain) throws MessagingException {
        // 이메일 인증 코드 생성
        String email = domain.getEmail();
        String verificationCode = emailRedisRepository.setVerification(email);

        // 이미 사용중인 이메일인지 확인
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        // 보류 이메일이 있는지 확인
        PendingEmail pendingEmail = pendingEmailsRepository.findByEmail(email)
                .map(emailMapper::entityToDomain)
                .orElseGet(() -> { // 보류 이메일 없으면 생성
                    PendingEmail newPendingEmail = PendingEmail.builder()
                            .email(email)
                            .authCode(verificationCode)
                            .authCount(0)
                            .build();

                    return Optional.of(newPendingEmail)
                            .map(emailMapper::domainToEntity)
                            .map(pendingEmailsRepository::save)
                            .map(emailMapper::entityToDomain)
                            .orElseThrow();
                });

        // 인증 횟수 5번 초과 확인
        if (pendingEmail.getAuthCount() >= MAX_VERIFICATION_ATTEMPTS) {
            throw new MaxVerificationAttemptsExceededException("인증 횟수 5번을 초과하였습니다.");
        }

        // 이메일 전송
        MimeMessage message = mailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("이메일 인증코드");
        message.setText("인증코드: " + verificationCode);
        message.setFrom(FROM_EMAIL_ADDRESS);

        mailSender.send(message);

        // 인증 횟수 증가
        pendingEmail.setAuthCount(pendingEmail.getAuthCount() + 1);
        pendingEmail.setAuthCode(verificationCode);
        pendingEmailsRepository.save(emailMapper.domainToEntity(pendingEmail));

        return domain;
    }

    public boolean checkEmailVerification(EmailVerification domain) throws RuntimeException {

        // 이메일 인증 코드 확인 3분안에 진행 해야함
        if(!emailRedisRepository.hasVerification(domain.getEmail())) {
            throw new EmailVerificationCodeNotFoundException("인증코드가 존재하지 않습니다. 이메일 인증을 다시 진행해주세요.");
        }

        // 보류 이메일에 있는지 확인
        PendingEmail pendingEmail = pendingEmailsRepository.findByEmail(domain.getEmail())
                .map(emailMapper::entityToDomain)
                .orElseThrow(() -> new PendingEmailNotFoundException("보류 이메일에 존재하지 않습니다."));

        // 인증코드 일치 확인
        if (!pendingEmail.getAuthCode().equals(domain.getCode())) {
            throw new EmailVerificationCodeNotFoundException("인증코드가 일치하지 않습니다.");
        }

        // 인증 코드 저장
        pendingEmail.setAuthCode(domain.getCode());
        pendingEmailsRepository.save(emailMapper.domainToEntity(pendingEmail));

        // 캐시 메모리 인증 코드 삭제
        emailRedisRepository.deleteVerification(domain.getEmail());

        return true;
    }

    // 임시 비밀번호 전송
    public boolean sendResetPassword(User user) throws MessagingException {

        // 임시 비밀번호 생성
        String resetPassword = generateResetPassword();

        // 회원 비밀번호 변경
        userRepository.findByEmailAndUserName(user.getEmail(), user.getUserName())
                .map(u -> {
                    u.setPassword(passwordEncoder.encode(resetPassword));
                    return userRepository.save(u);
                })
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        // 이메일 전송
        MimeMessage message = mailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, user.getEmail());
        message.setSubject("Down 임시 비밀번호 발송");
        message.setText("임시 비밀번호 발송: " + resetPassword);
        message.setFrom(FROM_EMAIL_ADDRESS);

        mailSender.send(message);

        return true;
    }

    private String generateResetPassword() {

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";

        for (int i = 0; i < RESET_PASSWORD_LENGTH; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

}