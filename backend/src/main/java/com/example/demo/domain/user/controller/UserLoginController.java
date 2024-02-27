package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.dto.response.UserLogoutResponseDTO;
import com.example.demo.domain.user.dto.command.UserLoginCommand;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.domain.util.BaseResponse;
import com.example.demo.domain.util.FailResponse;
import com.example.demo.domain.util.SuccessResponse;
import com.example.demo.global.auth.jwt.JwtTokenProvider;
import com.example.demo.global.auth.jwt.dto.JwtTokenDTO;
import com.example.demo.global.auth.jwt.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Tag(
        name = "로그인",
        description = "로그인 API"
)
@Slf4j
@RestController
@RequestMapping("${api.version-path}/user/login")
@RequiredArgsConstructor
public class UserLoginController {

    private final UserService userService;
    private final JwtService jwtService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(
            summary = "로그인",
            description = "로그인을 진행합니다.",
            tags = "로그인"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "로그인 정보",
            required = true
    )
    @ApiResponses(value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "로그인 성공"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description =
                                    """
                                    - 회원이 존재하지 않습니다.
                                    - 비밀번호가 일치하지 않습니다.
                                    """,
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FailResponse.class)
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<BaseResponse<JwtTokenDTO>> login(
            @Parameter(description = "로그인 정보", required = true)
            @RequestBody @Valid UserLoginCommand cmd
    ) {
        Map<String, String> tokens = userService.login(cmd);
        jwtService.save(tokens);

        SuccessResponse<JwtTokenDTO> response = SuccessResponse.<JwtTokenDTO>builder()
                .data(JwtTokenDTO.of(tokens))
                .message("로그인 성공")
                .build();

        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "토큰 재발급",
            description = "재발급 토큰을 이용하여 엑세스 토큰을 재발급합니다.",
            tags = "로그인"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "토큰 재발급 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "토큰 재발급 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @GetMapping("/refresh")
    public ResponseEntity<BaseResponse<JwtTokenDTO>> refresh(
            @Parameter(description = "재발급 토큰", required = true)
            @RequestHeader("Authorization") String refreshToken
    ) {
        String token = jwtTokenProvider.parseToken(refreshToken);
        Map<String, String> tokens = userService.refresh(token);

        SuccessResponse<JwtTokenDTO> response = SuccessResponse.<JwtTokenDTO>builder()
                .data(JwtTokenDTO.of(tokens))
                .message("토큰 재발급 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "로그아웃",
            description = "로그아웃을 진행합니다.",
            tags = "로그인"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "로그아웃 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "로그아웃 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @GetMapping("/logout")
    public ResponseEntity<BaseResponse<UserLogoutResponseDTO>> logout(
            @Parameter(description = "인증 토큰", required = true)
            @RequestHeader("Authorization") String accessToken
    ) {
        String token = jwtTokenProvider.parseToken(accessToken);
        userService.logout(token);

        SuccessResponse<UserLogoutResponseDTO> response = SuccessResponse.<UserLogoutResponseDTO>builder()
                .data(UserLogoutResponseDTO.of())
                .message("로그아웃 성공")
                .build();

        return ResponseEntity.ok(response);
    }

}