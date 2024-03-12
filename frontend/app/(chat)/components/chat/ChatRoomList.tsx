import React, { useEffect, useState } from "react";
import {
  createGroupChatRoom,
  getGroupChatRoom,
  getGroupChatRooms,
} from "@/api/chat";
import { useRouter } from "next/navigation";

interface chatRoomListType {
  chatRoomId: number;
  chatRoomName: string;
  lastMessage: object;
}

const ChatRoomList = () => {
  const [chatRoomList, setChatRoomList] = useState<chatRoomListType[]>();

  const router = useRouter();
  useEffect(() => {
    const getChatRoomList = async () => {
      const response = await getGroupChatRooms(1);
      console.log(response.data);
      setChatRoomList(response.data.simpleChatRoomResponseDtoList);
    };

    getChatRoomList();
  }, []);

  const createChat = async () => {
    const body = {
      userId: 1,
      userIdList: [1, 2, 3],
      chatRoomName: "새 채팅방",
    };
    const response = await createGroupChatRoom(body);
    console.log(response);
  };

  return (
    <div>
      <p onClick={createChat}>채팅방만들기</p>
      {chatRoomList && (
        <>
          {chatRoomList.map((list) => (
            <div
              key={list.chatRoomId}
              onClick={() => router.push(`/chat/${list.chatRoomId}`)}
            >
              {list.chatRoomName}
            </div>
          ))}
        </>
      )}
    </div>
  );
};

export default ChatRoomList;
