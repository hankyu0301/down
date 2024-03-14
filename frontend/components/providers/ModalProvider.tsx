"use client";
import CreateGroupChatModal from "@/app/(chat)/components/modals/CreateGroupChatModal";
import { useEffect, useState } from "react";

export const ModalProvider = () => {
  const [isMounted, setIsMounted] = useState(false);

  useEffect(() => {
    setIsMounted(true);
  }, []);

  if (!isMounted) {
    return null;
  }

  return (
    <>
      <CreateGroupChatModal />
    </>
  );
};
