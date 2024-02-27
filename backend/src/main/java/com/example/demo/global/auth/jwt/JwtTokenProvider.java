package com.example.demo.global.auth.jwt;


import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.entity.UserRoleEnumType;
import com.example.demo.global.auth.PrincipalDetails;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ExceptionCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.token-provider}")
    private String TOKEN_PROVIDER;

    @Value("${jwt.refresh-token-period}")
    private long REFRESH_TOKEN_PERIOD;

    @Value("${jwt.access-token-period}")
    private long ACCESS_TOKEN_PERIOD;

    private Claims getClaimsFormToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateJwtToken(User user, Long period) {

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
        return Jwts.builder()
                .setClaims(payloads)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + period))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generateRefreshToken(User user) {
        return generateJwtToken(user, REFRESH_TOKEN_PERIOD);
    }

    public String generateAccessToken(User user) {
        return generateJwtToken(user, ACCESS_TOKEN_PERIOD);
    }

    public String generateAccessToken(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return generateAccessToken(principalDetails.toEntity());
    }

    public String generateRefreshToken(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return generateRefreshToken(principalDetails.toEntity());
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFormToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | NullPointerException exception) {
            return false;
        }
    }

    public String getEmail(String token) {
        Claims claims = getClaimsFormToken(token);
        return claims.get("sub", String.class);
    }

    public Long getExpire(String token) {
        Claims claims = getClaimsFormToken(token);
        return claims.getExpiration().getTime();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaimsFormToken(token);

        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);

        return new UsernamePasswordAuthenticationToken(
                email,
                null,
                List.of(new SimpleGrantedAuthority(role))
        );
    }

    public String parseToken(String headerValue) {
        if (headerValue == null || !headerValue.startsWith("Bearer ")) {
            return null;
        }

        // "Bearer " 이후의 토큰 문자열 추출
        return headerValue.substring(7);
    }

    public String parseToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }

        // "Bearer " 이후의 토큰 문자열 추출
        return authorization.substring(7);
    }

    public String authorizationToJwt(String authorizationHeader) {

        if (authorizationHeader == null) {
            throw new CustomException(ExceptionCode.INVALID_AUTH_HEADER);
        }

        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new CustomException(ExceptionCode.INVALID_AUTH_HEADER_FORMAT);
        }

        // "Bearer " 접두사를 제거한 후 반환
        return authorizationHeader.substring(7);
    }
}