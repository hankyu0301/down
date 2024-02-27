package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.dto.command.PasswordChangeCommand;
import com.example.demo.domain.user.dto.command.UserInfoChangeCommand;
import com.example.demo.domain.user.dto.response.PasswordChangeResponseDTO;
import com.example.demo.domain.user.dto.response.UserDeleteResponseDTO;
import com.example.demo.domain.user.dto.response.UserInfoChangeResponseDTO;
import com.example.demo.domain.user.dto.response.UserInfoResponseDTO;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.domain.util.BaseResponse;
import com.example.demo.domain.util.FailResponse;
import com.example.demo.domain.util.SuccessResponse;
import com.example.demo.global.auth.jwt.JwtTokenProvider;
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
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "회원",
        description = "회원 정보 조회, 회원 정보 수정, 회원 탈퇴"
)
@Slf4j
@RestController
@RequestMapping("${api.version-path}/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(
            summary = "회원들 정보 조회",
            description = "회원들 정보를 조회합니다.",
            tags = "회원"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원들 정보 조회 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "회원들 정보 조회 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<SuccessResponse<Page<UserInfoResponseDTO>>> getUserInfo(
            @Parameter(name = "page", description = "페이지 번호")
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @Parameter(name = "size", description = "페이지 크기")
            @RequestParam(defaultValue = "20", required = false) Integer size
    ) {
        Page<UserInfoResponseDTO> users = userService.getUsersInfo(page, size)
                .map(UserInfoResponseDTO::from);

        SuccessResponse<Page<UserInfoResponseDTO>> response = SuccessResponse.<Page<UserInfoResponseDTO>>builder()
                .data(users)
                .message("회원들 정보 조회 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    // 회원 정보 조회
    @Operation(
            summary = "회원 정보 조회",
            description = "회원 정보를 조회합니다.",
            tags = "회원"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원 정보 조회 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "회원 정보 조회 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<UserInfoResponseDTO>> getUserInfo(
            @Parameter(description = "회원 ID", required = true)
            @PathVariable Long id
    ) {
        UserInfoResponseDTO user = UserInfoResponseDTO.from(userService.getUserInfo(id));

        SuccessResponse<UserInfoResponseDTO> response = SuccessResponse.<UserInfoResponseDTO>builder()
                .data(user)
                .message("회원 정보 조회 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "회원 정보 수정",
            description = "회원 정보를 수정합니다.",
            tags = "회원"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "회원 정보 수정",
            required = true
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원 정보 수정 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "회원 정보 수정 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<SuccessResponse<UserInfoChangeResponseDTO>> updateUserInfo(
            @Parameter(description = "회원 ID", required = true)
            @PathVariable Long id,
            @RequestBody UserInfoChangeCommand cmd
    ) {
        boolean result = userService.updateUserInfo(id, cmd);

        UserInfoChangeResponseDTO dto = UserInfoChangeResponseDTO.builder()
                .result(result)
                .build();

        SuccessResponse<UserInfoChangeResponseDTO> response = SuccessResponse.<UserInfoChangeResponseDTO>builder()
                .data(dto)
                .message("회원 정보 수정 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "회원 탈퇴",
            description = "회원 탈퇴를 진행합니다.",
            tags = "회원"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "회원 탈퇴",
            required = true
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원 탈퇴 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "회원 탈퇴 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<UserDeleteResponseDTO>> deleteUser(
            @Parameter(description = "회원 ID", required = true)
            @PathVariable Long id
    ) {
        boolean result = userService.deleteUser(id);

        UserDeleteResponseDTO dto = UserDeleteResponseDTO.builder()
                .result(result)
                .build();

        SuccessResponse<UserDeleteResponseDTO> response = SuccessResponse.<UserDeleteResponseDTO>builder()
                .data(dto)
                .message("회원 탈퇴 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "비밀번호 변경",
            description = "회원의 비밀번호를 변경합니다.",
            tags = "회원"
    )
    @PostMapping("/{id}/password/change")
    public ResponseEntity<BaseResponse<PasswordChangeResponseDTO>> changePassword(
            @Parameter(description = "회원 ID", required = true)
            @PathVariable Long id,
            @RequestBody @Valid PasswordChangeCommand cmd,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String token = jwtTokenProvider.authorizationToJwt(authorizationHeader);
        String userEmail = jwtTokenProvider.getEmail(token);

        boolean result = userService.changePassword(id, cmd, userEmail);

        PasswordChangeResponseDTO responseDTO = PasswordChangeResponseDTO.builder()
                .success(result)
                .build();

        SuccessResponse<PasswordChangeResponseDTO> response =
                SuccessResponse.<PasswordChangeResponseDTO>builder()
                        .data(responseDTO)
                        .message("비밀번호 변경이 완료되었습니다.")
                        .build();

        return ResponseEntity.ok(response);
    }
}