package com.example.demo.global.auth.kakao;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class KakaoUserInfo {

    private Long id;
    private String birthday;
    private String email;
    private String gender;
    private String profileImgUrl;
    private String name;
    private String nickname;

    public static KakaoUserInfo of(String response) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(response);

        JsonElement kakaoAccount = element.getAsJsonObject().get("kakao_account");
        JsonElement profile = kakaoAccount.getAsJsonObject().get("profile");

        Long id = element.getAsJsonObject().get("id").getAsLong();
        String email = kakaoAccount.getAsJsonObject().get("email").getAsString();
        String name = kakaoAccount.getAsJsonObject().get("name").getAsString();
        String nickname = profile.getAsJsonObject().get("nickname").getAsString();
        String profileImgUrl = profile.getAsJsonObject().get("profile_image_url").getAsString();

        KakaoUserInfo kakaoUserInfo = KakaoUserInfo.builder()
                .id(id)
                .email(email)
                .name(name)
                .nickname(nickname)
                .profileImgUrl(profileImgUrl)
                .build();

        // 생일, 성별은 선택적으로 제공되므로 존재 여부를 확인하여 값을 설정한다.
        boolean hasBirthDay = kakaoAccount.getAsJsonObject().get("has_birthyear").getAsBoolean();
        kakaoUserInfo.setBirthday(hasBirthDay ? kakaoAccount.getAsJsonObject().get("birthyear").getAsString() : null);

        boolean hasGender = kakaoAccount.getAsJsonObject().get("has_gender").getAsBoolean();
        kakaoUserInfo.setGender(hasGender ? kakaoAccount.getAsJsonObject().get("gender").getAsString() : null);

        return kakaoUserInfo;
    }
}