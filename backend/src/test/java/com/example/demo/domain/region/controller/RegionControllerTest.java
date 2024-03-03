package com.example.demo.domain.region.controller;

import com.example.demo.domain.region.entity.SiDo;
import com.example.demo.domain.region.entity.SiGunGu;
import com.example.demo.domain.region.service.SiDoService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;


import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@WebMvcTest(RegionController.class)
class RegionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SiDoService siDoService;

    @DisplayName("시/도 목록 조회 성공")
    @Test
    @WithMockUser
    void listSiDo() throws Exception {
        // Given
        List<SiDo> siDoList = List.of(
                SiDo.builder().code((short) 11).name("서울특별시").build(),
                SiDo.builder().code((short) 22).name("부산광역시").build()
        );

        given(siDoService.listSiDo()).willReturn(siDoList);

        // When
        mockMvc.perform(get("/api/v1/regions/si-do"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                            {
                                "success": true,
                                "data": {
                                    "siDoList": [
                                        {
                                            "code": 11,
                                            "name": "서울특별시"
                                        },
                                        {
                                            "code": 22,
                                            "name": "부산광역시"
                                        }
                                    ]
                                },
                                "message": "시/도 조회 성공"
                            }
                        """));
    }

    @DisplayName("시/군/구 목록 조회 성공")
    @Test
    @WithMockUser
    void listSiGunGu() throws Exception {
        // Given
        SiDo siDo = SiDo.builder().code((short) 11).name("서울특별시").build();

        List<SiGunGu> siGunGuList = List.of(
                SiGunGu.builder().code((short) 110).name("종로구").siDo(siDo).legalAddressCode(1111000000L).build(),
                SiGunGu.builder().code((short) 140).name("중구").siDo(siDo).legalAddressCode(1114000000L).build()
        );

        given(siDoService.listSiGunGu((short) 11)).willReturn(siGunGuList);

        // When
        mockMvc.perform(get("/api/v1/regions/si-do/11/si-gun-gu"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                {
                                    "success": true,
                                    "data": {
                                        "siGunGuList": [
                                            {
                                                "code": 110,
                                                "name": "종로구",
                                                "legalAddressCode": 1111000000
                                            },
                                            {
                                                "code": 140,
                                                "name": "중구",
                                                "legalAddressCode": 1114000000
                                            }
                                        ]
                                    },
                                    "message": "시/군/구 조회 성공"
                                }
                        """));
    }
}