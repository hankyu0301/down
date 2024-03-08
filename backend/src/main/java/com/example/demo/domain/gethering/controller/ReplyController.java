package com.example.demo.domain.gethering.controller;

import com.example.demo.domain.gethering.dto.response.ReplyResponseDTO;
import com.example.demo.domain.gethering.dto.command.ReplyCommand;
import com.example.demo.domain.gethering.dto.response.ReplyDeleteResponseDTO;
import com.example.demo.domain.gethering.entity.Reply;
import com.example.demo.domain.gethering.service.ReplyService;
import com.example.demo.domain.util.FailResponse;
import com.example.demo.domain.util.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "대댓글",
        description = "대댓글 카테고리 CRUD API"
)
@Slf4j
@RestController
@RequestMapping("${api.version-path}/gethering/")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    // 대댓글 생성
    @Operation(
            summary = "대댓글 생성",
            description = "대댓글을 생성합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "대댓글 생성 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "대댓글 생성 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping("/{getheringId}/comment/{commentId}/reply/users/{userId}")
    public ResponseEntity<SuccessResponse<ReplyResponseDTO>> register(
            @Parameter(description = "모임 ID", example = "1")
            @PathVariable Long getheringId,
            @Parameter(description = "댓글 ID", example = "1")
            @PathVariable Long commentId,
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId,
            ReplyCommand cmd
    ) {
        Reply reply = replyService.register(getheringId, commentId, userId, cmd);

        SuccessResponse<ReplyResponseDTO> response = SuccessResponse.<ReplyResponseDTO>builder()
                .data(ReplyResponseDTO.of(reply))
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "대댓글 수정",
            description = "대댓글을 수정합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "대댓글 수정 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "대댓글 수정 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping("/{getheringId}/comment/{commentId}/reply/{replyId}/users/{userId}")
    public ResponseEntity<SuccessResponse<ReplyResponseDTO>> modify(
            @Parameter(description = "모임 ID", example = "1")
            @PathVariable Long getheringId,
            @Parameter(description = "댓글 ID", example = "1")
            @PathVariable Long commentId,
            @Parameter(description = "대댓글 ID", example = "1")
            @PathVariable Long replyId,
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId,
            ReplyCommand cmd
    ) {
        Reply reply = replyService.modify(getheringId, commentId, replyId, userId, cmd);

        SuccessResponse<ReplyResponseDTO> response = SuccessResponse.<ReplyResponseDTO>builder()
                .data(ReplyResponseDTO.of(reply))
                .build();

        return ResponseEntity.ok(response);
    }

    // 대댓글 삭제
    @Operation(
            summary = "대댓글 삭제",
            description = "대댓글을 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "대댓글 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "대댓글 삭제 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @DeleteMapping("/{getheringId}/comment/{commentId}/reply/{replyId}/users/{userId}")
    public ResponseEntity<SuccessResponse<ReplyDeleteResponseDTO>> delete(
            @Parameter(description = "모임 ID", example = "1")
            @PathVariable Long getheringId,
            @Parameter(description = "댓글 ID", example = "1")
            @PathVariable Long commentId,
            @Parameter(description = "대댓글 ID", example = "1")
            @PathVariable Long replyId,
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId
    ) {
        replyService.delete(getheringId, commentId, replyId, userId);

        SuccessResponse<ReplyDeleteResponseDTO> response = SuccessResponse.<ReplyDeleteResponseDTO>builder()
                .data(ReplyDeleteResponseDTO.of(replyId))
                .build();

        return ResponseEntity.ok(response);
    }
}