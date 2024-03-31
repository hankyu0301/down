package com.example.demo.global.config;

import com.example.demo.global.auth.OAuth2FailureHandler;
import com.example.demo.global.auth.OAuth2SuccessHandler;
import com.example.demo.global.auth.handler.UserAccessDeniedHandler;
import com.example.demo.global.auth.handler.UserAuthenticationEntryPoint;
import com.example.demo.global.auth.jwt.JwtTokenProvider;
import com.example.demo.global.auth.oauth.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    private final OAuth2UserService oauth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final JwtTokenProvider jwtTokenProvider;

    public static final String[] PUBLIC_URLS = {
            "/loginForm/**",
            "/address/**",
            "/chat/**",
            "/check-mysql-connection/**",
    };

    public static final String[] PRIVATE_URLS = {
            "/private/**", "/api/v1/private/chat/**", "/api/v1/private/chatRoom/**", "/api/v1/group/chat/**", "/api/v1/group/chatRoom/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .exceptionHandling(
                        exception -> exception
                                .authenticationEntryPoint(new UserAuthenticationEntryPoint())
                                .accessDeniedHandler(new UserAccessDeniedHandler())
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_URLS).permitAll()
                        .requestMatchers(PRIVATE_URLS).authenticated()
                        .anyRequest().permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                )

                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/loginForm")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oauth2UserService)
                        )
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler)
                )
                .apply(jwtSecurityConfig());

                 //.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtSecurityConfig jwtSecurityConfig() {
        return new JwtSecurityConfig(jwtTokenProvider);
    }
}

