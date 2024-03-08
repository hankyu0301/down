package com.example.demo.domain.gethering.service;


import com.example.demo.domain.gethering.dto.command.GetheringCommand;
import com.example.demo.domain.gethering.entity.Gethering;
import com.example.demo.domain.gethering.entity.Image;
import com.example.demo.domain.gethering.repository.GetheringPagingAndSortingRepository;
import com.example.demo.domain.gethering.repository.GetheringRepository;
import com.example.demo.domain.gethering.repository.ImageRepository;
import com.example.demo.domain.sports.entity.Sports;
import com.example.demo.domain.sports.repository.SportsRepository;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.domain.util.ImageUpload;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ExceptionCode;
import io.jsonwebtoken.lang.Assert;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GetheringService {

    private final UserRepository userRepository;
    private final SportsRepository sportsRepository;
    private final GetheringRepository getheringRepository;
    private final GetheringPagingAndSortingRepository getheringPagingAndSortingRepository;

    private final ImageUpload imageUpload;
    private final ImageRepository imageRepository;

    public Gethering register(GetheringCommand cmd, Long userId, Long sportsId, MultipartFile[] images) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_USER));

        Sports sports = sportsRepository.findById(sportsId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_SPORTS));

        Gethering entity = cmd.toEntity();
        entity.setUser(user);
        entity.setSports(sports);
        entity = getheringRepository.save(entity);

        if (images != null) {
            List<Image> imageList = uploadImages(entity, images);
            entity.setImages(imageList);
            entity = getheringRepository.save(entity);
        }

        return entity;
    }

    private List<Image> uploadImages(Gethering gethering, MultipartFile[] images) {
        Assert.notNull(gethering, "gethering must not be null");
        Assert.notNull(images, "images must not be null");

        List<Image> imageList = new ArrayList<>();
        for (MultipartFile image : images) {
            String saveFileName = imageUpload.saveImage(gethering.getId(), image);

            Image entity = Image.builder()
                    .gethering(gethering)
                    .originalFileName(image.getOriginalFilename())
                    .saveFileName(saveFileName)
                    .build();

            imageList.add(imageRepository.save(entity));
        }

        return imageList;
    }

    public Page<Gethering> list(int page, int size, String location, Double latitude, Double longitude, Long sportsId, String career, String gender, String age, Short maxPeople) {
        // 필터 생성해야함

        return getheringPagingAndSortingRepository.findAll(PageRequest.of(page, size));
    }

    public Gethering detail(Long getheringId) {
        return getheringRepository.findById(getheringId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_GETHERING));
    }

    public Gethering update(GetheringCommand cmd, Long getheringId, Long tokenUserId, MultipartFile[] images) {

        Gethering gethering = getheringRepository.findById(getheringId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_EXIST_GETHERING));

        if (!gethering.getUser().getId().equals(tokenUserId)) {
            throw new CustomException(ExceptionCode.NOT_MATCH_USER);
        }

        // 제목 수정
        gethering.setTitle(cmd.getTitle());
        // 설명 수정
        gethering.setDescription(cmd.getDescription());
        // 시작일 수정
        gethering.setStartDate(cmd.getStartDate());
        // 종료일 수정
        gethering.setEndDate(cmd.getEndDate());
        // 시작시간 수정
        gethering.setStartTime(cmd.getStartTime());
        // 종료시간 수정
        gethering.setEndTime(cmd.getEndTime());
        // 요일 수정
        gethering.setDaysOfWeek(cmd.getDaysOfWeek());
        // 장소 수정
        gethering.setLocation(cmd.getLocation());

        // 법정동 코드 수정
        gethering.setLegalAddressCode(""); // API 연동
        gethering.setMapLocation(""); // API 연동
        gethering.setMapAddress("");  // API 연동

        // 위도 수정
        gethering.setLatitude(cmd.getLatitude());
        // 경도 수정
        gethering.setLongitude(cmd.getLongitude());
        // 모임 요구 경력 수정
        gethering.setCareer(cmd.getCareer());
        // 모임 성별 수정
        gethering.setGender(cmd.getGender());
        // 모임 최대 인원 수정
        gethering.setMaxPeopleCount(cmd.getMaxPeopleCount());
        // 태그 수정
        gethering.setTags(null); // 여기 구현
        // 이미지 수정
        gethering.setImages(null); // 여기 구현

        gethering = getheringRepository.save(gethering);

        if (images != null) {
            List<Image> imageList = uploadImages(gethering, images);
            gethering.setImages(imageList);
            gethering = getheringRepository.save(gethering);
        }

        return gethering;
    }
}