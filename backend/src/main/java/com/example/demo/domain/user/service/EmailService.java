package com.example.demo.domain.user.service;

import com.example.demo.domain.user.dto.command.CheckEmailVerificationCommand;
import com.example.demo.domain.user.dto.command.SendEmailVerificationCommand;
import com.example.demo.domain.user.dto.command.UserJoinCheckCommand;
import com.example.demo.domain.user.entity.PendingEmail;
import com.example.demo.domain.user.model.EmailVerification;
import com.example.demo.domain.user.repository.PendingEmailsRepository;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ExceptionCode;
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
    private final EmailRedisRepository emailRedisRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    public Boolean registerPendingEmail(String email) {

        if (userRepository.existsByEmail(email)) {
            throw CustomException.of(ExceptionCode.ALREADY_EXIST_USER_EMAIL);
        }

        return true;
    }

    public EmailVerification sendEmailVerification(SendEmailVerificationCommand cmd) throws MessagingException {
        // 이메일 인증 코드 생성
        String email = cmd.getEmail();
        String verificationCode = emailRedisRepository.setVerification(email);

        // 이미 사용중인 이메일인지 확인
        if (userRepository.existsByEmail(email)) {
            throw CustomException.of(ExceptionCode.ALREADY_EXIST_USER_EMAIL);
        }

        // 보류 이메일이 있는지 확인
        PendingEmail pendingEmail = pendingEmailsRepository.findByEmail(email)
                .orElseGet(() -> { // 보류 이메일 없으면 생성
                    PendingEmail newPendingEmail = PendingEmail.builder()
                            .email(email)
                            .authCode(verificationCode)
                            .authCount(0)
                            .build();
                    return pendingEmailsRepository.save(newPendingEmail);
                });

        // 인증 횟수 5번 초과 확인
        if (pendingEmail.getAuthCount() >= MAX_VERIFICATION_ATTEMPTS) {
            throw CustomException.of(ExceptionCode.EXCEED_EMAIL_VERIFICATION_ATTEMPTS);
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
        pendingEmailsRepository.save(pendingEmail);

        return EmailVerification.builder()
                .email(email)
                .code(verificationCode)
                .build();
    }

    public boolean checkEmailVerification(CheckEmailVerificationCommand cmd) throws RuntimeException {

        // 이메일 인증 코드 확인 3분안에 진행 해야함
        if(!emailRedisRepository.hasVerification(cmd.getEmail())) {
            throw CustomException.of(ExceptionCode.NOT_EXIST_EMAIL_VERIFICATION_CODE);
        }

        // 보류 이메일에 있는지 확인
        PendingEmail pendingEmail = pendingEmailsRepository.findByEmail(cmd.getEmail())
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_EXIST_PENDING_EMAIL));

        // 인증코드 일치 확인
        if (!pendingEmail.getAuthCode().equals(cmd.getCode())) {
            throw CustomException.of(ExceptionCode.NOT_MATCH_EMAIL_VERIFICATION_CODE);
        }

        // 인증 코드 저장
        pendingEmail.setAuthCode(cmd.getCode());
        pendingEmailsRepository.save(pendingEmail);

        // 캐시 메모리 인증 코드 삭제
        emailRedisRepository.deleteVerification(cmd.getEmail());

        return true;
    }

    // 임시 비밀번호 전송
    public boolean sendResetPassword(UserJoinCheckCommand cmd) throws MessagingException {

        // 임시 비밀번호 생성
        String resetPassword = generateResetPassword();

        // 회원 비밀번호 변경
        userRepository.findByEmailAndUserName(cmd.getEmail(), cmd.getUserName())
                .map(u -> {
                    u.setPassword(passwordEncoder.encode(resetPassword));
                    return userRepository.save(u);
                })
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_EXIST_USER));

        // 이메일 전송
        MimeMessage message = mailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, cmd.getEmail());
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