package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.dto.command.UserSportsInfoCommand;
import com.example.demo.domain.user.dto.response.UserSportsInfoDeleteResponseDTO;
import com.example.demo.domain.user.dto.response.UserSportsInfoResponseDTO;
import com.example.demo.domain.user.dto.response.UserSportsInfosResponseDTO;
import com.example.demo.domain.user.dto.command.UserSportsInfosCommand;
import com.example.demo.domain.user.entity.UserSportsInfo;
import com.example.demo.domain.user.service.UserSportsInfoService;
import com.example.demo.domain.util.SuccessResponse;
import com.example.demo.global.auth.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "사용자 운동 경력",
        description = "사용자의 운동 경력을 조회하는 API"
)
@Slf4j
@RestController
@RequestMapping("${api.version-path}/users/")
@RequiredArgsConstructor
public class UserSportsController {

    private final UserSportsInfoService userSportsInfoService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("{id}/sports")
    public ResponseEntity<SuccessResponse<UserSportsInfosResponseDTO>> register(
            @PathVariable Long id,
            @RequestBody @Valid UserSportsInfosCommand cmd,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String token = jwtTokenProvider.parseToken(authorizationHeader);
        jwtTokenProvider.isAuthorized(token, id);
        String email = jwtTokenProvider.getEmail(token);

        List<UserSportsInfo> userSportsInfos = userSportsInfoService.register(id, cmd, email);

        SuccessResponse<UserSportsInfosResponseDTO> response = SuccessResponse.<UserSportsInfosResponseDTO>builder()
                .data(UserSportsInfosResponseDTO.of(userSportsInfos))
                .message("운동 경력 입력 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}/sports")
    public ResponseEntity<SuccessResponse<UserSportsInfosResponseDTO>> list(
            @PathVariable Long id
    ) {
        List<UserSportsInfo> userSportsInfos = userSportsInfoService.list(id);

        SuccessResponse<UserSportsInfosResponseDTO> response = SuccessResponse.<UserSportsInfosResponseDTO>builder()
                .data(UserSportsInfosResponseDTO.of(userSportsInfos))
                .message("운동 경력 조회 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("{userId}/sports/{sportsId}")
    public ResponseEntity<SuccessResponse<UserSportsInfoResponseDTO>> update(
            @PathVariable Long userId,
            @PathVariable Long sportsId,
            @RequestBody @Valid UserSportsInfoCommand cmd,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String token = jwtTokenProvider.parseToken(authorizationHeader);
        jwtTokenProvider.isAuthorized(token, userId);
        String email = jwtTokenProvider.getEmail(token);

        UserSportsInfo userSportsInfos = userSportsInfoService.update(userId, sportsId, cmd);

        SuccessResponse<UserSportsInfoResponseDTO> response = SuccessResponse.<UserSportsInfoResponseDTO>builder()
                .data(UserSportsInfoResponseDTO.of(userSportsInfos))
                .message("운동 경력 수정 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{userId}/sports/{sportsId}")
    public ResponseEntity<SuccessResponse<UserSportsInfoDeleteResponseDTO>> delete(
            @PathVariable Long userId,
            @PathVariable Long sportsId,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String token = jwtTokenProvider.parseToken(authorizationHeader);
        jwtTokenProvider.isAuthorized(token, userId);

        userSportsInfoService.delete(userId, sportsId);

        SuccessResponse<UserSportsInfoDeleteResponseDTO> response = SuccessResponse.<UserSportsInfoDeleteResponseDTO>builder()
                .data(UserSportsInfoDeleteResponseDTO.of(sportsId))
                .message("운동 경력 삭제 성공")
                .build();

        return ResponseEntity.ok(response);
    }
}