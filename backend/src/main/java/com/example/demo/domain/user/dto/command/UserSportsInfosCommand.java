package com.example.demo.domain.user.dto.command;

import com.example.demo.domain.sports.entity.Sports;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.entity.UserSportsInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


import java.util.List;
import java.util.stream.Collectors;

@Data
@Schema(description = "사용자의 운동 경력 정보 입력")
public class UserSportsInfosCommand {

    @Schema(description = "운동 경력 데이터 목록")
    List<UserSportsInfoCommand> userSportsInfos;

    public List<UserSportsInfo> toEntity(User user) {
        return userSportsInfos.stream()
                .map(info -> UserSportsInfo.builder()
                        .user(user)
                        .sports(Sports.builder().id(info.getSportsId()).build())
                        .career(info.getCareer())
                        .build())
                .collect(Collectors.toList());
    }
}