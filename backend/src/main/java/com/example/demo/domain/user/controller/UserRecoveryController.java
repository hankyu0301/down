package com.example.demo.domain.user.controller;


import com.example.demo.domain.user.dto.command.UserJoinCheckCommand;
import com.example.demo.domain.user.dto.response.EmailExistenceDTO;
import com.example.demo.domain.user.dto.response.PasswordResetResponseDTO;
import com.example.demo.domain.user.service.EmailService;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.domain.util.BaseResponse;
import com.example.demo.domain.util.FailResponse;
import com.example.demo.domain.util.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(
        name = "아이디 찾기/비밀번호 찾기",
        description = "분실한 비밀번호를 재설정하는 API"
)
@Slf4j
@RestController
@RequestMapping("${api.version-path}/user/")
@RequiredArgsConstructor
public class UserRecoveryController {

    private final UserService userService;
    private final EmailService emailService;

    @Operation(
            summary = "회원가입 여부 확인",
            description = "아이디를 찾기 위한 회원가입 여부를 확인합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "회원가입 여부 확인 요청",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = UserJoinCheckCommand.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원가입 여부 확인 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "회원가입 여부 확인 실패, 회원이 존재하지 않습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping("/find-id")
    public ResponseEntity<BaseResponse<EmailExistenceDTO>> checkUserJoin(
            @Validated @RequestBody UserJoinCheckCommand cmd
    ) {
        boolean result = userService.checkEmail(cmd.toUserDomain());

        EmailExistenceDTO responseDTO = EmailExistenceDTO.builder()
                .checkedEmail(cmd.getEmail())
                .existence(result)
                .build();

        SuccessResponse<EmailExistenceDTO> response = SuccessResponse.<EmailExistenceDTO>builder()
                .data(responseDTO)
                .message("이메일 중복 체크 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "임시 비밀번호 전송",
            description = "비밀번호 분실로 인한 임시 비밀번호를 전송합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "임시 비밀번호 전송 요청",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = UserJoinCheckCommand.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "임시 비밀번호 전송 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "임시 비밀번호 전송 실패, 회원이 존재하지 않습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping("/send-reset-password")
    public ResponseEntity<BaseResponse<PasswordResetResponseDTO>> sendResetPassword(
            @Validated @RequestBody UserJoinCheckCommand cmd
    ) throws MessagingException {

        boolean result = emailService.sendResetPassword(cmd.toUserDomain());

        PasswordResetResponseDTO responseDTO = PasswordResetResponseDTO.builder()
                .email(cmd.getEmail())
                .result(result)
                .build();

        SuccessResponse<PasswordResetResponseDTO> response = SuccessResponse.<PasswordResetResponseDTO>builder()
                .data(responseDTO)
                .message("임시 비밀번호 전송 성공")
                .build();

        return ResponseEntity.ok(response);
    }
}