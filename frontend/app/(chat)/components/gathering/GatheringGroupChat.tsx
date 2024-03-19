"use client";

import { Edit, Hash, Lock, Mic, Trash, Video } from "lucide-react";
import { useParams, useRouter } from "next/navigation";

import { cn } from "@/lib/cn";
import ActionTooltip from "../ActionTooltip";
import { useProfile } from "@/hooks/user/useProfile";
import { useEffect, useState } from "react";
import { getGroupChatRooms } from "@/api/chat";
import { useQuery } from "@tanstack/react-query";

const GatheringGroupChat = ({ groupChat }: any) => {
  const router = useRouter();

  return (
    <button
      onClick={() => router.push(`/chat/${groupChat.chatRoomId}`)}
      className={cn(
        "group px-2 py-2 rounded-md flex items-center gap-x-2 w-full hover:bg-zinc-700/10 dark:hover:bg-zinc-700/50 transition mb-1"
      )}
    >
      <p
        className={cn(
          "line-clamp-1 font-semibold text-sm text-zinc-500 group-hover:text-zinc-600 dark:text-zinc-400 dark:group-hover:text-zinc-300 transition"
        )}
      >
        {groupChat.chatRoomName}
      </p>
    </button>
  );
};

export default GatheringGroupChat;
