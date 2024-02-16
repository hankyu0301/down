package com.example.demo.global.auth.oauth;

import com.example.demo.domain.user.entity.UserRoleEnumType;
import com.example.demo.domain.user.repository.PendingEmailsRepository;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.auth.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.demo.domain.user.entity.UserEntity;

import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Slf4j
public class OAuth2UserService extends DefaultOAuth2UserService {

    @Value("${spring.security.oauth2.client.password-key}")
    private String OAUTH2_PASSWORD_KEY;

    private final UserRepository userRepository;
    private final PendingEmailsRepository pendingEmailsRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}", oAuth2User.getAttributes());

        String oAuthType = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = switch (oAuthType) {
            case "google" ->
                // 구글 로그인 요청
                    new GoogleUserInfo(oAuth2User.getAttributes());
            case "kakao" ->
                // 카카오 로그인 요청
                    new KakaoUserInfo(oAuth2User.getAttributes());
            case "naver" ->
                // 네이버 로그인 요청
                    new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
            default -> {
                log.error("지원되지 않는 제공업체입니다.");
                throw new OAuth2AuthenticationException(new OAuth2Error("unsupported_provider"), "지원되지 않는 제공업체입니다.");
            }
        };

        LoginEnumType provider = LoginEnumType.valueOf(oAuth2UserInfo.getProvider().toUpperCase());
        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String username = oAuth2UserInfo.getName();

        Optional<UserEntity> optUser = userRepository.findByEmailAndProviderId(email, providerId);

        if (optUser.isPresent()) {
            return new PrincipalDetails(optUser.get(), oAuth2User.getAttributes());
        }

        if (userRepository.existsByEmail(email) || pendingEmailsRepository.existsByEmail(email)) {
            throw new OAuth2AuthenticationException(new OAuth2Error("email_exists"), "이미 가입된 이메일입니다.");
        }

        UserEntity newUser = UserEntity.builder()
                .email(email)
                .username(username)
                .nickName(null)
                .provider(provider)
                .termsAgree(true)
                .password(passwordEncoder.encode(email + OAUTH2_PASSWORD_KEY))
                .providerId(providerId)
                .role(UserRoleEnumType.ROLE_USER)
                .build();

        UserEntity user = userRepository.save(newUser);
        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}