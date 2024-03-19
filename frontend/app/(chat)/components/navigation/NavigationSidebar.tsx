"use client";

import React, { useEffect, useState } from "react";
import { Navigation } from "lucide-react";
import NavigationItem from "./NavigationItem";
import { Separator } from "@/components/ui/seperator/separator";
import { ScrollArea } from "@/components/ui/scroll-area/scroll-area";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { getGroupChatRooms } from "@/api/chat";
import { useProfile } from "@/hooks/user/useProfile";

const NavigationSidebar = () => {
  return (
    <div className="space-y-4 flex flex-col items-center h-full text-primary w-full dark:bg-[#1E1F22] bg-[#E3E5E8] py-3"></div>
  );
};

export default NavigationSidebar;
