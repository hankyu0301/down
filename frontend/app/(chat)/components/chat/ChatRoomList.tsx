"use client";

import React, { useEffect, useState } from "react";
import { useQuery } from "@tanstack/react-query";
import {
  createGroupChatRoom,
  getGroupChatRoom,
  getGroupChatRooms,
} from "@/api/chat";
import { useRouter } from "next/navigation";
import { useProfile } from "@/hooks/user/useProfile";
import { useModal } from "@/store/useModalStore";
import { Button } from "@/components/ui/button/button";

interface chatRoomListType {
  chatRoomId: number;
  chatRoomName: string;
  lastMessage: object;
}

const ChatRoomList = () => {
  const [chatRoomList, setChatRoomList] = useState<chatRoomListType[]>();
  const user = useProfile();
  const { onOpen } = useModal();

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
      userId: user?.id,
      userIdList: [user?.id, 1, 2],
      chatRoomName: "새 채팅방",
    };
    const response = await createGroupChatRoom(body);
    console.log(response);
  };

  return (
    <div>
      <Button onClick={() => onOpen("createGroupChat")} variant="default">
        Create group chat
      </Button>
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
