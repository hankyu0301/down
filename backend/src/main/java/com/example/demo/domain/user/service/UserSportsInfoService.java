package com.example.demo.domain.user.service;


import com.example.demo.domain.user.dto.command.UserSportsInfoCommand;
import com.example.demo.domain.user.dto.command.UserSportsInfosCommand;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.entity.UserSportsInfo;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.domain.user.repository.UserSportsInfoRepository;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ExceptionCode;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class UserSportsInfoService {

    private final UserRepository userRepository;
    private final UserSportsInfoRepository userSportsInfoRepository;

    public List<UserSportsInfo> register(Long id, UserSportsInfosCommand cmd, String email) {
        Assert.notNull(id, "id must not be null");
        Assert.notNull(cmd, "cmd must not be null");
        Assert.notNull(email, "email must not be null");

        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_USER));

        if (!user.getEmail().equals(email)) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED_USER);
        }

        List<UserSportsInfo> userSportsInfos = cmd.toEntity(user);

        return userSportsInfoRepository.saveAll(userSportsInfos);
    }

    public List<UserSportsInfo> list(Long id) {
        Assert.notNull(id, "id must not be null");
        return userSportsInfoRepository.findByUserId(id);
    }

    public UserSportsInfo update(Long userId, Long sportsId, UserSportsInfoCommand cmd) {

        UserSportsInfo userSportsInfos = userSportsInfoRepository.findByUserIdAndSportsId(userId, sportsId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_USER_SPORTS_INFO));

        userSportsInfos.setCareer(cmd.getCareer());

        return userSportsInfoRepository.save(userSportsInfos);
    }

    public void delete(Long userId, Long sportsId) {
        UserSportsInfo userSportsInfos = userSportsInfoRepository.findByUserIdAndSportsId(userId, sportsId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_USER_SPORTS_INFO));

        userSportsInfoRepository.delete(userSportsInfos);
    }
}
