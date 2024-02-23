"use client";
import { useState } from "react";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { signUpSchema } from "../../../constants/sign-up/schema";

import { postEmailCheck, postSendEmailCode, postCheckEmailCode } from "@/api/signup";

import {
	Form,
	FormControl,
	FormDescription,
	FormField,
	FormItem,
	FormLabel,
	FormMessage,
} from "@/components/ui";
import { Input, Button } from "@/components/ui";

import { EmailCheckField } from "@/app/(auth)/components/sign-up";

type EmailCodeSendingStatus = "sending" | "success" | "error" | null;

type EmailValidationStatus = {
	success: boolean;
	data: {email: string, result: boolean};
	message: string;
}

const PrevSignUpForm = () => {
	const [emailCodeSendingStatus, setEmailCodeSendingStatus] =
		useState<EmailCodeSendingStatus>(null);
	const [emailValidationStatus, setEmailValidationStatus] =
		useState<EmailValidationStatus | null>(null);

	const form = useForm<z.infer<typeof signUpSchema>>({
		resolver: zodResolver(signUpSchema),
		defaultValues: {
			email: "",
			emailCode: "",
			nickname: "",
			password: "",
		}
	});
	const { formState: { errors } } = form;

	// 이메일 인증코드 전송
	// const onSendEmailCode = async () => {
	// 	if (errors.email) return;
	// 	if (!emailCheckStatus?.success) return;

	// 	try {
	// 		setEmailCodeSendingStatus("sending");
	// 		const email = form.getValues("email");
	// 		const result = await postSendEmailCode(email);

	// 		if (result.success) {
	// 			setEmailCodeSendingStatus("success");
	// 		} else {
	// 			setEmailCodeSendingStatus("error");
	// 		}
	// 	} catch (error) {
	// 		setEmailCodeSendingStatus("error");
	// 	}
	// };

	// 이메일 인증코드 확인
	const onCheckEmailCode = async () => {
		if (errors.email || errors.emailCode) return;
		if (emailCodeSendingStatus !== "success") return;

		const email = form.getValues("email");
		const code = form.getValues("emailCode");
		const response = await postCheckEmailCode(email, code);
	};

	const onSubmit = async (values: z.infer<typeof signUpSchema>) => {
		// 회원가입 양식 제출 로직 실행
	};

	return (
		<>
			<Form {...form}>
				<form
					onSubmit={form.handleSubmit(onSubmit)}
					className="space-y-8"
				>
					{/* 이메일 */}
					{/* <EmailCheckField /> */}

					{/* 이메일 인증코드 */}
					<FormField
						control={form.control}
						name="emailCode"
						render={({ field }) => (
							<FormItem>
								<FormLabel>이메일 인증코드</FormLabel>
								<div className="flex gap-2">
									<FormControl>
										<Input
											placeholder="이메일 인증코드를 입력해주세요."
											{...field}
										/>
									</FormControl>
									<Button
										className={
											emailCodeSendingStatus === "success" ? "hidden" : "block"
										}
										// onClick={onSendEmailCode}
										disabled={emailCodeSendingStatus === "sending"}
										type="button"
									>
										{emailCodeSendingStatus === "sending" ? "인증코드 전송중" : "인증코드 전송"}
									</Button>
									<Button
										className={
											emailCodeSendingStatus !== "success" ? "hidden" : "block"
										}
										onClick={onCheckEmailCode}
										type="button"
									>
										인증코드 확인
									</Button>
								</div>
								<FormMessage />
								{!errors.emailCode && emailCodeSendingStatus === "success" && (
									<p className="text-sm font-medium text-stone-500">
										인증코드가 전송되었습니다. 메일이 오지 않았다면 스팸메일함을
										확인해주세요.
									</p>
								)}
								{!errors.emailCode && emailCodeSendingStatus === "error" && (
									<p className="text-sm font-medium text-destructive">
										인증코드 전송 중 오류가 발생했습니다. 다시 시도해주세요.
									</p>
								)}
							</FormItem>
						)}
					/>

					<FormField
						control={form.control}
						name="nickname"
						render={({ field }) => (
							<FormItem>
								<FormLabel>닉네임</FormLabel>
								<FormControl>
									<Input
										placeholder="닉네임을 입력해주세요."
										{...field}
									/>
								</FormControl>
								<FormDescription>
									닉네임은 2자 이상 12자 미만으로 입력해주세요.
								</FormDescription>
								<FormMessage />
							</FormItem>
						)}
					/>

					{/* 비밀번호 */}
					<FormField
						control={form.control}
						name="password"
						render={({ field }) => (
							<FormItem>
								<FormLabel>비밀번호</FormLabel>
								<FormControl>
									<Input
										type="password"
										placeholder="비밀번호는 영문과 숫자를 포함하여 8~20자로 입력해 주세요."
										{...field}
									/>
								</FormControl>
								<FormMessage />
							</FormItem>
						)}
					/>

					{/* 비밀번호 확인 */}
					<FormField
						control={form.control}
						name="passwordCheck"
						render={({ field }) => (
							<FormItem>
								<FormLabel>비밀번호 확인</FormLabel>
								<FormControl>
									<Input
										type="password"
										placeholder="비밀번호를 다시 입력해주세요."
										{...field}
									/>
								</FormControl>
								<FormMessage />
							</FormItem>
						)}
					/>

					{/* 약관 동의 */}
					<FormField
						control={form.control}
						name="termsOfService"
						render={({ field }) => {
							const { value, ...fieldProps } = field;
							return (
								<FormItem className="flex flex-col w-full gap-2 space-y-0">
									<div className="flex justify-between">
										<FormLabel>
											이용 약관 동의{" "}
											<span className="text-red-400">(필수)</span>
										</FormLabel>
										<FormControl>
											<input
												type="checkbox"
												{...fieldProps}
											/>
										</FormControl>
									</div>
									<FormMessage />
								</FormItem>
							);
						}}
					/>
					<FormField
						control={form.control}
						name="privacyPolicy"
						render={({ field }) => {
							const { value, ...fieldProps } = field;
							return (
								<FormItem className="flex flex-col w-full gap-2 space-y-0">
									<div className="flex justify-between">
										<FormLabel>
											개인정보 수집 및 이용 동의{" "}
											<span className="text-red-400">(필수)</span>
										</FormLabel>
										<FormControl>
											<input
												type="checkbox"
												{...fieldProps}
											/>
										</FormControl>
									</div>
									<FormMessage />
								</FormItem>
							);
						}}
					/>
					<FormField
						control={form.control}
						name="marketingConsent"
						render={({ field }) => {
							const { value, ...fieldProps } = field;
							return (
								<FormItem className="flex w-full justify-between gap-2 items-center space-y-0">
									<FormLabel>마케팅 알림 수신 동의</FormLabel>
									<FormControl>
										<input
											type="checkbox"
											{...fieldProps}
										/>
									</FormControl>
									<FormMessage />
								</FormItem>
							);
						}}
					/>

					<Button className="w-full">회원가입</Button>
				</form>
			</Form>
		</>
	);
};

export default PrevSignUpForm;