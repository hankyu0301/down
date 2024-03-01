package com.example.demo.domain.user.dto.command;


import com.example.demo.domain.user.entity.SportsCareer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "사용자의 운동 경력 정보 입력")
public class UserSportsInfoCommand {

    @NotNull(message = "운동 종목 ID는 필수 입력 값입니다.")
    @Schema(description = "운동 종목 ID")
    private Long sportsId;

    @NotNull(message = "운동 경력은 필수 입력 값입니다.")
    @Schema(description = "운동 경력")
    private SportsCareer career;
}