package com.example.demo.domain.chat.entity;

import com.example.demo.domain.chat.dto.response.ChatMessageDto;
import com.example.demo.domain.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "group_chat_message")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "content")
    private String content;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private MessageType type;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    public ChatMessage(Long userId, String userName, String content, MessageType type, ChatRoom chatRoom) {
        this.userId = userId;
        this.userName = userName;
        this.content = content;
        this.type = type;
        this.chatRoom = chatRoom;
    }

    public ChatMessageDto of() {
        return new ChatMessageDto(
                this.id,
                this.chatRoom.getId(),
                this.chatRoom.getChatRoomName(),
                this.userId,
                this.userName,
                this.content,
                this.getType(),
                this.getCreatedAt()
        );
    }

    public enum MessageType {
        ENTER, TALK, QUIT
    }
}
