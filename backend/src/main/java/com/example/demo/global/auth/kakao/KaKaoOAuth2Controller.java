package com.example.demo.global.auth.kakao;

import com.example.demo.domain.user.model.User;
import com.example.demo.domain.util.SuccessResponse;
import com.example.demo.global.auth.jwt.JwtTokenDTO;
import com.example.demo.global.auth.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("${api.version-path}/oauth/kakao")
public class KaKaoOAuth2Controller {

    private final KakaoOAuth2Service kakaoOAuth2Service;
    private final JwtTokenProvider jwtTokenProvider;

    // 카카오 로그인 폼으로 이동 시키기
    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        String url = kakaoOAuth2Service.getAuthorizationUrl();
        response.sendRedirect(url);
    }

    // 코드 받는 컨트롤러
    @GetMapping("/callback")
    public ResponseEntity<SuccessResponse<JwtTokenDTO>> callback(@RequestParam String code) {

        // 토큰 발급
        KaKaoAccessTokenResponse accessToken = kakaoOAuth2Service.getAccessToken(code);

        // 유저 정보 가져오기
        KakaoUserInfo userInfo = kakaoOAuth2Service.getUserInfo(accessToken.getAccessToken());

        // 유저 정보로 회원가입 or 로그인 처리
        User user = kakaoOAuth2Service.processOAuth2User(userInfo);

        // JWT 토큰 발급
        SuccessResponse<JwtTokenDTO> response = SuccessResponse.<JwtTokenDTO>builder()
                .data(JwtTokenDTO.of(jwtTokenProvider.generateJwtToken(user)))
                .message("로그인 성공")
                .build();

        return ResponseEntity.ok(response);
    }
}