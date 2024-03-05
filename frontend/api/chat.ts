import instance from "@/lib/axios/instance";
import {
  CreateChatRoomType,
  GetChatMessagesType,
  GetChatRoomType,
  GetChatRoomsType,
  LeaveChatRoomType,
} from "@/types/chat";

// 채팅 메세지

// 채팅 메세지 조회
export const getChatMessages = async ({
  chatRoomId,
  lastChatMessageId,
  size,
}: GetChatMessagesType) => {
  try {
    const response = await instance.get(`/chat/message`, {
      params: {
        chatRoomId,
        lastChatMessageId,
        size,
      },
    });
    return response.data;
  } catch (error) {
    return error;
  }
};

// 채팅방

// 채팅방 생성
export const createChatRoom = async (body: CreateChatRoomType) => {
  try {
    const response = await instance.post(`/chatRoom`, body);
    return response.data;
  } catch (error) {
    return error;
  }
};

// 채팅방 정보 조회
export const getChatRoom = async ({ chatRoomId }: GetChatRoomType) => {
  try {
    const response = await instance.get(`/chatRoom`, {
      params: {
        chatRoomId,
      },
    });
  } catch (error) {
    return error;
  }
};

// 채팅방 목록 조회
export const getChatRooms = async ({ userId }: GetChatRoomsType) => {
  try {
    const response = await instance.get(`/chatRoom/list`, {
      params: {
        userId,
      },
    });
    return response.data;
  } catch {}
};

// 채팅방 퇴장
// export const leaveChatRoom = async (body: LeaveChatRoomType) => {
//   try {
//     const response = await instance.delete(`/chatRoom`, body);
//     return response.data;
//   } catch (error) {}
// };
