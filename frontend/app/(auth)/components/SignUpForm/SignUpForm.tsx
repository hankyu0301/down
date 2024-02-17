"use client";
import { useState } from "react";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { signUpSchema } from "../../constants/schema";

import { checkEmailDuplication } from "@/api/signup";

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

type EmailCheck = {
	success: boolean;
	checkedEmail: string;
	message: string;
};

const SignUpForm = () => {
	const [emailCheck, setEmailCheck] = useState<EmailCheck | null>(null);
	const [sentEmailCode, setSentEmailCode] = useState(false);

	const form = useForm<z.infer<typeof signUpSchema>>({
		resolver: zodResolver(signUpSchema),
	});
	const { formState: { errors } } = form;

	// 이메일 중복검사
	const onCheckEmailDuplication = async () => {
		const input = await form.trigger("email");
		if (!input) return;

		const email = form.getValues("email");
		const response = await checkEmailDuplication(email);

		console.log("response", response);

		const success = response.success;
		const checkedEmail = response.data.checkedEmail;
		const message = response.message;

		setEmailCheck({ success, checkedEmail, message });
	};

	// 이메일 인증코드 전송
	const onSendEmailCode = async () => {
		const email = await form.trigger("email");
		const emailCode = await form.trigger("emailCode");

		if (email && emailCode) {
			try {
				setSentEmailCode(true);
				// 이메일 인증코드 전송 로직
			} catch (error) {
				setSentEmailCode(false);
			}
		}
	}

	// 이메일 인증코드 확인
	const onCheckEmailCode = async () => {
		const email = await form.trigger("email");
		const emailCode = await form.trigger("emailCode");
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
					<FormField
						control={form.control}
						name="email"
						render={({ field }) => (
							<FormItem>
								<FormLabel>이메일</FormLabel>
								<FormControl>
									<Input
										placeholder="email@example.com"
										{...field}
										onBlur={onCheckEmailDuplication}
									/>
								</FormControl>
								<FormMessage />
								{!errors.email && emailCheck && (
									<p className="text-sm font-medium text-stone-500">{emailCheck.message}</p>
								)}
							</FormItem>
						)}
					/>

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
									<Button type="button">
										{sentEmailCode ? "인증코드 확인" : "인증코드 전송"}
									</Button>
								</div>
								<FormMessage />
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
										placeholder="비밀번호를 입력해주세요."
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

					<Button
						className="w-full"
						type="button"
					>
						회원가입
					</Button>
				</form>
			</Form>
		</>
	);
};

export default SignUpForm;
