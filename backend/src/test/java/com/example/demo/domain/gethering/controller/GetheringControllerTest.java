package com.example.demo.domain.gethering.controller;


import com.example.demo.domain.gethering.dto.command.GetheringCommand;
import com.example.demo.domain.gethering.entity.Gethering;
import com.example.demo.domain.gethering.entity.Image;
import com.example.demo.domain.gethering.service.GetheringService;
import com.example.demo.domain.sports.entity.Sports;
import com.example.demo.domain.user.entity.EnumGender;
import com.example.demo.domain.user.entity.EnumSportsCareer;
import com.example.demo.domain.user.entity.User;
import com.example.demo.global.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@WebMvcTest(GetheringController.class)
class GetheringControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetheringService getheringService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("모임 생성 성공")
    @Test
    @WithMockUser
    void register() throws Exception {

        // Given
        String requestBodyJson =
                """
                            {
                                "title": "모임 제목",
                                "description": "모임 설명",
                                "startDate": "2022-12-12",
                                "endDate": "2022-12-12",
                                "startTime": "12:00",
                                "endTime": "12:00",
                                "daysOfWeek": ["월", "화", "수", "목", "금", "토", "일"],
                                "location": "서울시 강남구",
                                "latitude": 37.123456,
                                "longitude": 127.123456,
                                "career": "초보자",
                                "maxPeople": 10
                            }
                        """;

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsInJvbGUiOiJST0xFX1VTRVIiLCJpc3MiOiJkb3duLXNlcnZlci1qd3QiLCJpZCI6MSwiZXhwIjoxNzE2NTY0OTU4LCJpYXQiOjE3MDg3ODg5NTgsInVzZXJuYW1lIjoidGVzdCJ9.5somLxNq43BKloXJmUTCn0a31hsavnzyVzVpPXk_ed8";

        User user = User.builder()
                .id(1L)
                .email("tset@gmail.com")
                .nickName("test")
                .gender("남성")
                .build();

        List<Image> images = new ArrayList<>();
        images.add(Image.builder().id(1L).originalFileName("img.jpg").saveFileName("img.jpg").url("http://localhost:8080/api/v1/image/1").build());
        images.add(Image.builder().id(2L).originalFileName("img.jpg").saveFileName("img.jpg").url("http://localhost:8080/api/v1/image/2").build());
        images.add(Image.builder().id(3L).originalFileName("img.jpg").saveFileName("img.jpg").url("http://localhost:8080/api/v1/image/3").build());

        Sports sports = Sports.builder()
                .id(1L)
                .name("축구")
                .build();

        Gethering gethering = Gethering.builder()
                .id(1L)
                .title("모임 제목")
                .description("모임 설명")
                .startDate(LocalDate.of(2022, 12, 12))
                .endDate(LocalDate.of(2022, 12, 12))
                .startTime(LocalTime.of(12, 0))
                .endTime(LocalTime.of(12, 0))
                .viewCount(100)
                .daysOfWeek(1)
                .location("서울시 강남구")
                .latitude(37.123456)
                .gender(EnumGender.MAN)
                .longitude(127.123456)
                .career(EnumSportsCareer.GOD)
                .maxPeople((short) 10)
                .user(user)
                .images(images)
                .sports(sports)
                .build();

        given(jwtTokenProvider.parseToken(any(String.class))).willReturn(token);
        given(jwtTokenProvider.getUserId(any(String.class))).willReturn(1L);
        given(getheringService.register(any(GetheringCommand.class), any(Long.class), any(Long.class), any())).willReturn(gethering);

        List<String> filePaths = new ArrayList<>();
        filePaths.add("C:\\Users\\gksxo\\Desktop\\DownProject\\backend\\src\\test\\resources\\img.jpg");
        filePaths.add("C:\\Users\\gksxo\\Desktop\\DownProject\\backend\\src\\test\\resources\\img.jpg");
        filePaths.add("C:\\Users\\gksxo\\Desktop\\DownProject\\backend\\src\\test\\resources\\img.jpg");

        List<MockMultipartFile> files = new ArrayList<>();
        byte[] content;
        for (String filePath : filePaths) {
            Path path = Paths.get(filePath);
            String fileName = path.getFileName().toString();
            String contentType = Files.probeContentType(path);
            content = Files.readAllBytes(path);
            files.add(new MockMultipartFile("files", fileName, contentType, content));
        }

        mockMvc.perform(multipart("/api/v1/gethering")
                        .file(files.get(0))
                        .file(files.get(1))
                        .file(files.get(2))
                        .param("sportsId", "1")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(requestBodyJson)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("""
                        {
                        	"success": true,
                        	"data": {
                        		"id": 1,
                        		"title": "모임 제목",
                        		"description": "모임 설명",
                        		"startDate": "2022-12-12",
                        		"endDate": "2022-12-12",
                        		"startTime": "12:00",
                        		"endTime": "12:00",
                        		"daysOfWeek": [
                        			"월"
                        		],
                        		"location": "서울시 강남구",
                        		"gender": "남자",
                        		"latitude": 37.123456,
                        		"longitude": 127.123456,
                        		"career": "신",
                        		"maxPeople": 10,
                        		"viewCount": 100,
                        		"sports": {
                        			"id": 1,
                        			"name": "축구"
                        		},
                        		"images": [
                        			{
                        				"id": 1,
                        				"url": "http://localhost:8080/api/v1/image/1"
                        			},
                        			{
                        				"id": 2,
                        				"url": "http://localhost:8080/api/v1/image/2"
                        			},
                        			{
                        				"id": 3,
                        				"url": "http://localhost:8080/api/v1/image/3"
                        			}
                        		]
                        	},
                        	"message": "모임 생성 성공"
                        }                                                       
                        """));
    }
}