"use client";

import React from "react";
import { useRef, useEffect, useState } from "react";

import ChatInput from "./ChatInput";
import { useSocket } from "@/components/providers/SocketProvider";
import SockJs from "sockjs-client";
import SocketIndicator from "@/components/ui/socket/SocketIndicator";
import { useProfile } from "@/hooks/user/useProfile";
import { cn } from "@/lib/cn";

const ChatContainer = () => {
  const [message, setMessage] = useState("");
  const [messageList, setMessageList] = useState([]);
  const messagesEndRef = useRef(null as any);

  const user = useProfile();

  const { socket } = useSocket();

  useEffect(() => {
    if (socket) {
      socket.onmessage = function (e: any) {
        const { data, id } = JSON.parse(e.data);
        // @ts-ignore
        setMessageList((prev) => [
          ...prev,
          { msg: data, type: "other", id: id },
        ]);
      };
    }
  }, [socket]);

  useEffect(() => {
    if (user && socket) {
      const sendData = {
        type: "id",
        data: user?.nickName,
      };

      socket.send(JSON.stringify(sendData));
      console.log("데이터보냄");
    }
  }, [user]);

  useEffect(() => {
    scrollToBottom();
  }, [messageList]);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  return (
    <div className="m-auto w-[60%]">
      <SocketIndicator />
      <h3 className="text-lg font-semibold mb-2">
        Login as a "{user?.nickName}"
      </h3>
      <div className="h-[60vh] p-4 rounded-lg overflow-auto bg-indigo-50">
        <ul className="flex flex-col gap-2">
          {messageList.map((v: any, i) => (
            <li
              className={cn(
                v.type === "me"
                  ? "float-right self-end text-right"
                  : "float-left items-start text-left"
              )}
              key={`${i}_li`}
            >
              <div className="text-gray-600">{v.id}</div>
              <div
                className={cn(
                  "p-2 rounded-md",
                  v.type === "me"
                    ? "bg-blue-500 text-white"
                    : "bg-white float-left"
                )}
              >
                {v.msg}
              </div>
            </li>
          ))}
          <li ref={messagesEndRef} />
        </ul>
      </div>
      <ChatInput
        message={message}
        setMessage={setMessage}
        setMessageList={setMessageList}
        className="mt-4"
      />
    </div>
  );
};

export default ChatContainer;
