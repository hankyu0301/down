package com.example.demo.domain.user.controller;

import com.example.demo.domain.region.dto.jpql.RegionNameDTO;
import com.example.demo.domain.user.dto.command.*;
import com.example.demo.domain.user.dto.response.*;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.model.EmailVerification;
import com.example.demo.domain.user.service.EmailService;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.domain.util.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "회원가입",
        description = "회원가입을 위한 API"
)
@Slf4j
@RestController
@RequestMapping("${api.version-path}/users")
@RequiredArgsConstructor
public class UserJoinController {

    private final EmailService emailService;
    private final UserService userService;

    @Operation(
            summary = "이메일 중복 체크",
            description = "회원가입을 진행하는 사용자의 이메일이 이미 사용중인지 확인합니다.",
            tags = {"회원가입"}
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "중복 체크할 이메일 DTO",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CheckEmailCommand.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "이메일 중복 체크 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description =
                            """
                            - 이미 사용중인 이메일입니다.
                            """,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping("/email/check")
    public ResponseEntity<BaseResponse<CheckEmailResponseDTO>> checkEmail(
            @RequestBody @Valid CheckEmailCommand cmd
    ) {
        boolean result = emailService.registerPendingEmail(cmd.getEmail());

        CheckEmailResponseDTO responseDTO = CheckEmailResponseDTO.builder()
                .checkedEmail(cmd.getEmail())
                .available(result)
                .build();

        SuccessResponse<CheckEmailResponseDTO> response =
                SuccessResponse.<CheckEmailResponseDTO>builder()
                        .data(responseDTO)
                        .message("사용 가능한 이메일입니다.")
                        .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "이메일 인증코드 발송",
            description = "회원가입을 진행하는 사용자의 이메일로 인증코드를 발송합니다.",
            tags = {"회원가입"}
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "인증코드 발송할 이메일 DTO",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SendEmailVerificationCommand.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "이메일 인증코드 발송 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description =
                            """
                            - 이미 사용중인 이메일입니다.
                            - 인증 횟수 5번을 초과하였습니다.
                            """,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping("/email/send")
    public ResponseEntity<BaseResponse<SendEmailVerificationResponseDTO>> sendEmailVerification(
            @RequestBody @Valid SendEmailVerificationCommand cmd
    ) throws MessagingException {
        EmailVerification domain = emailService.sendEmailVerification(cmd);

        SendEmailVerificationResponseDTO responseDTO = SendEmailVerificationResponseDTO.builder()
                .success(true)
                .email(domain.getEmail())
                .build();

        SuccessResponse<SendEmailVerificationResponseDTO> response =
                SuccessResponse.<SendEmailVerificationResponseDTO>builder()
                        .data(responseDTO)
                        .message("이메일 인증코드가 발송되었습니다.")
                        .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "이메일 인증코드 확인",
            description = "회원가입을 진행하는 사용자의 이메일로 발송된 인증코드를 확인합니다.",
            tags = {"회원가입"}
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "인증코드 확인할 이메일 DTO",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CheckEmailVerificationCommand.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "이메일 인증코드 확인 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description =
                            """
                            - 인증코드가 존재하지 않습니다. 이메일 인증을 다시 진행해주세요.
                            - 보류 이메일이 존재하지 않습니다. 이메일 인증을 다시 진행해주세요.
                            - 인증코드가 일치하지 않습니다.
                            """,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping("/email/verify")
    public ResponseEntity<BaseResponse<CheckEmailVerificationResponseDTO>> checkEmailVerification(
            @RequestBody @Valid CheckEmailVerificationCommand cmd
    ) {
        boolean success = emailService.checkEmailVerification(cmd);

        CheckEmailVerificationResponseDTO responseDTO = CheckEmailVerificationResponseDTO.builder()
                .email(cmd.getEmail())
                .result(success)
                .build();

        SuccessResponse<CheckEmailVerificationResponseDTO> response =
                SuccessResponse.<CheckEmailVerificationResponseDTO>builder()
                        .data(responseDTO)
                        .message("이메일 인증코드 확인이 완료되었습니다.")
                        .build();

        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "닉네임 중복 체크",
            description = "회원가입을 진행하는 사용자의 닉네임이 이미 사용중인지 확인합니다.",
            tags = {"회원가입"}
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "중복 체크할 닉네임 DTO",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CheckNickNameCommand.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "닉네임 중복 체크 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description =
                            """
                            - 이미 사용중인 닉네임입니다.
                            """,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping("/nickname/check")
    public ResponseEntity<BaseResponse<CheckNickNameResponseDTO>> checkNickName(
            @RequestBody @Valid CheckNickNameCommand cmd
    ) {
        boolean result = userService.checkNickName(cmd);

        CheckNickNameResponseDTO responseDTO = CheckNickNameResponseDTO.builder()
                .nickName(cmd.getNickName())
                .available(result)
                .build();

        SuccessResponse<CheckNickNameResponseDTO> response =
                SuccessResponse.<CheckNickNameResponseDTO>builder()
                        .data(responseDTO)
                        .message("사용 가능한 닉네임입니다.")
                        .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "회원가입",
            description = "회원가입을 진행합니다.",
            tags = {"회원가입"}
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "회원가입 정보 DTO",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserJoinCommand.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원가입 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description =
                            """
                            - 이메일 유효성 검사를 진행하지 않은 이메일 주소에 대한 요청입니다.
                            - 이메일 인증코드가 일치하지 않습니다.
                            """,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<BaseResponse<UserJoinResponseDTO>> join(
            @RequestBody @Valid UserJoinCommand cmd
    ) {
        User user = userService.join(cmd);

        UserJoinResponseDTO responseDTO = UserJoinResponseDTO.builder()
                .id(user.getId())
                .email(cmd.getEmail())
                .build();

        SuccessResponse<UserJoinResponseDTO> response =
                SuccessResponse.<UserJoinResponseDTO>builder()
                        .data(responseDTO)
                        .message("회원가입이 완료되었습니다.")
                        .build();

        // 생성 코드 반환
        return ResponseEntity.ok(response);
    }

    // 법정동 코드 등록
    @Operation(
            summary = "법정동 코드 등록",
            description = "회원가입을 진행하는 사용자의 법정동 코드를 등록합니다.",
            tags = {"회원가입"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "법정동 코드 등록 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "법정동 코드 등록 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping("/{id}/address")
    public ResponseEntity<BaseResponse<LegalAddressResponseDTO>> registerLegalAddress(
            @PathVariable Long id,
            @RequestBody @Valid LegalAddressCommand cmd
    ) {
        RegionNameDTO regionNameDTO = userService.registerLegalAddress(id, cmd);

        LegalAddressResponseDTO responseDTO = LegalAddressResponseDTO.builder()
                .userId(id)
                .legalAddressCode(cmd.getLegalAddressCode())
                .address(regionNameDTO.getSiDoName())
                .build();

        SuccessResponse<LegalAddressResponseDTO> response =
                SuccessResponse.<LegalAddressResponseDTO>builder()
                        .data(responseDTO)
                        .message("법정동 코드 등록이 완료되었습니다.")
                        .build();

        return ResponseEntity.ok(response);
    }
}