package com.example.demo.global.auth.oauth;

import com.example.demo.domain.user.entity.UserRole;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.auth.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.demo.domain.user.entity.User;

import java.util.Map;


@RequiredArgsConstructor
@Service
@Slf4j
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;


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

        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String loginId = provider + "_" + providerId;
        String nickname = oAuth2UserInfo.getName();

        log.info("oAuth2UserInfo : {}", oAuth2UserInfo);

        User user = userRepository.findByLoginId(loginId)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .loginId(loginId)
                            .email(email)
                            .userName(nickname)
                            .provider(provider)
                            .providerId(providerId)
                            .role(UserRole.ROLE_USER)
                            .build();
                    return userRepository.save(newUser);
                });

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}