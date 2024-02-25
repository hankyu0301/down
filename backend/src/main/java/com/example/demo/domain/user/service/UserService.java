package com.example.demo.domain.user.service;

import com.example.demo.domain.user.dto.command.*;
import com.example.demo.domain.user.entity.PendingEmail;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.entity.UserRoleEnumType;
import com.example.demo.domain.user.repository.PendingEmailsRepository;
import com.example.demo.domain.user.repository.UserPagingAndSortingRepository;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.auth.LoginEnumType;
import com.example.demo.global.auth.jwt.JwtTokenProvider;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserPagingAndSortingRepository userPagingAndSortingRepository;
    private final PendingEmailsRepository pendingEmailsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public User join(UserJoinCommand cmd) {

        // 이메일 확인 코드 보류 이메일에 있는지 확인
        PendingEmail pendingEmail = pendingEmailsRepository.findByEmail(cmd.getEmail())
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_VERIFIED_EMAIL));

        // 이메일 확인 코드가 일치하는지 확인
        if (!pendingEmail.getAuthCode().equals(cmd.getCode())) {
            throw CustomException.of(ExceptionCode.NOT_MATCH_EMAIL_AUTH_CODE);
        }

        // 회원 가입 처리
        User user = cmd.toEntity();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setProvider(LoginEnumType.SERVICE);
        user.setRole(UserRoleEnumType.ROLE_USER);
        user.setProviderId(0L);

        // 회원 가입 완료 후 팬딩 이메일 삭제
        pendingEmailsRepository.deleteByEmail(user.getEmail());
        return userRepository.save(user);
    }

    public boolean checkEmail(UserJoinCheckCommand cmd) {
        if (!userRepository.existsByEmailAndUserName(cmd.getEmail(), cmd.getUserName())) {
            throw CustomException.of(ExceptionCode.NOT_EXIST_USER);
        }
        return true;
    }

    public String login(UserLoginCommand cmd) {

        // 이메일 존재 확인
        User user = userRepository.findByEmail(cmd.getEmail())
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_EXIST_USER));

        // 비밀번호 확인
        if (!passwordEncoder.matches(cmd.getPassword(), user.getPassword())) {
            throw CustomException.of(ExceptionCode.NOT_MATCH_PASSWORD);
        }

        // 토큰 발행
        return jwtTokenProvider.generateJwtToken(user);
    }

    public boolean checkNickName(CheckNickNameCommand cmd) {
        Assert.notNull(cmd, "cmd 필수 입력값 입니다. 확인해 주세요");

        if (userRepository.existsByNickName(cmd.getNickName())) {
            throw CustomException.of(ExceptionCode.EXIST_NICKNAME);
        }

        return true;
    }

    public boolean changePassword(Long id, PasswordChangeCommand cmd, String userEmail) {
        Assert.notNull(id, "id 필수 입력값 입니다. 확인해 주세요");
        Assert.notNull(cmd, "cmd 필수 입력값 입니다. 확인해 주세요");
        Assert.notNull(userEmail, "userEmail 필수 입력값 입니다. 확인해 주세요");

        User user = userRepository.findById(id)
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_EXIST_USER));

        if (!user.getEmail().equals(userEmail)) {
            throw CustomException.of(ExceptionCode.INVALID_AUTH_HEADER);
        }

        user.setPassword(passwordEncoder.encode(cmd.getPassword()));

        return true;
    }

    public Page<User> getUsersInfo(Integer page, Integer size) {
        Assert.notNull(page, "page 필수 입력값 입니다. 확인해 주세요");
        Assert.notNull(size, "size 필수 입력값 입니다. 확인해 주세요");

        return userPagingAndSortingRepository
                .findAll(PageRequest.of(page, size));
    }

    public boolean updateUserInfo(Long id, UserInfoChangeCommand cmd) {
        Assert.notNull(id, "id 필수 입력값 입니다. 확인해 주세요");
        Assert.notNull(cmd, "cmd 필수 입력값 입니다. 확인해 주세요");

        User user = userRepository.findById(id)
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_EXIST_USER));

        user.setNickName(cmd.getNickName());

        userRepository.save(user);

        return true;
    }

    public boolean deleteUser(Long id) {
        Assert.notNull(id, "id 필수 입력값 입니다. 확인해 주세요");

        User user = userRepository.findById(id)
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_EXIST_USER));

        userRepository.delete(user);

        return true;
    }

    public User getUserInfo(Long id) {
        Assert.notNull(id, "id 필수 입력값 입니다. 확인해 주세요");

        return userRepository.findById(id)
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_EXIST_USER));
    }
}