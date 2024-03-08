package com.example.demo.domain.gethering.dto.response;

import com.example.demo.domain.gethering.entity.EnumDayOfWeek;
import com.example.demo.domain.gethering.entity.Gethering;
import com.example.demo.domain.gethering.entity.Image;
import com.example.demo.domain.sports.entity.Sports;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetheringResponseDTO {

    @Schema(description = "모임 ID", example = "1")
    private Long id;

    @Schema(description = "모임 제목", example = "모임 제목 입니다.")
    private String title;

    @Schema(description = "모임 설명", example = "모임 설명 입니다.")
    private String description;

    @Schema(description = "시작일", example = "2021-01-01")
    private String startDate;

    @Schema(description = "종료일", example = "2021-01-01")
    private String endDate;

    @Schema(description = "시작시간", example = "12:00")
    private String startTime;

    @Schema(description = "종료시간", example = "12:00")
    private String endTime;

    @Schema(description = "요일 정보", example = "[월, 화, 수, 목, 금, 토, 일]")
    private List<String> daysOfWeek;

    @Schema(description = "모임 장소", example = "서울시 강남구")
    private String location;

    @Schema(description = "모임 성별", example = "남자, 여자, 혼성")
    private String gender;

    @Schema(description = "모임 장소 위도", example = "37.123456")
    private Double latitude;

    @Schema(description = "모임 장소 경도", example = "127.123456")
    private Double longitude;

    @Schema(description = "모임 최소 경력 요구", example = "초보자")
    private String career;

    @Schema(maximum = "100", description = "모임 최대 인원", example = "10")
    private Short maxPeopleCount;

    @Schema(description = "모임 현재 인원", example = "10")
    private Short currentPeopleCount;

    @Schema(description = "모임 조회수", example = "100")
    private Integer viewCount;

    @Schema(description = "운동 카테고리", example = "축구")
    private SportsDTO sports;

    @Schema(description = "모임 이미지 목록", example = "모임 이미지 목록 입니다.")
    private List<ImageDTO> images;

    public static GetheringResponseDTO of(Gethering entity) {
        return GetheringResponseDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .startDate(entity.getStartDate().toString())
                .endDate(entity.getEndDate().toString())
                .startTime(entity.getStartTime().toString())
                .endTime(entity.getEndTime().toString())
                .location(entity.getLocation())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .viewCount(entity.getViewCount())
                .gender(entity.getGender().getLabel())
                .career(entity.getCareer().getCareer())
                .maxPeopleCount(entity.getMaxPeopleCount())
                .currentPeopleCount(entity.getCurrentPeopleCount())
                .sports(SportsDTO.of(entity.getSports()))
                .images(ImageDTO.of(entity.getImages()))
                .daysOfWeek(EnumDayOfWeek.of(entity.getDaysOfWeek()))
                .build();
    }

    @Data
    @Builder
    private static class SportsDTO {
        @Schema(description = "운동 ID", example = "1")
        private Long id;
        @Schema(description = "운동 이름", example = "축구")
        private String name;
        public static SportsDTO of(Sports entity) {
            return SportsDTO.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .build();
        }
    }

    @Data
    @Builder
    private static class ImageDTO {
        @Schema(description = "이미지 ID", example = "1")
        private Long id;
        @Schema(description = "이미지 URL", example = "http://localhost:8080/이미지 경로")
        private String url;
        public static List<ImageDTO> of(List<Image> entities) {
            return entities.stream()
                    .map(entity -> ImageDTO.builder()
                            .id(entity.getId())
                            .url(entity.getUrl())
                            .build())
                    .toList();
        }
    }
}