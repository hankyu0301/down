"use client";

import React, { useEffect } from "react";
import qs from "query-string";
import axios from "axios";
import * as z from "zod";
import { useRouter, useParams } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useModal } from "@/store/useModalStore";
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog/dialog";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form/form";
import { Input } from "@/components/ui/input/input";
import { Button } from "@/components/ui/button/button";

import { useProfile } from "@/hooks/user/useProfile";
import { inviteGroupChat } from "@/api/chat";

const InviteGroupChatModal = () => {
  const { isOpen, onClose, type, data } = useModal();
  const { chatRoomId, chatRoomName } = data;
  const router = useRouter();
  const params = useParams();
  const user = useProfile();

  const formSchema = z.object({
    targetId: z.string().min(1, {
      message: "targetId is required.",
    }),
    chatRoomId: z.string().min(1, {
      message: "ChatRoomId is required.",
    }),
  });

  const isModalOpen = isOpen && type === "inviteGroupChat";

  const form = useForm({
    resolver: zodResolver(formSchema),
    defaultValues: {
      targetId: "",
      chatRoomId: chatRoomId,
    },
  });

  const isLoading = form.formState.isSubmitting;

  const onSubmit = async (values: z.infer<typeof formSchema>) => {
    console.log(values);
    try {
      const body = {
        inviterId: Number(user?.id),
        chatRoomId: Number(params.chatRoomId),
        targetId: Number(values.targetId),
      };
      const response = await inviteGroupChat(body);
      console.log(response);
      form.reset();
      router.refresh();
      onClose();
    } catch (error) {}
  };

  const handleClose = () => {
    form.reset();
    onClose();
  };

  return (
    <Dialog open={isModalOpen} onOpenChange={handleClose}>
      <DialogContent className="bg-white text-black p-0 overflow-hidden">
        <DialogHeader className="pt-8 px-6">
          <DialogTitle className="text-2xl text-center font-bold">
            Invite Group Chat {chatRoomName}
          </DialogTitle>
        </DialogHeader>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
            <div className="space-y-8 px-6">
              <FormField
                control={form.control}
                name="targetId"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="uppercase text-xs font-bold text-zinc-500">
                      targetId
                    </FormLabel>
                    <FormControl>
                      <Input
                        disabled={isLoading}
                        className="bg-zinc-300/50 border-0 focus-visible:ring-0 text-black focus-visible:ring-offset-0"
                        placeholder="Enter targetId"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="chatRoomId"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="uppercase text-xs font-bold text-zinc-500">
                      chatRoomId
                    </FormLabel>
                    <FormControl>
                      <Input
                        disabled={isLoading}
                        className="bg-zinc-300/50 border-0 focus-visible:ring-0 text-black focus-visible:ring-offset-0"
                        placeholder="Enter chatRoomId"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>
            <DialogFooter className="bg-gray-100 px-6 py-4">
              <Button disabled={isLoading} variant="default">
                Invite
              </Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
};

export default InviteGroupChatModal;
