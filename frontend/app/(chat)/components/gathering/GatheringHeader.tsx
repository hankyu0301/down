"use client";

import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu/dropdown-menu";

import {
  ChevronDown,
  LogOut,
  PlusCircle,
  Settings,
  Trash,
  UserPlus,
  Users,
} from "lucide-react";

import { useModal } from "@/store/useModalStore";

const GatheringHeader = () => {
  const { onOpen } = useModal();
  return (
    <DropdownMenu>
      <DropdownMenuTrigger className="focus:outline-none" asChild>
        <button className="w-full text-md font-semibold px-3 flex items-center h-12 border-neutral-200 dark:border-neutral-800 border-b-2 hover:bg-zinc-700/10 dark:hover:bg-zinc-700/50 transition">
          <ChevronDown className="h-5 w-5 ml-auto" />
        </button>
      </DropdownMenuTrigger>
      <DropdownMenuContent className="w-56 text-xs font-medium text-black dark:text-neutral-400 space-y-[2px]">
        <DropdownMenuItem
          onClick={() => onOpen("inviteGroupChat")}
          className="text-indigo-600 dark:text-indigo-400 px-3 py-2 text-sm cursor-pointer"
        >
          Invite Group Chat
          <UserPlus className="h-4 w-4 ml-auto" />
        </DropdownMenuItem>
        <DropdownMenuItem className=" px-3 py-2 text-sm cursor-pointer">
          Gathering Settings
          <Settings className="h-4 w-4 ml-auto" />
        </DropdownMenuItem>

        <DropdownMenuItem className=" px-3 py-2 text-sm cursor-pointer">
          Manage Members
          <Users className="h-4 w-4 ml-auto" />
        </DropdownMenuItem>

        <DropdownMenuItem
          onClick={() => onOpen("createGroupChat")}
          className=" px-3 py-2 text-sm cursor-pointer"
        >
          Create GroupChat
          <PlusCircle className="h-4 w-4 ml-auto" />
        </DropdownMenuItem>

        <DropdownMenuItem className="text-rose-500 px-3 py-2 text-sm cursor-pointer">
          Delete Gathering
          <Trash className="h-4 w-4 ml-auto" />
        </DropdownMenuItem>

        <DropdownMenuItem className="text-rose-500 px-3 py-2 text-sm cursor-pointer">
          Leave Gathering
          <LogOut className="h-4 w-4 ml-auto" />
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
};

export default GatheringHeader;
