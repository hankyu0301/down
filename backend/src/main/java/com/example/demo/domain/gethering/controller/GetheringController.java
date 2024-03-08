package com.example.demo.domain.gethering.controller;


import com.example.demo.domain.gethering.dto.response.GetheringBoardListResponseDTO;
import com.example.demo.domain.gethering.dto.command.GetheringCommand;
import com.example.demo.domain.gethering.dto.response.GetheringDetailResponseDTO;
import com.example.demo.domain.gethering.dto.response.GetheringResponseDTO;
import com.example.demo.domain.gethering.entity.Gethering;
import com.example.demo.domain.gethering.service.GetheringService;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@Tag(
        name = "모임",
        description = "모임 카테고리 CRUD API"
)
@Slf4j
@RestController
@RequestMapping("${api.version-path}/gethering")
@RequiredArgsConstructor
public class GetheringController {

    private final GetheringService getheringService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(
            summary = "모임 생성",
            description = "모임을 생성합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "모임 생성 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "모임 생성 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PostMapping("/sports/{sportsId}/users/{userId}")
    public ResponseEntity<SuccessResponse<GetheringResponseDTO>> register(
            @RequestBody GetheringCommand cmd,
            @PathVariable Long sportsId,
            @PathVariable Long userId,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestHeader("Authorization") String authorization
    ) {
        String token = jwtTokenProvider.parseToken(authorization);
        Long tokenUserId = jwtTokenProvider.getUserId(token);

        Gethering gethering = getheringService.register(cmd, tokenUserId, sportsId, images);

        SuccessResponse<GetheringResponseDTO> response = SuccessResponse.<GetheringResponseDTO>builder()
                .data(GetheringResponseDTO.of(gethering))
                .message("모임 생성 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    // 모임 목록 조회
    @Operation(
            summary = "모임 목록 조회",
            description = "모임 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "모임 목록 조회 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "모임 목록 조회 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<SuccessResponse<Page<GetheringBoardListResponseDTO>>> list(
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,

            @Parameter(description = "페이지 사이즈", example = "10")
            @RequestParam(value = "size", defaultValue = "10") int size,

            @Parameter(description = "지역 법정동 코드", example = "1168010600")
            @RequestParam(value = "location", required = false) String location,

            @Parameter(description = "위도", example = "37.123456")
            @RequestParam(value = "latitude", required = true) Double latitude,

            @Parameter(description = "경도", example = "127.123456")
            @RequestParam(value = "longitude", required = true) Double longitude,

            @Parameter(description = "운동 카테고리 ID", example = "1")
            @RequestParam(value = "sportsId", required = false) Long sportsId,

            @Parameter(description = "모임 제목", example = "모임 제목")
            @RequestParam(value = "career", required = false) String career,

            @Parameter(description = "모임 성별", example = "남자, 여자, 혼성")
            @RequestParam(value = "gedner", required = false) String gender,

            @Parameter(description = "모임 최소 나이", example = "20")
            @RequestParam(value = "age", required = false) String age,

            @Parameter(description = "모임 최대 인원", example = "10")
            @RequestParam(value = "maxPeople", required = false) Short maxPeople
    ) {
        Page<GetheringBoardListResponseDTO> getheringList = getheringService
                .list(page, size, location, latitude, longitude, sportsId, career, gender, age, maxPeople)
                .map(gethering -> GetheringBoardListResponseDTO.of(gethering, latitude, longitude));

        SuccessResponse<Page<GetheringBoardListResponseDTO>> response = SuccessResponse.<Page<GetheringBoardListResponseDTO>>builder()
                .data(getheringList)
                .message("모임 목록 조회 성공")
                .build();

        return ResponseEntity.ok(response);
    }


    // 모임 상세 페이지 조회
    @Operation(
            summary = "모임 상세 조회",
            description = "모임 상세를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "모임 상세 조회 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "모임 상세 조회 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @GetMapping("/{getheringId}")
    public ResponseEntity<SuccessResponse<GetheringDetailResponseDTO>> detail(
            @Parameter(description = "모임 ID", example = "1")
            @PathVariable Long getheringId,
            @Parameter(description = "위도", example = "37.123456")
            @RequestParam(value = "latitude", required = true) Double latitude,
            @Parameter(description = "경도", example = "127.123456")
            @RequestParam(value = "longitude", required = true) Double longitude
    ) {
        Gethering gethering = getheringService.detail(getheringId);

        SuccessResponse<GetheringDetailResponseDTO> response = SuccessResponse.<GetheringDetailResponseDTO>builder()
                .data(GetheringDetailResponseDTO.of(gethering, latitude, longitude))
                .message("모임 상세 조회 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    // 모임 수정
    @Operation(
            summary = "모임 수정",
            description = "모임을 수정합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "모임 수정 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "모임 수정 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @PutMapping("/{getheringId}/users/{userId}")
    public ResponseEntity<SuccessResponse<GetheringResponseDTO>> patch(
            @RequestBody GetheringCommand cmd,
            @PathVariable Long getheringId,
            @PathVariable Long userId,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestHeader("Authorization") String authorization
    ) {
        String token = jwtTokenProvider.parseToken(authorization);
        Long tokenUserId = jwtTokenProvider.getUserId(token);

        Gethering gethering = getheringService.update(cmd, getheringId, tokenUserId, images);

        SuccessResponse<GetheringResponseDTO> response = SuccessResponse.<GetheringResponseDTO>builder()
                .data(GetheringResponseDTO.of(gethering))
                .message("모임 수정 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    // 모임 삭제
}