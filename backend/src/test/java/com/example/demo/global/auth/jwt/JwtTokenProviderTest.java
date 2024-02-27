package com.example.demo.global.auth.jwt;

import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.entity.UserRoleEnumType;
import com.example.demo.global.auth.LoginEnumType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class JwtTokenProviderTest {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.access-token-period}")
    private long ACCESS_TOKEN_PERIOD;

    @Value("${jwt.token-provider}")
    private String TOKEN_PROVIDER;

    @Test
    void getEmail() {

        // given
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsInJvbGUiOiJST0xFX1VTRVIiLCJpc3MiOiJkb3duLXNlcnZlci1qd3QiLCJpZCI6MSwiZXhwIjoxNzE2NTY0OTU4LCJpYXQiOjE3MDg3ODg5NTgsInVzZXJuYW1lIjoidGVzdCJ9.5somLxNq43BKloXJmUTCn0a31hsavnzyVzVpPXk_ed8";
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        System.out.println(claims.get("sub"));
    }

    @Test
    void generateJwtToken() {

        // given
        User user = User.builder()
                .id(1L)
                .email("test@gmail.com")
                .password("test1234")
                .nickName("test")
                .userName("test")
                .provider(LoginEnumType.SERVICE)
                .gender("test")
                .birth("test")
                .providerId(123L)
                .role(UserRoleEnumType.ROLE_USER)
                .termsAgree(true)
                .build();

        Map<String, Object> payloads = new HashMap<>();

        // Subject (sub): 토큰의 주체를 식별하는 정보입니다.
        payloads.put("sub", user.getEmail());

        // Issuer (iss): 토큰을 발급한 발급자를 식별하는 정보입니다.
        payloads.put("iss", TOKEN_PROVIDER);

        // Role (role): 사용자의 역할을 나타내는 정보입니다.
        // 사용자가 어떤 권한을 가지고 있는지를 나타낼 수 있습니다.
        payloads.put("role", UserRoleEnumType.ROLE_USER.name());
        payloads.put("username", user.getUserName());
        payloads.put("id", user.getId());

        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(payloads)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_PERIOD))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        System.out.println(token);
    }
}