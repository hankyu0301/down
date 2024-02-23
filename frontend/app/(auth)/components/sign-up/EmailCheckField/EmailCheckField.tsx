"use client";
import { useState, useEffect } from "react";
import clsx from "clsx";

import { useSignupContext } from "@/app/(auth)/contexts/sign-up/SignUpContext";
import { FormFieldWrapper } from "@/app/(auth)/components/sign-up";

import { emailCheckFieldSchema } from "@/app/(auth)/constants/sign-up/schema";
import { useCommonForm } from "@/app/(auth)/hooks/sign-up/useCommonForm";

import { postEmailCheck, postSendEmailCode } from "@/api/signup";

import {
	FormControl,
	FormField,
	FormItem,
	FormLabel,
	FormMessage,
} from "@/components/ui";
import { Input, Button } from "@/components/ui";

interface EmailCheckFieldProps {
  onNext: () => void;
};

type EmailCheckStatus = {
	success: boolean;
	data: { checkedEmail: string, available: boolean; } | { errorMessage: string; };
	message: string;
};

type EmailCodeSendingStatus = "sending" | "success" | "error" | null;

const EmailCheckField = ({ onNext }: EmailCheckFieldProps) => {
	const { userInfo, setUserInfo } = useSignupContext();
	const { method } = useCommonForm({ schema: emailCheckFieldSchema, checkMode: "onSubmit" });
	const { email: formErrors } = method.formState.errors;
	const [emailCheckStatus, setEmailCheckStatus] =
		useState<EmailCheckStatus | null>(null);
	const [emailCodeSendingStatus, setEmailCodeSendingStatus] =
		useState<EmailCodeSendingStatus>(null);

	const emailValue = method.watch("email");

	useEffect(() => {
		setEmailCheckStatus(null);
	}, [emailValue]);
  
	const onSubmit = async () => {
		const hasEmailInput = await method.trigger("email");
		if (!hasEmailInput) return;

		const result = await postEmailCheck(emailValue);

		setEmailCheckStatus(result);
		
		if (result.success) {
			setUserInfo({ ...userInfo, email: emailValue });
		}
	};

	const onSendEmailVerificationCode = async () => {
		if (!emailCheckStatus || !emailCheckStatus.success) return;
		try {
			setEmailCodeSendingStatus("sending");
			const result = await postSendEmailCode(userInfo.email);

			if (result.success) {
				setEmailCodeSendingStatus("success");
				onNext();
			} else {
				setEmailCodeSendingStatus("error");
			}
		} catch (error) {
			setEmailCodeSendingStatus("error");
		}

		// onNext(); // 이메일 인증코드 전송 정상 동작하면 삭제
	}

	console.log("formErrors", formErrors);
	console.log("emailCheckStatus", emailCheckStatus)
	
	return (
		<FormFieldWrapper
			method={method}
			onSubmit={onSubmit}
		>
			<FormField
				control={method.control}
				name="email"
				render={({ field }) => (
					<FormItem>
						<FormLabel>이메일 입력</FormLabel>
						<div className="flex gap-2">
							<FormControl>
								<Input
									placeholder="email@example.com"
									{...field}
								/>
							</FormControl>
							<Button type="submit">중복확인</Button>
						</div>
						<FormMessage />
						{!formErrors && emailCheckStatus && (
							<p
								className={clsx(
									"text-sm font-medium",
									emailCheckStatus.success
										? "text-stone-500"
										: "text-destructive"
								)}
							>
								{emailCheckStatus.message}
							</p>
						)}
						{!formErrors && emailCodeSendingStatus === "error" && (
							<p className="text-sm font-medium text-destructive">
								인증코드 전송 중 오류가 발생했습니다. 다시 시도해주세요.
							</p>
						)}
					</FormItem>
				)}
			/>
			<Button
				onClick={onSendEmailVerificationCode}
				disabled={emailCodeSendingStatus === "sending"}
				variant="outline"
				className="w-full"
				type="button"
			>
				{emailCodeSendingStatus === "sending"
					? "인증코드 전송중"
					: "인증코드 전송하기"}
			</Button>
		</FormFieldWrapper>
	);
}

export default EmailCheckField;