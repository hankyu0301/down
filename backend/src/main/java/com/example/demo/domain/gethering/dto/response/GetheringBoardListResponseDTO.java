package com.example.demo.domain.gethering.dto.response;

import com.example.demo.domain.gethering.entity.Gethering;
import com.example.demo.domain.gethering.entity.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "모임 리스트 응답")
public class GetheringBoardListResponseDTO {

    @Schema(description = "모임 ID", example = "1")
    private Long id;

    @Schema(description = "대표 이미지", example = "https://image.com")
    private String imageUrl;

    @Schema(description = "모임 제목", example = "모임 제목 입니다.")
    private String title;

    @Schema(description = "모임 지역", example = "서울시 강남구")
    private String location;

    @Schema(description = "현 위치에서의 거리", example = "0.3Km")
    private String distance;

    @Schema(description = "현재 인원", example = "10")
    private Short currentPeopleCount;

    @Schema(description = "조회수", example = "100")
    private Integer viewCount;

    @Schema(description = "모임 태그", example = "축구, 농구")
    private List<String> tags;

    public static GetheringBoardListResponseDTO of(Gethering gethering, double latitude, double longitude) {
        return GetheringBoardListResponseDTO.builder()
                .id(gethering.getId())
                .imageUrl(gethering.getImages().get(0).getUrl())
                .title(gethering.getTitle())
                .location(gethering.getLocation())
                .distance(gethering.getDistance(latitude, longitude))
                .currentPeopleCount(gethering.getCurrentPeopleCount())
                .viewCount(gethering.getViewCount())
                .tags(gethering.getTags().stream().map(Tag::getName).toList())
                .build();
    }
}