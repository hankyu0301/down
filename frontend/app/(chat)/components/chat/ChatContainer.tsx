"use client";

import React from "react";
import { useRef, useEffect, useState } from "react";

import ChatInput from "./ChatInput";
import SockJs from "sockjs-client";
import SocketIndicator from "@/components/ui/socket/SocketIndicator";

const ChatContainer = () => {
  return (
    <div className="m-auto w-[60%]">
      <SocketIndicator />

      <ChatInput />
    </div>
  );
};

export default ChatContainer;
