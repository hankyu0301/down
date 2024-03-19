import { getGroupChatRooms } from "@/api/chat";
import { ScrollArea } from "@/components/ui/scroll-area/scroll-area";
import { Separator } from "@/components/ui/seperator/separator";
import { useProfile } from "@/hooks/user/useProfile";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import GatheringGroupChat from "./GatheringGroupChat";
import GatheringHeader from "./GatheringHeader";

interface ChatRoomListType {
  chatRoomId: number;
  chatRoomName: string;
  lastMessage: string;
  lastMessageTime: [];
}

const GatheringSidebar = () => {
  const [groupChatList, setGroupChatList] = useState<ChatRoomListType>([]);
  const user = useProfile();

  console.log(user);

  //   const queryClient = useQueryClient();
  const { data, isLoading } = useQuery({
    queryKey: ["groupChatRooms"],
    queryFn: () => getGroupChatRooms(user.id),
    enabled: !!user, // 유저 정보가 있는 경우에만 쿼리 실행
  });

  useEffect(() => {
    if (!isLoading && data) {
      setGroupChatList(data.data.simpleChatRoomResponseDtoList);
    }
  }, [data, isLoading]);
  return (
    <div className="flex flex-col h-full text-primary w-full dark:bg-[#2B2D31] bg-[#F2F3F5] pt-12">
      <GatheringHeader />
      <ScrollArea className="flex-1 px-3">
        <div className="mt-2"></div>
        <Separator className="bg-zinc-200 dark:bg-zinc-700 rounded-md my-2" />

        <div className="space-y-[2px]">
          {groupChatList.map((list) => (
            <GatheringGroupChat key={list.chatRoomId} groupChat={list} />
          ))}
        </div>
      </ScrollArea>
    </div>
  );
};

export default GatheringSidebar;
