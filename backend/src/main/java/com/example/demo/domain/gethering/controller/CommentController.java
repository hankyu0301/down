package com.example.demo.domain.gethering.controller;

import com.example.demo.domain.gethering.dto.response.CommentResponseDTO;
import com.example.demo.domain.gethering.dto.command.CommentCommand;
import com.example.demo.domain.gethering.dto.response.CommentDeleteResponseDTO;
import com.example.demo.domain.gethering.entity.Comment;
import com.example.demo.domain.gethering.service.CommentService;
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
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "댓글",
        description = "댓글 카테고리 CRUD API"
)
@Slf4j
@RestController
@RequestMapping("${api.version-path}/gethering")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(
            summary = "댓글 생성",
            description = "댓글을 생성합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "댓글 생성 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "댓글 생성 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping("/{getheringId}/comment/users/{userId}")
    public ResponseEntity<SuccessResponse<CommentResponseDTO>> register(
            @Parameter(description = "모임 ID", example = "1")
            @PathVariable Long getheringId,
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId,
            @RequestBody CommentCommand cmd
    ) {
        Comment command = commentService.register(getheringId, userId, cmd);

        SuccessResponse<CommentResponseDTO> response = SuccessResponse.<CommentResponseDTO>builder()
                .data(CommentResponseDTO.of(command))
                .message("댓글 생성 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    // 댓글 조회
    @Operation(
            summary = "댓글 조회",
            description = "댓글을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "댓글 조회 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "댓글 조회 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @GetMapping("/{getheringId}/comment")
    public ResponseEntity<SuccessResponse<Page<CommentResponseDTO>>> getComments(
            @Parameter(description = "모임 ID", example = "1")
            @PathVariable Long getheringId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<CommentResponseDTO> list = commentService.list(getheringId, page, size)
                .map(CommentResponseDTO::of);

        SuccessResponse<Page<CommentResponseDTO>> response = SuccessResponse.<Page<CommentResponseDTO>>builder()
                .data(list)
                .message("댓글 조회 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    // 댓글 수정
    @Operation(
            summary = "댓글 수정",
            description = "댓글을 수정합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "댓글 수정 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "댓글 수정 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PutMapping("/{getheringId}/comment/{commentId}/users/{userId}")
    public ResponseEntity<SuccessResponse<CommentResponseDTO>> update(
            @Parameter(description = "모임 ID", example = "1")
            @PathVariable Long getheringId,
            @Parameter(description = "댓글 ID", example = "1")
            @PathVariable Long commentId,
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId,
            @RequestBody CommentCommand cmd
    ) {
        Comment command = commentService.update(getheringId, commentId, cmd);

        SuccessResponse<CommentResponseDTO> response = SuccessResponse.<CommentResponseDTO>builder()
                .data(CommentResponseDTO.of(command))
                .message("댓글 수정 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "댓글 삭제",
            description = "댓글을 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "댓글 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "댓글 삭제 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @DeleteMapping("/{getheringId}/comment/{commentId}/users/{userId}")
    public ResponseEntity<SuccessResponse<CommentDeleteResponseDTO>> delete(
            @Parameter(description = "모임 ID", example = "1")
            @PathVariable Long getheringId,
            @Parameter(description = "댓글 ID", example = "1")
            @PathVariable Long commentId,
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId
    ) {
        commentService.delete(getheringId, commentId);

        SuccessResponse<CommentDeleteResponseDTO> response = SuccessResponse.<CommentDeleteResponseDTO>builder()
                .data(CommentDeleteResponseDTO.of(commentId))
                .message("댓글 삭제 성공")
                .build();

        return ResponseEntity.ok(response);
    }
}