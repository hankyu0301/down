export interface GetChatMessagesType {
  chatRoomId: number;
  lastChatMessageId: number;
  size: number;
}

export interface CreateChatRoomType {
  userIdList: Array<number>;
  chatRoomName: string;
}

export interface GetChatRoomType {
  chatRoomId: number;
}

export interface LeaveChatRoomType {
  userId: number;
  chatRoomId: number;
}

export interface GetChatRoomsType {
  userId: string;
}
