package com.example.demo.global.auth.jwt;


import com.example.demo.domain.user.entity.UserRole;
import com.example.demo.global.auth.PrincipalDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.DatatypeConverter;
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

    @Value("${jwt.token-period}")
    private long TOKEN_PERIOD;

    /**
     * 토큰에서 Claim 추출
     */
    private Claims getClaimsFormToken(String token) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 토큰 발급
     */
    public String generateJwtToken(Authentication authentication) {
        log.info("getPrincipal: {}", authentication.getPrincipal());

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        Map<String, Object> payloads = new HashMap<>();

        // Subject (sub): 토큰의 주체를 식별하는 정보입니다.
        payloads.put("sub", principalDetails.getEmail());

        // Issuer (iss): 토큰을 발급한 발급자를 식별하는 정보입니다.
        payloads.put("iss", TOKEN_PROVIDER);

        // Role (role): 사용자의 역할을 나타내는 정보입니다.
        // 사용자가 어떤 권한을 가지고 있는지를 나타낼 수 있습니다.
        payloads.put("role", UserRole.ROLE_USER.name());
        payloads.put("username", principalDetails.getUsername());
        payloads.put("id", principalDetails.getId());

        Date now = new Date();
        return Jwts.builder()
                .setClaims(payloads)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TOKEN_PERIOD))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 토큰 검증
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFormToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | NullPointerException exception) {
            return false;
        }
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

    public String parseToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }

        // "Bearer " 이후의 토큰 문자열 추출
        return authorization.substring(7);
    }
}