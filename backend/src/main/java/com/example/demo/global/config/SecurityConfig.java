package com.example.demo.global.config;

import com.example.demo.global.auth.OAuth2SuccessHandler;
import com.example.demo.global.auth.jwt.JwtAuthenticationFilter;
import com.example.demo.global.auth.jwt.JwtTokenProvider;
import com.example.demo.global.auth.oauth.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2UserService oauth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    public static final String[] PUBLIC_URLS = {
            "/loginForm/**",
            "/address/**",
            "/chat/**",
    };

    public static final String[] PRIVATE_URLS = {
            "/private/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
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
                )

                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}