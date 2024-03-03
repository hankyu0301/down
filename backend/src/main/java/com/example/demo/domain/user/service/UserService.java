package com.example.demo.domain.user.service;

import com.example.demo.domain.region.dto.jpql.RegionNameDTO;
import com.example.demo.domain.region.repository.SiGunGuRepository;
import com.example.demo.domain.region.service.SiDoService;
import com.example.demo.domain.user.dto.command.*;
import com.example.demo.domain.user.entity.PendingEmail;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.entity.UserRoleEnumType;
import com.example.demo.domain.user.repository.PendingEmailsRepository;
import com.example.demo.domain.user.repository.UserPagingAndSortingRepository;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.auth.LoginEnumType;
import com.example.demo.global.auth.jwt.JwtTokenProvider;
import com.example.demo.global.auth.jwt.entity.AccessToken;
import com.example.demo.global.auth.jwt.entity.RefreshToken;
import com.example.demo.global.auth.jwt.repository.AccessTokenRepository;
import com.example.demo.global.auth.jwt.repository.RefreshTokenRepository;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserPagingAndSortingRepository userPagingAndSortingRepository;
    private final PendingEmailsRepository pendingEmailsRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final SiGunGuRepository siGunGuRepository;
    private final SiDoService siDoService;

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

    public Map<String, String> login(UserLoginCommand cmd) {

        // 이메일 존재 확인
        User user = userRepository.findByEmail(cmd.getEmail())
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_EXIST_USER));

        // 비밀번호 확인
        if (!passwordEncoder.matches(cmd.getPassword(), user.getPassword())) {
            throw CustomException.of(ExceptionCode.NOT_MATCH_PASSWORD);
        }

        // 토큰 발행
        return Map.of(
                "accessToken", jwtTokenProvider.generateAccessToken(user),
                "refreshToken", jwtTokenProvider.generateRefreshToken(user)
        );
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

    public Map<String, String> refresh(String refreshToken) {
        Assert.notNull(refreshToken, "refreshToken 필수 입력값 입니다. 확인해 주세요");

        // 토큰 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw CustomException.of(ExceptionCode.INVALID_TOKEN);
        }

        // 토큰에서 회원 정보 추출
        String email = jwtTokenProvider.getEmail(refreshToken);

        // 회원 정보로 회원 정보 가져오기
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_EXIST_USER));

        List<AccessToken> accessTokens = accessTokenRepository.findAllByEmail(email);
        accessTokens.forEach(accessToken -> accessToken.setUsable(false));
        accessTokenRepository.saveAll(accessTokens);

        String nweAccessToken = jwtTokenProvider.generateAccessToken(user);

        AccessToken accessToken = AccessToken.builder()
                .email(email)
                .accessToken(nweAccessToken)
                .expiredAt(jwtTokenProvider.getExpire(nweAccessToken))
                .isUsable(true)
                .build();

        accessTokenRepository.save(accessToken);

        // 토큰 발행
        return Map.of(
                "accessToken", nweAccessToken,
                "refreshToken", refreshToken
        );
    }

    public void logout(String token) {
        Assert.notNull(token, "token 필수 입력값 입니다. 확인해 주세요");

        // 토큰 검증
        if (!jwtTokenProvider.validateToken(token)) {
            throw CustomException.of(ExceptionCode.INVALID_TOKEN);
        }

        // 토큰에서 회원 정보 추출
        String email = jwtTokenProvider.getEmail(token);

        List<AccessToken> accessTokens = accessTokenRepository.findAllByEmail(email);
        accessTokens.forEach(accessToken -> accessToken.setUsable(false));
        accessTokenRepository.saveAll(accessTokens);

        List<RefreshToken> refreshTokens = refreshTokenRepository.findAllByEmail(email);
        refreshTokens.forEach(refreshToken -> refreshToken.setUsable(false));
        refreshTokenRepository.saveAll(refreshTokens);
    }

    public RegionNameDTO registerLegalAddress(Long id, LegalAddressCommand cmd) {
        Assert.notNull(id, "id 필수 입력값 입니다. 확인해 주세요");
        Assert.notNull(cmd, "cmd 필수 입력값 입니다. 확인해 주세요");

        User user = userRepository.findById(id)
                .orElseThrow(() -> CustomException.of(ExceptionCode.NOT_EXIST_USER));

        if (siGunGuRepository.existsByLegalAddressCode(cmd.getLegalAddressCode())) {
            throw CustomException.of(ExceptionCode.NOT_FOUND_SI_GUN_GU);
        }

        user.setLegalAddressCode(cmd.getLegalAddressCode());
        userRepository.save(user);

        return siDoService.getAddressName(cmd.getLegalAddressCode());
    }
}