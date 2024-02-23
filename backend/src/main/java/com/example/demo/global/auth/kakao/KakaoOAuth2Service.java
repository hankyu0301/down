package com.example.demo.global.auth.kakao;

import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.entity.UserRoleEnumType;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.auth.LoginEnumType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;



@RequiredArgsConstructor
@Service
@Transactional
public class KakaoOAuth2Service {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public KaKaoAccessTokenResponse getAccessToken(String code) {
        Assert.notNull(code, "인증 코드가 존재하지 않습니다.");

        WebClient webClient = WebClient.create();

        String url = "https://kauth.kakao.com/oauth/token";

        String parameters = "grant_type=authorization_code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&code=" + code +
                "&client_secret=" + clientSecret;

        return webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(parameters)
                .retrieve()
                .bodyToMono(KaKaoAccessTokenResponse.class)
                .block();
    }

    public String getAuthorizationUrl() {
        return "https://kauth.kakao.com/oauth/authorize" +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUri;
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        Assert.notNull(accessToken, "토큰이 존재하지 않습니다.");

        WebClient webClient = WebClient.create();

        String url = "https://kapi.kakao.com/v2/user/me";

        String response = webClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return KakaoUserInfo.of(response);
    }

    public User processOAuth2User(KakaoUserInfo userInfo) {
        Assert.notNull(userInfo, "유저 정보가 존재하지 않습니다.");

        // 기존 유저 확인

        return userRepository.findByEmailAndProviderId(userInfo.getEmail(), userInfo.getId())
                .orElseGet(() -> {
                    // 신규 유저
                    User newUser = User.builder()
                            .email(userInfo.getEmail())
                            .userName(userInfo.getName())
                            .nickName(userInfo.getNickname())
                            .provider(LoginEnumType.KAKAO)
                            .providerId(userInfo.getId())
                            .role(UserRoleEnumType.ROLE_USER)
                            .gender(userInfo.getGender())
                            .password(passwordEncoder.encode(userInfo.getId() + "kakao" + clientSecret))
                            .birth(userInfo.getBirthday())
                            .termsAgree(true)
                            .build();

                    return userRepository.save(newUser);
                });
    }
}