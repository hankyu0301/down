"use client";

import {
  Edit,
  Hash,
  Lock,
  Mic,
  Trash,
  Video,
  UserPlus,
  LogOut,
} from "lucide-react";
import { useParams, useRouter } from "next/navigation";

import { cn } from "@/lib/cn";
import ActionTooltip from "../ActionTooltip";
import { useProfile } from "@/hooks/user/useProfile";
import { useEffect, useState } from "react";
import { getGroupChatRooms } from "@/api/chat";
import { useQuery } from "@tanstack/react-query";
import { ModalType, useModal } from "@/store/useModalStore";

const GatheringGroupChat = ({ groupChat }: any) => {
  const { onOpen } = useModal();
  const router = useRouter();

  const onAction = (e: React.MouseEvent, action: ModalType) => {
    e.stopPropagation();
    onOpen(action, groupChat);
  };

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
      <div className="ml-auto flex items-center gap-x-2">
        <ActionTooltip label="Invite">
          <UserPlus
            onClick={(e) => onAction(e, "inviteGroupChat")}
            className="hidden group-hover:block w-4 h-4 text-zinc-500 hover:text-zinc-600 dark:text-zinc-400 dark:hover:text-zinc-300 transition"
          />
        </ActionTooltip>
        <ActionTooltip label="Leave">
          <LogOut
            onClick={(e) => onAction(e, "leaveGroupChat")}
            className="hidden group-hover:block w-4 h-4 text-zinc-500 hover:text-zinc-600 dark:text-zinc-400 dark:hover:text-zinc-300 transition"
          />
        </ActionTooltip>
      </div>
    </button>
  );
};

export default GatheringGroupChat;
