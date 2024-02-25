package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.dto.command.UserLoginCommand;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.domain.util.BaseResponse;
import com.example.demo.domain.util.FailResponse;
import com.example.demo.domain.util.SuccessResponse;
import com.example.demo.global.auth.jwt.JwtTokenDTO;
import io.swagger.v3.oas.annotations.Operation;
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
            @RequestBody @Valid UserLoginCommand cmd
    ) {
        String accessToken = userService.login(cmd);

        JwtTokenDTO jwtTokenDTO = JwtTokenDTO.of(accessToken);

        SuccessResponse<JwtTokenDTO> response = SuccessResponse.<JwtTokenDTO>builder()
                .data(jwtTokenDTO)
                .message("로그인 성공")
                .build();

        return ResponseEntity.ok(response);
    }
}