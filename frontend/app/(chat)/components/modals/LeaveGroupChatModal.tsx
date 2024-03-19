"use client";

import { useState } from "react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog/dialog";

import { useModal } from "@/store/useModalStore";
import { Button } from "@/components/ui/button/button";
import { useParams, useRouter } from "next/navigation";
import { leaveGroupChatRoom } from "@/api/chat";
import { useProfile } from "@/hooks/user/useProfile";

const LeaveGroupChatModal = () => {
  const { isOpen, onClose, type, data } = useModal();
  const router = useRouter();
  const user = useProfile();
  const params = useParams();
  const [isLoading, setIsLoading] = useState(false);

  const isModalOpen = isOpen && type === "leaveGroupChat";
  const { chatRoomId, chatRoomName } = data;

  const onClick = async () => {
    try {
      setIsLoading(true);
      const chatRoomId = params.chatRoomId;
      const body = {
        userId: user?.id,
      };

      await leaveGroupChatRoom(chatRoomId, body);
      onClose();
      router.refresh();
      router.push("/");
    } catch (error) {
      console.log(error);
    } finally {
      setIsLoading(true);
    }
  };

  return (
    <Dialog open={isModalOpen} onOpenChange={onClose}>
      <DialogContent className="bg-white text-black p-0 overflow-hidden">
        <DialogHeader className="pt-8 px-6">
          <DialogTitle className="text-2xl text-center font-bold">
            Leave Group Chat
          </DialogTitle>
          <DialogDescription className="text-center text-zinc-500">
            Are you sure you want to leave{" "}
            <span className="font-semibold text-indigo-500">
              {chatRoomName}
            </span>
            ?
          </DialogDescription>
        </DialogHeader>
        <DialogFooter className="bg-gray-100 px-6 py-4">
          <div className="flex items-center justify-between w-full">
            <Button disabled={isLoading} onClick={onClose} variant="ghost">
              Cancel
            </Button>
            <Button disabled={isLoading} onClick={onClick} variant="default">
              Confirm
            </Button>
          </div>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default LeaveGroupChatModal;
