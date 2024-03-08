package com.example.demo.domain.gethering.controller;

import com.example.demo.domain.gethering.dto.command.TagCommand;
import com.example.demo.domain.gethering.dto.response.TagDeleteResponseDTO;
import com.example.demo.domain.gethering.dto.response.TagResponseDTO;
import com.example.demo.domain.gethering.dto.response.TagSearchResponseDTO;
import com.example.demo.domain.gethering.entity.Tag;
import com.example.demo.domain.gethering.service.TagService;
import com.example.demo.domain.util.FailResponse;
import com.example.demo.domain.util.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@io.swagger.v3.oas.annotations.tags.Tag(
        name = "태그",
        description = "태그 카테고리 CRUD API"
)
@Slf4j
@RestController
@RequestMapping("${api.version-path}/gethering/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @Operation(
            summary = "태그 생성",
            description = "태그를 생성합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "태그 생성 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "태그 생성 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<SuccessResponse<TagResponseDTO>> register(
            @RequestBody TagCommand cmd
    ) {
         Tag tag = tagService.register(cmd);

         SuccessResponse<TagResponseDTO> response = SuccessResponse.<TagResponseDTO>builder()
                 .data(TagResponseDTO.of(tag))
                 .message("태그 생성 성공")
                 .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "태그 조회",
            description = "태그를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "태그 조회 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "태그 조회 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<SuccessResponse<TagSearchResponseDTO>> list(
            @RequestParam("name") String name
    ) {
        List<Tag> tag = tagService.getTag(name);

        SuccessResponse<TagSearchResponseDTO> response = SuccessResponse.<TagSearchResponseDTO>builder()
                .data(TagSearchResponseDTO.of(tag))
                .message("태그 조회 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    // 태그 수정
    @Operation(
            summary = "태그 수정",
            description = "태그를 수정합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "태그 수정 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "태그 수정 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<TagResponseDTO>> update(
            @PathVariable("id") Long id,
            @RequestBody TagCommand cmd
    ) {
        Tag tag = tagService.update(id, cmd);

        SuccessResponse<TagResponseDTO> response = SuccessResponse.<TagResponseDTO>builder()
                .data(TagResponseDTO.of(tag))
                .message("태그 수정 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "태그 삭제",
            description = "태그를 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "태그 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "태그 삭제 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<TagDeleteResponseDTO>> delete(
            @PathVariable("id") Long id
    ) {
        tagService.delete(id);

        SuccessResponse<TagDeleteResponseDTO> response = SuccessResponse.<TagDeleteResponseDTO>builder()
                .data(TagDeleteResponseDTO.of(id))
                .message("태그 삭제 성공")
                .build();

        return ResponseEntity.ok(response);
    }
}