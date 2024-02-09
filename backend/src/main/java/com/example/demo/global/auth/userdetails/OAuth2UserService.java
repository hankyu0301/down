package com.example.demo.global.auth.userdetails;

import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder encoder;
    private final UserRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getClientId();
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" + providerId; //중복이 발생하지 않도록 provider와 providerId를 조합
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER"; //일반 유저
        Optional<User> findMember = memberRepository.findByName(username);

        if (findMember.isEmpty()) { //찾지 못했다면
            User member = User.builder()
                    .name(username)
                    .email(email)
                    .password(encoder.encode("password"))
                    .role(role)
                    .provider(provider)
                    .providerId(providerId).build();
            memberRepository.save(member);
        }
        return oAuth2User;
    }
}