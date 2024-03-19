"use client";

import React, { useEffect, useState } from "react";
import { getGroupChatRoom } from "@/api/chat";
import { useParams } from "next/navigation";
import ChatContainer from "./ChatContainer";

const ChatRoom = () => {
  return (
    <div>
      <ChatContainer />
    </div>
  );
};

export default ChatRoom;
