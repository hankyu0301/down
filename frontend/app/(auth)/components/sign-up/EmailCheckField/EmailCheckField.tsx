"use client";
import { useState, useEffect } from "react";
import clsx from "clsx";

import { useSignupContext } from "@/app/(auth)/contexts/sign-up/SignUpContext";
import { FormFieldWrapper } from "@/app/(auth)/components/sign-up";

import { emailCheckFieldSchema } from "@/app/(auth)/constants/sign-up/schema";
import { useCommonForm } from "@/app/(auth)/hooks/sign-up/useCommonForm";

import { postEmailCheck, postSendEmailCode } from "@/api/signup";

import { ToastSuccess } from "@/lib/toastifyAlert";
import { TOAST_MESSAGE } from "@/constants/toastMessage/signup";

import {
	FieldProps,
	EmailCheckResponse,
	EmailCodeSendingStatus,
} from "@/app/(auth)/types/signup";

import {
	FormControl,
	FormField,
	FormItem,
	FormLabel,
	FormMessage,
} from "@/components/ui";
import { Input, Button } from "@/components/ui";

const EmailCheckField = ({ onNext }: FieldProps) => {
	const { userEmailInfo, setUserEmailInfo } = useSignupContext();

	const { method } = useCommonForm({
		schema: emailCheckFieldSchema,
		checkMode: "onSubmit",
		defaultValues: { email: "" },
	});
	const { email: formErrors } = method.formState.errors;

	const [emailCheckResponse, setEmailCheckResponse] =
		useState<EmailCheckResponse | null>(null);
	const [emailCheckErrorMessage, setEmailCheckErrorMessage] = useState<
		string | null
	>(null);
	const [emailCodeSendingStatus, setEmailCodeSendingStatus] =
		useState<EmailCodeSendingStatus>(null);

	const emailValue = method.watch("email");

	useEffect(() => {
		setEmailCheckResponse(null);
		setEmailCheckErrorMessage(null);
	}, [emailValue]);

	const onSubmit = async () => {
		const hasEmailInput = await method.trigger("email");
		if (!hasEmailInput) return;

		const result = await postEmailCheck(emailValue);

		setEmailCheckResponse(result);

		if (result.success) {
			setEmailCheckErrorMessage(null);
			setUserEmailInfo({ ...userEmailInfo, email: emailValue });
		}
	};

	const onSendEmailVerificationCode = async () => {
		const hasEmailInput = await method.trigger("email");
		if (!hasEmailInput) return;
		if (!emailCheckResponse || !emailCheckResponse.success) {
			setEmailCheckErrorMessage("먼저 이메일 중복확인을 해주세요.");
			return;
		}

		try {
			setEmailCodeSendingStatus("sending");
			const result = await postSendEmailCode(userEmailInfo.email);

			if (result.success) {
				setEmailCodeSendingStatus("success");
				ToastSuccess(TOAST_MESSAGE.SUCCESS_SEND_CODE);
				onNext();
			} else {
				setEmailCodeSendingStatus("error");
			}
		} catch (error) {
			setEmailCodeSendingStatus("error");
		}
	};

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
						{!formErrors && !emailCheckResponse && emailCheckErrorMessage && (
							<p className="text-sm font-medium text-destructive">
								{emailCheckErrorMessage}
							</p>
						)}
						{!formErrors && emailCheckResponse && (
							<p
								className={clsx(
									"text-sm font-medium",
									emailCheckResponse.success
										? "text-stone-500"
										: "text-destructive"
								)}
							>
								{emailCheckResponse.message}
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
};

export default EmailCheckField;
