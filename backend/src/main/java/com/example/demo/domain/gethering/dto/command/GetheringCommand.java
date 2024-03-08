package com.example.demo.domain.gethering.dto.command;

import com.example.demo.domain.gethering.entity.EnumDayOfWeek;
import com.example.demo.domain.gethering.entity.Gethering;
import com.example.demo.domain.gethering.entity.Tag;
import com.example.demo.domain.user.entity.EnumGender;
import com.example.demo.domain.user.entity.EnumSportsCareer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Schema(description = "모임 생성 요청")
public class GetheringCommand {

    @Getter @Setter
    @Schema(description = "제목", example = "모임 제목 입니다.")
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @Getter @Setter
    @Schema(description = "설명", example = "모임 설명 입니다.")
    @NotBlank(message = "설명을 입력해주세요.")
    private String description;

    @Setter
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "날짜 형식이 유효하지 않습니다 (yyyy-MM-dd)")
    @Schema(description = "시작일", example = "2021-01-01")
    @NotBlank(message = "시작일을 입력해주세요.")
    private String startDate;

    public LocalDate getStartDate() {
        return LocalDate.parse(startDate);
    }

    @Setter
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "날짜 형식이 유효하지 않습니다 (yyyy-MM-dd)")
    @Schema(description = "종료일", example = "2021-01-01")
    @NotBlank(message = "종료일을 입력해주세요.")
    private String endDate;

    public LocalDate getEndDate() {
        return LocalDate.parse(endDate);
    }

    @Setter
    @Pattern(regexp = "\\d{2}:\\d{2}", message = "시간 형식이 유효하지 않습니다 (HH:mm)")
    @Schema(description = "시작시간", example = "12:00")
    @NotBlank(message = "시작시간을 입력해주세요.")
    private String startTime;

    public LocalTime getStartTime() {
        return LocalTime.parse(startTime);
    }

    @Setter
    @Pattern(regexp = "\\d{2}:\\d{2}", message = "시간 형식이 유효하지 않습니다 (HH:mm)")
    @Schema(description = "종료시간", example = "12:00")
    @NotBlank(message = "종료시간을 입력해주세요.")
    private String endTime;

    public LocalTime getEndTime() {
        return LocalTime.parse(endTime);
    }

    @Setter
    @Schema(description = "요일 정보", example = "[월, 화, 수, 목, 금, 토, 일]")
    private List<String> daysOfWeek;

    public Integer getDaysOfWeek() {
        return EnumDayOfWeek.of(daysOfWeek);
    }

    @Getter @Setter
    @Schema(description = "장소", example = "모임 장소 입니다.")
    @NotBlank(message = "장소를 입력해주세요.")
    private String location;

    @Getter @Setter
    @Schema(description = "위도", example = "37.123456")
    @Digits(integer = 3, fraction = 6, message = "위도 형식이 유효하지 않습니다.")
    private double latitude;

    @Getter @Setter
    @Schema(description = "경도", example = "127.123456")
    @Digits(integer = 3, fraction = 6, message = "경도 형식이 유효하지 않습니다.")
    private double longitude;

    @Setter
    @Schema(description = "모임 요구 경력", example = "초보자")
    @NotBlank(message = "모임 요구 경력을 입력해주세요.")
    private String career;

    public EnumSportsCareer getCareer() {
        return EnumSportsCareer.of(career);
    }

    @Setter
    @Schema(description = "모임 성별", example = "1")
    @Digits(integer = 1, fraction = 0, message = "성별 형식이 유효하지 않습니다.")
    private String gender;

    public EnumGender getGender() {
        return EnumGender.of(gender);
    }

    @Getter @Setter
    @Schema(description = "모임 최대 인원", example = "100")
    @Digits(integer = 3, fraction = 0, message = "최대 인원 형식이 유효하지 않습니다.")
    private Short maxPeopleCount;

    @Getter @Setter
    @Schema(description = "태그 정보", example = "[태그 아이디]")
    private List<Long> tagIds;

    public Gethering toEntity() {
        return Gethering.builder()
                .title(title)
                .description(description)
                .startDate(getStartDate())
                .endDate(getEndDate())
                .startTime(getStartTime())
                .endTime(getEndTime())
                .location(location)
                .gender(getGender())
                .latitude(latitude)
                .longitude(longitude)
                .career(getCareer())
                .gender(getGender())
                .maxPeopleCount(maxPeopleCount)
                .currentPeopleCount((short) 0)
                .tags((List<Tag>) tagIds.stream().map(tagId -> Tag.builder().id(tagId).build()).toList())
                .build();
    }
}