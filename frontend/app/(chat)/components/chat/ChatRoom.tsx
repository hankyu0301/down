"use client";

import React, { useEffect, useState } from "react";
import { getGroupChatRoom } from "@/api/chat";
import { useParams } from "next/navigation";
import ChatContainer from "./ChatContainer";

const ChatRoom = () => {
  const [chatRoom, setChatRoom] = useState("");
  const params = useParams();
  console.log(params);
  useEffect(() => {
    const getChatRoom = async () => {
      const response = await getGroupChatRoom(params.chatRoomId);
      setChatRoom(response.data);
      console.log(response);
    };

    getChatRoom();
  }, []);

  console.log(chatRoom.chatRoomId);

  return (
    <div>
      {chatRoom && <div>{chatRoom.chatRoomName}</div>}
      <ChatContainer />
    </div>
  );
};

export default ChatRoom;
