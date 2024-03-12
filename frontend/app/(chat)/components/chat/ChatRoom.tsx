"use client";

import React, { useEffect, useState } from "react";
import { getGroupChatRoom } from "@/api/chat";
import { useParams } from "next/navigation";

const ChatRoom = () => {
  const [chatRoom, setChatRoom] = useState([]);
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
  return (
    <div>
      채팅방
      {/* {chatRoom.map((room) => (
        <div>{room.chatRoomName}</div>
      ))} */}
    </div>
  );
};

export default ChatRoom;
