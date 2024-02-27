package com.example.demo.global.auth.kakao;

import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.util.SuccessResponse;
import com.example.demo.global.auth.jwt.dto.JwtTokenDTO;
import com.example.demo.global.auth.jwt.JwtTokenProvider;
import com.example.demo.global.auth.jwt.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("${api.version-path}/oauth/kakao")
public class KaKaoOAuth2Controller {

    private final KakaoOAuth2Service kakaoOAuth2Service;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtService jwtService;

    @Operation(
            summary = "카카오 로그인",
            description = "카카오 로그인을 진행합니다.",
            tags = "로그인"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "카카오 로그인 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "카카오 로그인 실패"
            )
    })
    @GetMapping("/login")
    public void login(HttpServletResponse response) throws IOException {
        String url = kakaoOAuth2Service.getAuthorizationUrl();
        response.sendRedirect(url);
    }

    @Operation(
            summary = "카카오 로그인 콜백",
            description = "카카오 로그인 콜백을 진행합니다.",
            tags = "로그인"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "카카오 로그인 콜백 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "카카오 로그인 콜백 실패"
            )
    })
    @GetMapping("/callback")
    public ResponseEntity<SuccessResponse<JwtTokenDTO>> callback(
            @Parameter(description = "카카오 인가 코드 자동으로 입력됩니다.")
            @RequestParam String code
    ) {
        // 토큰 발급
        KaKaoAccessTokenResponse accessToken = kakaoOAuth2Service.getAccessToken(code);

        // 유저 정보 가져오기
        KakaoUserInfo userInfo = kakaoOAuth2Service.getUserInfo(accessToken.getAccessToken());

        // 유저 정보로 회원가입 or 로그인 처리
        User user = kakaoOAuth2Service.processOAuth2User(userInfo);

        Map<String, String> tokens = Map.of(
                "access-token", jwtTokenProvider.generateAccessToken(user),
                "refresh-token", jwtTokenProvider.generateRefreshToken(user)
        );

        jwtService.save(tokens);

        SuccessResponse<JwtTokenDTO> response = SuccessResponse.<JwtTokenDTO>builder()
                .data(JwtTokenDTO.of(tokens))
                .message("로그인 성공")
                .build();

        return ResponseEntity.ok(response);
    }
}