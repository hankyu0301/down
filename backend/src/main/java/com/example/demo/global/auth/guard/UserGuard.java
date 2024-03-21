package com.example.demo.global.auth.guard;

import com.example.demo.domain.user.entity.EnumUserRole;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.domain.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserGuard extends Guard{

    private final UserRepository userRepository;
    private final List<EnumUserRole> roleTypes = List.of(EnumUserRole.ROLE_ADMIN);

    @Override
    protected List<EnumUserRole> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long id) {
        return userRepository.findById(id)
                .filter(user -> user.getId().equals(AuthUtil.extractUserId()))
                .isPresent();
    }
}
