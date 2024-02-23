package com.example.demo.global.auth.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;


class KaKaoUserInfoTest {

    @Test
    public void testJsonMapping() throws JsonProcessingException {
        String json = """
                    {
                    	"id": 3356490999,
                    	"connected_at": "2024-02-22T03:19:44Z",
                    	"properties": {
                    		"nickname": "한태규",
                    		"profile_image": "http://k.kakaocdn.net/dn/1G9kp/btsAot8liOn/8CWudi3uy07rvFNUkk3ER0/img_640x640.jpg",
                    		"thumbnail_image": "http://k.kakaocdn.net/dn/1G9kp/btsAot8liOn/8CWudi3uy07rvFNUkk3ER0/img_110x110.jpg"
                    	},
                    	"kakao_account": {
                    		"profile_nickname_needs_agreement": false,
                    		"profile_image_needs_agreement": false,
                    		"profile": {
                    			"nickname": "한태규",
                    			"thumbnail_image_url": "http://k.kakaocdn.net/dn/1G9kp/btsAot8liOn/8CWudi3uy07rvFNUkk3ER0/img_110x110.jpg",
                    			"profile_image_url": "http://k.kakaocdn.net/dn/1G9kp/btsAot8liOn/8CWudi3uy07rvFNUkk3ER0/img_640x640.jpg",
                    			"is_default_image": true
                    		},
                    		"name_needs_agreement": false,
                    		"name": "한태규",
                    		"has_email": true,
                    		"email_needs_agreement": false,
                    		"is_email_valid": true,
                    		"is_email_verified": true,
                    		"email": "gksxorb147@naver.com",
                    		"has_age_range": true,
                    		"age_range_needs_agreement": false,
                    		"age_range": "20~29",
                    		"has_birthyear": true,
                    		"birthyear_needs_agreement": false,
                    		"birthyear": "1998",
                    		"has_birthday": true,
                    		"birthday_needs_agreement": false,
                    		"birthday": "0407",
                    		"birthday_type": "SOLAR",
                    		"has_gender": true,
                    		"gender_needs_agreement": false,
                    		"gender": "male"
                    	}
                    }
                """;

        // when
        KakaoUserInfo data = KakaoUserInfo.of(json);
        System.out.println(data);
    }
}