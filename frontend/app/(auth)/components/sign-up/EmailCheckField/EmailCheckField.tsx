"use client";
import { useState } from "react";
import clsx from "clsx";

import { useSignupContext } from "@/app/(auth)/contexts/sign-up/SignUpContext";
import { FormFieldWrapper } from "@/app/(auth)/components/sign-up";

import { emailCheckFieldSchema } from "@/app/(auth)/constants/sign-up/schema";
import { useCommonForm } from "@/app/(auth)/hooks/sign-up/useCommonForm";

import { postEmailCheck } from "@/api/signup";

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
}

type EmailCheckStatus = {
	success: boolean;
	data: { checkedEmail: string, available: boolean; } | { errorMessage: string; };
	message: string;
};

const EmailCheckField = ({ onNext }: EmailCheckFieldProps) => {
	const { userInfo, setUserInfo } = useSignupContext();
	const { method } = useCommonForm({ schema: emailCheckFieldSchema, checkMode: "onSubmit" });
	const { email: formErrors } = method.formState.errors;
	const [emailCheckStatus, setEmailCheckStatus] =
		useState<EmailCheckStatus | null>(null);
  
	const onSubmit = async () => {
		const hasEmailInput = await method.trigger("email");
		if (!hasEmailInput) return;

		const emailInput = method.getValues("email");

		const result = await postEmailCheck(emailInput);

		setEmailCheckStatus(result);
		
		if (result.success) {
			setUserInfo({ ...userInfo, email: emailInput });
			onNext();
		};
	};

	// console.log("formErrors", formErrors);
	// console.log("emailCheckStatus", emailCheckStatus)
	
	return (
		<FormFieldWrapper method={method} onSubmit={onSubmit}>
			<FormField
				control={method.control}
				name="email"
				render={({ field }) => (
					<FormItem>
						<FormLabel>이메일</FormLabel>
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
							<p className={clsx("text-sm font-medium", emailCheckStatus.success ? "text-stone-500" : "text-destructive")}>
								{emailCheckStatus.message}
							</p>
						)}
					</FormItem>
				)}
			/>
		</FormFieldWrapper>
	);
}

export default EmailCheckField;