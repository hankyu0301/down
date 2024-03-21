package com.example.demo.global.auth.guard;

import com.example.demo.domain.chat.repository.PrivateChatRoomJpaRepository;
import com.example.demo.domain.user.entity.EnumUserRole;
import com.example.demo.domain.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivateChatRoomGuard extends Guard {

    private final PrivateChatRoomJpaRepository privateChatRoomJpaRepository;
    private final List<EnumUserRole> roleTypes = List.of(EnumUserRole.ROLE_ADMIN);

    @Override
    protected List<EnumUserRole> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long chatRoomId) {
        return  privateChatRoomJpaRepository.findByIdWithUsers(chatRoomId)
                .filter(pcr -> pcr.isParticipant(AuthUtil.extractUserId()))
                .isPresent();
    }
}
