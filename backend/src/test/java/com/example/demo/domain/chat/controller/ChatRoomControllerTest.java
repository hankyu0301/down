package com.example.demo.domain.chat.controller;

import com.example.demo.domain.chat.dto.request.ChatRoomCreateRequest;
import com.example.demo.domain.chat.dto.request.ChatRoomDeleteRequest;
import com.example.demo.domain.chat.dto.request.ChatRoomInviteRequest;
import com.example.demo.domain.chat.dto.response.*;
import com.example.demo.domain.chat.service.ChatRoomService;
import com.example.demo.domain.user.dto.response.UserInfoResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@WebMvcTest(ChatRoomController.class)
class ChatRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatRoomService chatRoomService;

    ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("O 성공 회원의 모든 채팅방 조회")
    @Test
    @WithMockUser
    void getAllChatRoomByUserId() throws Exception {
        // Given
        List<SimpleChatRoomResponseDto> dtoList = new ArrayList<>();
        LongStream.rangeClosed(0,10).forEach(
                i -> dtoList.add(new SimpleChatRoomResponseDto("lastMessage", LocalDateTime.of(2024,3,18,0,0), "testChatRoom" + i, i)));
        ChatRoomListResponseDto result = new ChatRoomListResponseDto(dtoList);

        given(chatRoomService.getAllChatRoomByUserId(1L)).willReturn(result);

        // When
        mockMvc.perform(get("/api/v1/group/chatRoom/list/{userId}", 1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "success":true,
                            "data":{
                                "simpleChatRoomResponseDtoList":[{
                                    "lastMessage":"lastMessage",
                                    "lastMessageTime":"2024-03-18T00:00:00",
                                    "chatRoomName":"testChatRoom0",
                                    "chatRoomId":0},
                                    
                                    {"lastMessage":"lastMessage",
                                    "lastMessageTime":"2024-03-18T00:00:00",
                                    "chatRoomName":"testChatRoom1",
                                    "chatRoomId":1},
                                    
                                    {"lastMessage":"lastMessage",
                                    "lastMessageTime":"2024-03-18T00:00:00",
                                    "chatRoomName":"testChatRoom2",
                                    "chatRoomId":2},
                                    
                                    {"lastMessage":"lastMessage",
                                    "lastMessageTime":"2024-03-18T00:00:00",
                                    "chatRoomName":"testChatRoom3",
                                    "chatRoomId":3},
                                    
                                    {"lastMessage":"lastMessage",
                                    "lastMessageTime":"2024-03-18T00:00:00",
                                    "chatRoomName":"testChatRoom4",
                                    "chatRoomId":4},
                                    
                                    {"lastMessage":"lastMessage",
                                    "lastMessageTime":"2024-03-18T00:00:00",
                                    "chatRoomName":"testChatRoom5",
                                    "chatRoomId":5},
                                    
                                    {"lastMessage":"lastMessage",
                                    "lastMessageTime":"2024-03-18T00:00:00",
                                    "chatRoomName":"testChatRoom6",
                                    "chatRoomId":6},
                                    
                                    {"lastMessage":"lastMessage",
                                    "lastMessageTime":"2024-03-18T00:00:00",
                                    "chatRoomName":"testChatRoom7",
                                    "chatRoomId":7},
                                    
                                    {"lastMessage":"lastMessage",
                                    "lastMessageTime":"2024-03-18T00:00:00",
                                    "chatRoomName":"testChatRoom8",
                                    "chatRoomId":8},
                                    
                                    {"lastMessage":"lastMessage",
                                    "lastMessageTime":"2024-03-18T00:00:00",
                                    "chatRoomName":"testChatRoom9",
                                    "chatRoomId":9},
                                    
                                    {"lastMessage":"lastMessage",
                                    "lastMessageTime":"2024-03-18T00:00:00",
                                    "chatRoomName":"testChatRoom10",
                                    "chatRoomId":10}]},
                                    
                                "message":"채팅방 목록 조회 성공"
                            }
                """));
    }

    @DisplayName("O 성공 채팅방 정보 상세 조회")
    @Test
    @WithMockUser
    void getChatRoom() throws Exception {
        // Given
        ChatRoomDto dto = ChatRoomDto.builder()
                .chatRoomId(1L)
                .chatRoomName("testChatRoom")
                .userInfoResponseDTOList(List.of(
                        UserInfoResponseDTO.builder()
                                .id(1L)
                                .email("test1@test.email")
                                .nickName("testNickName1")
                                .userName("testUsername1")
                                .build(),
                        UserInfoResponseDTO.builder()
                                .id(2L)
                                .email("test2test.email")
                                .nickName("testNickName2")
                                .userName("testUsername2")
                                .build()
                ))
                .build();

        given(chatRoomService.getChatRoomWithUserListByChatRoomId(1L)).willReturn(dto);

        // When
        mockMvc.perform(get("/api/v1/group/chatRoom/{chatRoomId}", 1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "success":true,
                            "data":{
                                "chatRoomId":1,
                                "chatRoomName":"testChatRoom",
                                "userInfoResponseDTOList":[{
                                    "id":1,
                                    "email":"test1@test.email",
                                    "nickName":"testNickName1",
                                    "userName":"testUsername1"},
                                    
                                    {"id":2,
                                    "email":"test2test.email",
                                    "nickName":"testNickName2",
                                    "userName":"testUsername2"}]},
                                    
                                "message":"채팅방 정보 조회 성공"
                            }
                """));
    }

    @DisplayName("O 성공 채팅방 생성")
    @Test
    @WithMockUser
    void createChatRoom() throws Exception {
        // Given
        ChatRoomCreateRequest req = new ChatRoomCreateRequest(1L, List.of(2L,3L,4L), "testChatRoom");
        ChatRoomCreateResponseDto result = new ChatRoomCreateResponseDto(1L,"testChatRoom", List.of(2L,3L,4L) );

        given(chatRoomService.createChatRoom(any(ChatRoomCreateRequest.class))).willReturn(result);

        // When
        mockMvc.perform(post("/api/v1/group/chatRoom")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "success":true,
                            "data":{
                                "chatRoomId":1,
                                "chatRoomName":"testChatRoom",
                                "userIdList":[2,3,4]},
                                
                            "message":"채팅방 생성 성공"
                        }
                """));
    }

    @DisplayName("O 성공 채팅방 퇴장")
    @Test
    @WithMockUser
    void exitChatRoom() throws Exception {
        // Given
        ChatRoomDeleteRequest req = new ChatRoomDeleteRequest(1L);
        ChatRoomDeleteResponseDto result = new ChatRoomDeleteResponseDto(1L);
        Long chatRoomId = 1L;

        given(chatRoomService.exitChatroom(anyLong(), any(ChatRoomDeleteRequest.class))).willReturn(result);

        // When
        mockMvc.perform(patch("/api/v1/group/chatRoom/{chatRoomId}/exit", chatRoomId)
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "success":true,
                            "data":{
                                "chatRoomId":1
                                },
                            "message":"채팅방 퇴장 성공"}
                """));
    }

    @DisplayName("O 성공 채팅방 초대")
    @Test
    @WithMockUser
    void inviteChatRoom() throws Exception {
        // Given
        ChatRoomInviteRequest req = new ChatRoomInviteRequest(1L,2L, 1L);
        ChatRoomInviteResponseDto result = new ChatRoomInviteResponseDto(1L, 2L, 1L);

        given(chatRoomService.inviteChatRoom(any(ChatRoomInviteRequest.class))).willReturn(result);

        // When
        mockMvc.perform(post("/api/v1/group/chatRoom/invite")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {"success":true,
                            "data":{
                                "inviterId":1,
                                "targetId":2,
                                "chatRoomId":1
                                },
                            "message":"채팅방 초대 성공"}
                """));
    }
}
