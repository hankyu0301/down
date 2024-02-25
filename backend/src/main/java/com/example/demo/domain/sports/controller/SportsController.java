package com.example.demo.domain.sports.controller;

import com.example.demo.domain.sports.dto.command.SportsCommand;
import com.example.demo.domain.sports.dto.response.SportsDeleteResponseDTO;
import com.example.demo.domain.sports.dto.response.SportsResponseDTO;
import com.example.demo.domain.sports.dto.response.SportsInfoDTO;
import com.example.demo.domain.sports.entity.Sports;
import com.example.demo.domain.sports.service.SportsService;
import com.example.demo.domain.util.FailResponse;
import com.example.demo.domain.util.SuccessResponse;
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
        name = "운동",
        description = "운동 카테고리 CRUD API"
)
@Slf4j
@RestController
@RequestMapping("${api.version-path}/sports")
@RequiredArgsConstructor
public class SportsController {

    private final SportsService sportsService;

    @Operation(
            summary = "운동 생성",
            description = "운동을 생성합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "운동 생성 요청",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = SportsCommand.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "운동 생성 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "운동 생성 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<SuccessResponse<SportsResponseDTO>> create(
            @RequestBody @Valid SportsCommand cmd
    ){
        Sports sports = sportsService.registerSports(cmd);

        SuccessResponse<SportsResponseDTO> response = SuccessResponse.<SportsResponseDTO>builder()
                .data(SportsResponseDTO.of(sports))
                .message("운동 생성 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "운동 정보 목록 조회",
            description = "운동들 정보를 조회합니다.",
            tags = "운동"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "운동 정보 목록 조회 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "운동 정보 목록 조회 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<SuccessResponse<Page<SportsResponseDTO>>> list(
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        Page<SportsResponseDTO> sportsList = sportsService.sportsList(page, size)
                .map(SportsResponseDTO::of);

        SuccessResponse<Page<SportsResponseDTO>> response = SuccessResponse.<Page<SportsResponseDTO>>builder()
                .data(sportsList)
                .message("운동 정보 목록 조회 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "운동 상세 조회",
            description = "운동 상세 정보를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "운동 상세 조회 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "운동 상세 조회 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<SportsInfoDTO>> get(
            @Parameter(description = "운동 ID", example = "1")
            @PathVariable Long id
    ) {
        Sports sports = sportsService.findById(id);

        SuccessResponse<SportsInfoDTO> response = SuccessResponse.<SportsInfoDTO>builder()
                .data(SportsInfoDTO.of(sports))
                .message("운동 상세 조회 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "운동 수정",
            description = "운동을 수정합니다."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "운동 수정 요청",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = SportsCommand.class)
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "운동 수정 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "운동 수정 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<SportsInfoDTO>> update(
            @PathVariable Long id,
            @RequestBody @Valid SportsCommand cmd
    ){
        Sports sports = sportsService.updateSports(id, cmd);

        SuccessResponse<SportsInfoDTO> response = SuccessResponse.<SportsInfoDTO>builder()
                .data(SportsInfoDTO.of(sports))
                .message("운동 수정 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "운동 삭제",
            description = "운동을 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "운동 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "운동 삭제 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<SportsDeleteResponseDTO>> delete(
            @PathVariable Long id
    ){
        sportsService.deleteById(id);

        SuccessResponse<SportsDeleteResponseDTO> response = SuccessResponse.<SportsDeleteResponseDTO>builder()
                .data(SportsDeleteResponseDTO.of(id))
                .message("운동 삭제 성공")
                .build();

        return ResponseEntity.ok(response);
    }
}