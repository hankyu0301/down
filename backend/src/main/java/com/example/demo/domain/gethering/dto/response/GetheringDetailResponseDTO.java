package com.example.demo.domain.gethering.dto.response;

import com.example.demo.domain.gethering.entity.EnumDayOfWeek;
import com.example.demo.domain.gethering.entity.Gethering;
import com.example.demo.domain.gethering.entity.Image;
import com.example.demo.domain.gethering.entity.Tag;
import com.example.demo.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@Schema(description = "모임 상세 조회 응답")
public class GetheringDetailResponseDTO {

    // ID
    @Schema(description = "모임 ID", example = "1")
    private Long id;

    // 시/군/구 이름
    @Schema(description = "시/군/구", example = "서울특별시 강남구")
    private String location;

    // 위치 이름
    @Schema(description = "위치 이름", example = "뚝섬한강공원")
    private String locationName;

    // 제목
    @Schema(description = "모임 제목", example = "뚝섬한강공원에서 운동하실 분")
    private String title;

    // 모임 설명
    @Schema(description = "모임 설명", example = "뚝섬한강공원에서 운동하실 분을 모집합니다.")
    private String description;

    // 태그
    @Schema(description = "태그", example = "태그명")
    private List<String> tags;

    // 최대 현재 모임 참여 인원
    @Schema(description = "최대 현재 모임 참여 인원", example = "5")
    private String currentMaxMember;

    // 조회수
    @Schema(description = "조회수", example = "5")
    private Integer viewCount;

    // 요일
    @Schema(description = "요일", example = "[월, 화, 수, 목, 금, 토, 일]")
    private List<String> days;

    // 시작 시각
    @Schema(description = "시작 시각", example = "00:00")
    private LocalTime startTime;

    // 종료 시각
    @Schema(description = "종료 시각", example = "00:00")
    private LocalTime endTime;

    // 상세 주소
    @Schema(description = "상세 주소", example = "서울특별시 강남구 뚝섬로 1")
    private String address;

    // 현 위치의 거리
    @Schema(description = "현 위치의 거리", example = "1.2km")
    private String distance;

    // 모임 이미지 URL들
    @Schema(description = "모임 이미지 URL들", example = "https://www.naver.com/1.jpg, https://www.naver.com/2.jpg")
    private List<String> images;

    // 사용자 정보
    @Schema(description = "사용자 정보", example = "사용자 정보")
    private UserResponseDTO user;

    // 운동 카테고리 이름
    @Schema(description = "운동 카테고리 이름", example = "운동 카테고리 이름")
    private String sportsName;

    // 모임 시작일
    @Schema(description = "모임 시작일", example = "2021-01-01")
    private LocalDate startDate;

    // 모임 최대 인원
    @Schema(description = "모임 최대 인원", example = "10")
    private Short maxPeopleCount;

    // 모임 최소 경력
    @Schema(description = "모임 최소 경력", example = "초보자")
    private String career;

    // 모임 성별
    @Schema(description = "모임 성별", example = "남자, 여자, 혼성")
    private String gender;

    // 모임 연령대
    @Schema(description = "모임 연령대", example = "20대, 30대, 40대")
    private Short age;

    // 찜 여부
    @Schema(description = "찜 여부", example = "true")
    private boolean isLike;

    @AllArgsConstructor(staticName = "of")
    private static class UserResponseDTO {
        @Schema(description = "사용자 ID", example = "1")
        private Long id;

        @Schema(description = "사용자 닉네임", example = "사용자 닉네임")
        private String nickname;

        public static UserResponseDTO of(User user) {
            return UserResponseDTO.of(user.getId(), user.getNickName());
        }

    }

    public static GetheringDetailResponseDTO of(Gethering gethering, Double latitude, Double longitude) {
        return GetheringDetailResponseDTO.builder()
                .id(gethering.getId())
                .location(gethering.getLocation())
                .locationName(gethering.getMapLocation())
                .title(gethering.getTitle())
                .description(gethering.getTitle())
                .tags(gethering.getTags().stream().map(Tag::getName).toList())
                .currentMaxMember(gethering.getTitle())
                .viewCount(gethering.getViewCount())
                .days(EnumDayOfWeek.of(gethering.getDaysOfWeek()))
                .startTime(gethering.getStartTime())
                .endTime(gethering.getEndTime())
                .address(gethering.getMapAddress())
                .distance(gethering.getDistance(latitude, longitude))
                .images(gethering.getImages().stream().map(Image::getUrl).toList())
                .user(UserResponseDTO.of(gethering.getUser()))
                .sportsName(gethering.getSports().getName())
                .startDate(gethering.getStartDate())
                .maxPeopleCount(gethering.getMaxPeopleCount())
                .career(gethering.getCareer().getCareer())
                .gender(gethering.getGender().getLabel())
                .age(gethering.getAge().getLabel())
                .isLike(true) // 찜 기능 구현 해야함
                .build();
    }
}