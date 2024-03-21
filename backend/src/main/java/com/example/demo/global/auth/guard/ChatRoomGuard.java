package com.example.demo.global.auth.guard;

import com.example.demo.domain.chat.entity.ChatRoom;
import com.example.demo.domain.chat.repository.ChatRoomJpaRepository;
import com.example.demo.domain.user.entity.EnumUserRole;
import com.example.demo.domain.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomGuard extends Guard {

    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final List<EnumUserRole> roleTypes = List.of(EnumUserRole.ROLE_ADMIN);

    @Override
    protected List<EnumUserRole> getRoleTypes() {
        return roleTypes;
    }

    @Override
    protected boolean isResourceOwner(Long chatRoomId) {
        return  chatRoomJpaRepository.findByIdWithChatRoomUser(chatRoomId)
                .map(ChatRoom::getChatRoomUserList)
                .filter(chatRoomUsers -> chatRoomUsers.stream()
                        .anyMatch(
                                chatRoomUser ->
                                        chatRoomUser.getUser().getId().equals(AuthUtil.extractUserId()))
                )
                .isPresent();
    }
}
