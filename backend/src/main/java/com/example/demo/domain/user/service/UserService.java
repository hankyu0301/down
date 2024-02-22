package com.example.demo.domain.user.service;

import com.example.demo.domain.user.entity.UserRoleEnumType;
import com.example.demo.domain.user.mapper.EmailMapper;
import com.example.demo.domain.user.model.PendingEmail;
import com.example.demo.domain.user.model.User;
import com.example.demo.domain.user.model.User.UserId;
import com.example.demo.domain.user.mapper.UserMapper;
import com.example.demo.domain.user.repository.PendingEmailsRepository;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.auth.LoginEnumType;
import com.example.demo.global.auth.jwt.JwtTokenProvider;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    @Value("${spring.mail.username}")
    private String FROM_EMAIL;

    private final UserRepository userRepository;
    private final PendingEmailsRepository pendingEmailsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailMapper emailMapper;
    private final UserMapper userMapper;

    public UserId join(User user, String code) {
        
        // 이메일 확인 코드 보류 이메일에 있는지 확인
        PendingEmail pendingEmail = pendingEmailsRepository.findByEmail(user.getEmail())
                .map(emailMapper::entityToDomain)
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_VERIFIED_EMAIL));

        // 이메일 확인 코드가 일치하는지 확인
        if (!pendingEmail.getAuthCode().equals(code)) {
            throw CustomException.of(ExceptionCode.NOT_MATCH_EMAIL_AUTH_CODE);
        }

        // 회원 가입 처리
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setProvider(LoginEnumType.SERVICE);
        user.setRole(UserRoleEnumType.ROLE_USER);
        user.setProviderId(0L);

        UserId userId = Optional.of(user)
                .map(userMapper::domainToEntity)
                .map(userRepository::save)
                .map(userMapper::entityToDomain)
                .map(User::getId)
                .orElseThrow();

        // 회원 가입 완료 후 팬딩 이메일 삭제
        pendingEmailsRepository.deleteByEmail(user.getEmail());

        return userId;
    }

    public boolean checkEmail(User user) {
        if (!userRepository.existsByEmailAndUserName(user.getEmail(), user.getUserName())) {
            throw CustomException.of(ExceptionCode.NOT_EXIST_USER);
        }
        return true;
    }

    public String login(User cmd) {

        // 이메일 존재 확인
        User user = userRepository.findByEmail(cmd.getEmail())
                .map(userMapper::entityToDomain)
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_EXIST_USER));

        // 비밀번호 확인
        if (!passwordEncoder.matches(cmd.getPassword(), user.getPassword())) {
            throw CustomException.of(ExceptionCode.NOT_MATCH_PASSWORD);
        }

        // 토큰 발행
        return jwtTokenProvider.generateJwtToken(user);
    }
}