"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";

import { useSignupContext } from "@/app/(auth)/contexts/sign-up/SignUpContext";
import { FormFieldWrapper } from "@/app/(auth)/components/sign-up";

import { useCommonForm } from "@/app/(auth)/hooks/sign-up/useCommonForm";
import { emailCodeFieldSchema } from "@/app/(auth)/constants/sign-up/schema";

import { postSendEmailCode, postCheckEmailCode } from "@/api/signup";

import {
	FormControl,
	FormField,
	FormItem,
	FormLabel,
	FormDescription,
	FormMessage,
} from "@/components/ui";
import { Input, Button } from "@/components/ui";

interface EmailCodeFieldProps {
	onNext: () => void;
}

type EmailCodeSendingStatus = "sending" | "success" | "error" | null;

type EmailCodeStatus = {
	success: boolean;
	data: { checkedEmail: string; result: boolean } | { errorMessage: string };
	message: string;
};

const EmailCodeField = ({ onNext }: EmailCodeFieldProps) => {
	const { userInfo, setUserInfo } = useSignupContext();
	const router = useRouter();
	console.log(userInfo);
	const { method } = useCommonForm({
		schema: emailCodeFieldSchema,
		checkMode: "onSubmit",
	});
	const { email: formErrors } = method.formState.errors;
	const [emailCodeSendingStatus, setEmailCodeSendingStatus] =
		useState<EmailCodeSendingStatus>("success");

	// 이메일 인증코드 다시 보내기
	const onSendEmailCode = async () => {
		try {
			setEmailCodeSendingStatus("sending");
			const result = await postSendEmailCode(userInfo.email);

			if (result.success) {
				setEmailCodeSendingStatus("success");
			} else {
				setEmailCodeSendingStatus("error");
			}
		} catch (error) {
			setEmailCodeSendingStatus("error");
		}
	};

	// 이메일 인증코드 확인
	const onCheckEmailCode = async () => {
		if (emailCodeSendingStatus !== "success") return;
		if (!userInfo.email) router.refresh();

		const code = method.getValues("emailCode");
		
		try {
			const result = await postCheckEmailCode(userInfo.email, code);
		} catch (error) {
			console.log(error);
		}
	};

	return (
		<FormFieldWrapper
			method={method}
			onSubmit={onCheckEmailCode}
		>
			<FormField
				control={method.control}
				name="emailCode"
				render={({ field }) => (
					<FormItem className="space-y-4">
						<div className="space-y-2">
							<FormLabel>이메일 인증코드</FormLabel>
							<div className="flex gap-2">
								<FormControl>
									<Input
										placeholder="인증코드를 입력해주세요."
										autoComplete="off"
										{...field}
									/>
								</FormControl>
								<Button
									onClick={onSendEmailCode}
									disabled={emailCodeSendingStatus === "sending"}
									type="button"
								>
									{emailCodeSendingStatus === "sending"
										? "전송중"
										: "재전송"}
								</Button>
							</div>
						</div>
						<FormMessage />
						{!formErrors && emailCodeSendingStatus === "success" && (
							<p className="text-sm font-medium text-stone-500">
								{userInfo.email}로 인증코드가 전송되었습니다.
								<br />메일이 오지 않았다면 스팸메일함을 확인해주세요.
							</p>
						)}
						{!formErrors && emailCodeSendingStatus === "error" && (
							<p className="text-sm font-medium text-destructive">
								인증코드 전송 중 오류가 발생했습니다. 다시 시도해주세요.
							</p>
						)}
						<Button
							onClick={onCheckEmailCode}
							disabled={emailCodeSendingStatus !== "success"}
							type="button"
							variant="outline"
							className="w-full"
						>
							인증코드 확인
						</Button>
					</FormItem>
				)}
			/>
		</FormFieldWrapper>
	);
};

export default EmailCodeField;
