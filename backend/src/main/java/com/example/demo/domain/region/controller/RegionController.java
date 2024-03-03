package com.example.demo.domain.region.controller;


import com.example.demo.domain.region.dto.response.SiDosResponseDTO;
import com.example.demo.domain.region.dto.response.SiGunGusResponseDTO;
import com.example.demo.domain.region.entity.SiDo;
import com.example.demo.domain.region.entity.SiGunGu;
import com.example.demo.domain.region.service.SiDoService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "지역 정보",
        description = "지역 정보를 조회하는 API"
)
@Slf4j
@RestController
@RequestMapping("${api.version-path}/regions")
@RequiredArgsConstructor
public class RegionController {

    private final SiDoService siDoService;

    @Operation(
            summary = "시/도 조회",
            description = "시/도를 조회하는 API"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "시/도 조회 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "시/도 조회 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @GetMapping("/si-do")
    public ResponseEntity<SuccessResponse<SiDosResponseDTO>> listSiDo() {
        List<SiDo> siDoList = siDoService.listSiDo();

        SuccessResponse<SiDosResponseDTO> response = SuccessResponse.<SiDosResponseDTO>builder()
                .data(SiDosResponseDTO.of(siDoList))
                .message("시/도 조회 성공")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "시/군/구 조회",
            description = "시/군/구를 조회하는 API"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "시/군/구 조회 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "시/군/구 조회 실패",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FailResponse.class)
                    )
            )
    })
    @GetMapping("/si-do/{siDoId}/si-gun-gu")
    public ResponseEntity<SuccessResponse<SiGunGusResponseDTO>> listSiGunGu(
            @Parameter(description = "시/도 ID", example = "11")
            @PathVariable Short siDoId
    ) {
        List<SiGunGu> siGunGuList = siDoService.listSiGunGu(siDoId);

        SuccessResponse<SiGunGusResponseDTO> response = SuccessResponse.<SiGunGusResponseDTO>builder()
                .data(SiGunGusResponseDTO.of(siGunGuList))
                .message("시/군/구 조회 성공")
                .build();

        return ResponseEntity.ok(response);
    }
}