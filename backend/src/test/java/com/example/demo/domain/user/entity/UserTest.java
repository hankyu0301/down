package com.example.demo.domain.user.entity;


import com.example.demo.domain.sports.entity.Sports;
import com.example.demo.global.auth.LoginEnumType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @DisplayName("사용자 운동 정보 저장 테스트")
    @Test
    void saveUserSportsInfo() {
        // Given
        String email = "test@example.com";
        String password = "password";
        String nickName = "test";
        String userName = "Test User";
        LoginEnumType provider = LoginEnumType.SERVICE;
        String gender = "M";
        String birth = "1990-01-01";
        Long providerId = 1L;
        UserRoleEnumType role = UserRoleEnumType.ROLE_USER;
        Boolean termsAgree = true;

        Sports sports = Sports.builder()
                .name("운동")
                .sportsUserInfos(List.of())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User user = User.builder()
                .email(email)
                .password(password)
                .nickName(nickName)
                .userName(userName)
                .provider(provider)
                .gender(gender)
                .birth(birth)
                .providerId(providerId)
                .role(role)
                .userSportsInfoList(List.of())
                .termsAgree(termsAgree)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        UserSportsInfo userSportsInfo = UserSportsInfo.builder()
                .user(user)
                .sports(sports)
                .career(SportsCareer.GOD)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // When
        User savedUser = testEntityManager.persistAndFlush(user);
    }
}