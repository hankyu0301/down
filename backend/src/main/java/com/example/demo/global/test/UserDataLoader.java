package com.example.demo.global.test;

import com.example.demo.domain.sports.entity.Sports;
import com.example.demo.domain.sports.repository.SportsRepository;
import com.example.demo.domain.user.entity.EnumSportsCareer;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.entity.EnumUserRole;
import com.example.demo.domain.user.entity.UserSportsInfo;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.domain.user.repository.UserSportsInfoRepository;
import com.example.demo.global.auth.EnumLoginType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SportsRepository sportsRepository;
    private final UserSportsInfoRepository userSportsInfoRepository;

    @Override
    public void run(String... args) {
        for (long i = 1; i <= 10; i++) {
            userRepository.save(newUserData(i));
            sportsRepository.save(newSportsData(i));
            userSportsInfoRepository.save(newUserSportsInfoData(i));
        }
    }

    private User newUserData(Long id) {
        return User.builder()
                .id(id)
                .email("test" + id + "@test.com")
                .password("test")
                .nickName("test" + id)
                .userName("test" + id)
                .birth("1990-01-01")
                .gender("male")
                .provider(EnumLoginType.SERVICE)
                .role(EnumUserRole.ROLE_USER)
                .termsAgree(true)
                .providerId(id)
                .build();
    }

    private Sports newSportsData(Long id) {
        return Sports.builder()
                .id(id)
                .name("운동" + id)
                .build();
    }

    private UserSportsInfo newUserSportsInfoData(Long id) {
        return UserSportsInfo.builder()
                .id(id)
                .user(newUserData(id))
                .sports(newSportsData(id))
                .career(EnumSportsCareer.BEGINNER)
                .build();
    }
}