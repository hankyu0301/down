"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import clsx from "clsx";

import { useSignupContext } from "@/app/(auth)/contexts/sign-up/SignUpContext";
import { FormFieldWrapper } from "@/app/(auth)/components/sign-up";

import { useCommonForm } from "@/app/(auth)/hooks/sign-up/useCommonForm";
import { emailCodeFieldSchema } from "@/app/(auth)/constants/sign-up/schema";

import { postSendEmailCode, postCheckEmailCode } from "@/api/signup";

import { ToastSuccess } from "@/lib/toastifyAlert";

import { TOAST_MESSAGE } from "@/constants/toastMessage/signup";

import {
	EmailCodeResponse,
	EmailCodeSendingStatus,
	FieldProps,
} from "@/app/(auth)/types/signup";

import {
	FormControl,
	FormField,
	FormItem,
	FormLabel,
	FormMessage,
} from "@/components/ui";
import { Input, Button } from "@/components/ui";

const EmailCodeField = ({ onNext }: FieldProps) => {
	const { userEmailInfo, setUserEmailInfo } = useSignupContext();
	const router = useRouter();

	const { method } = useCommonForm({
		schema: emailCodeFieldSchema,
		checkMode: "onSubmit",
	});
	const { email: formErrors } = method.formState.errors;

	const [emailCodeSendingStatus, setEmailCodeSendingStatus] =
		useState<EmailCodeSendingStatus>("success");
	const [emailCodeResponse, setEmailCodeResponse] =
		useState<EmailCodeResponse | null>(null);

	// 이메일 인증코드 다시 보내기
	const onSendEmailCode = async () => {
		setEmailCodeResponse(null);
		try {
			setEmailCodeSendingStatus("sending");
			const result = await postSendEmailCode(userEmailInfo.email);

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
		if (!userEmailInfo.email) router.refresh();

		const code = method.getValues("emailCode");

		const result = await postCheckEmailCode(userEmailInfo.email, code);

		setEmailCodeResponse(result);

		if (result.success) {
			setUserEmailInfo({ ...userEmailInfo, emailCode: code });
			ToastSuccess(TOAST_MESSAGE.SUCCESS_CHECK_CODE);
			onNext();
		}
	};

	return (
		<FormFieldWrapper
			method={method}
			onSubmit={onCheckEmailCode}
		>
			{!formErrors && emailCodeSendingStatus === "success" && (
				<p className="text-sm font-medium text-stone-500">
					{userEmailInfo.email}으로 인증코드가 전송되었습니다.
					<br />
					메일이 오지 않았다면 스팸메일함을 확인해주세요.
				</p>
			)}
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
									{emailCodeSendingStatus === "sending" ? "전송중" : "재전송"}
								</Button>
							</div>
						</div>
						<FormMessage />
						{!formErrors && emailCodeResponse && (
							<p
								className={clsx(
									"text-sm font-medium",
									emailCodeResponse.success
										? "text-stone-500"
										: "text-destructive"
								)}
							>
								{emailCodeResponse.message}
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
