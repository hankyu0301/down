"use client";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { FieldValues } from "react-hook-form";

import { useSignupContext } from "@/app/(auth)/contexts/sign-up/SignUpContext";
import { FormFieldWrapper } from "@/app/(auth)/components/sign-up";

import { useCommonForm } from "@/app/(auth)/hooks/sign-up/useCommonForm";
import { basicUserInfoFieldSchema } from "@/app/(auth)/constants/sign-up/schema";

import { postCheckNickname, postSignUp } from "@/api/signup";

import { NickNameCheckResponse } from "@/app/(auth)/types/signup";

import { ToastError, ToastSuccess } from "@/lib/toastifyAlert";
import { TOAST_MESSAGE } from "@/constants/toastMessage/signup";

import {
	FormControl,
	FormField,
	FormItem,
	FormLabel,
	FormMessage,
	RadioGroup,
	RadioGroupItem,
	Input,
	Button,
	Checkbox,
} from "@/components/ui";
import clsx from "clsx";

const BasicUserInfoField = () => {
	const router = useRouter();
	const { userEmailInfo } = useSignupContext();
	const { method } = useCommonForm({
		schema: basicUserInfoFieldSchema,
		checkMode: "onSubmit",
	});
	const [nickNameCheckResponse, setNickNameCheckResponse] =
		useState<NickNameCheckResponse | null>(null);

	const password = method.watch("password");
	const passwordCheck = method.watch("passwordCheck");

	useEffect(() => {
		if (password && passwordCheck) {
			if (password !== passwordCheck) {
				method.setError("passwordCheck", {
					type: "onChange",
					message: "비밀번호와 비밀번호 확인이 일치하지 않습니다.",
				});
			} else {
				method.clearErrors("passwordCheck");
			}
		}
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [password, passwordCheck]);

	const onCheckNickname = async (nickname: string) => {
		if (!nickname) {
			method.setError("nickname", { message: "닉네임을 입력해주세요." });
			setNickNameCheckResponse(null);
			return;
		}

		const result = await postCheckNickname(nickname);
		setNickNameCheckResponse(result);
	};

	const onSubmit = async (value: FieldValues) => {
		if (!userEmailInfo.email || !userEmailInfo.emailCode) return;
		if (!nickNameCheckResponse || !nickNameCheckResponse.success) return;

		const newUserData = {
			email: userEmailInfo.email,
			code: userEmailInfo.emailCode,
			password: value.password,
			userName: value.username,
			nickName: value.nickname,
			gender: value.gender,
			birth: value.birth,
			termsAgree: value.termsAgree,
		};

		const result = await postSignUp(newUserData);

		if (result.success) {
			ToastSuccess(TOAST_MESSAGE.SUCCESS_SIGN_UP);
			router.push("/login");
		} else {
			ToastError(TOAST_MESSAGE.FAIL_SIGN_UP);
		}
	};

	return (
		<FormFieldWrapper
			method={method}
			onSubmit={onSubmit}
		>
			<FormField
				control={method.control}
				name="username"
				render={({ field }) => (
					<FormItem>
						<FormLabel>이름</FormLabel>
						<FormControl>
							<Input
								placeholder="이름을 입력해주세요."
								{...field}
							/>
						</FormControl>
						<FormMessage />
					</FormItem>
				)}
			/>

			<FormField
				control={method.control}
				name="nickname"
				render={({ field }) => (
					<FormItem>
						<FormLabel>닉네임</FormLabel>
						<div className="flex gap-2">
							<FormControl>
								<Input
									placeholder="닉네임을 입력해주세요."
									{...field}
								/>
							</FormControl>
							<Button
								type="button"
								onClick={() => onCheckNickname(field.value)}
							>
								중복확인
							</Button>
						</div>
						<FormMessage />
						{nickNameCheckResponse && (
							<p
								className={clsx(
									"text-sm font-medium",
									nickNameCheckResponse.success
										? "text-stone-500"
										: "text-destructive"
								)}
							>
								{nickNameCheckResponse.message}
							</p>
						)}
					</FormItem>
				)}
			/>

			<FormField
				control={method.control}
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

			<FormField
				control={method.control}
				name="passwordCheck"
				render={({ field }) => (
					<FormItem>
						<FormLabel>비밀번호 확인</FormLabel>
						<FormControl>
							<Input
								type="password"
								placeholder="비밀번호를 다시 입력해주세요."
								{...field}
								onChange={field.onChange}
							/>
						</FormControl>
						<FormMessage />
					</FormItem>
				)}
			/>

			<FormField
				control={method.control}
				name="gender"
				render={({ field }) => (
					<FormItem>
						<FormLabel>성별</FormLabel>
						<FormControl>
							<RadioGroup
								onValueChange={field.onChange}
								className="flex space-x-2"
							>
								<FormItem className="flex items-center space-x-2 space-y-0">
									<FormControl>
										<RadioGroupItem value="male" />
									</FormControl>
									<FormLabel className="font-normal">남성</FormLabel>
								</FormItem>
								<FormItem className="flex items-center space-x-2 space-y-0">
									<FormControl>
										<RadioGroupItem value="female" />
									</FormControl>
									<FormLabel className="font-normal">여성</FormLabel>
								</FormItem>
							</RadioGroup>
						</FormControl>
						<FormMessage />
					</FormItem>
				)}
			/>

			<FormField
				control={method.control}
				name="birth"
				render={({ field }) => (
					<FormItem>
						<FormLabel>생년월일</FormLabel>
						<FormControl>
							<Input
								type="date"
								{...field}
							/>
						</FormControl>
						<FormMessage />
					</FormItem>
				)}
			/>

			<FormField
				control={method.control}
				name="termsAgree"
				render={({ field }) => {
					return (
						<FormItem className="flex flex-col w-full gap-2 space-y-0">
							<div className="flex justify-between">
								<FormLabel>
									이용약관, 개인정보 수집 및 이용 동의{" "}
									<span className="text-red-400">(필수)</span>
								</FormLabel>
								<FormControl>
									<Checkbox
										checked={field.value}
										onCheckedChange={field.onChange}
										{...field}
									/>
								</FormControl>
							</div>
							<FormMessage />
						</FormItem>
					);
				}}
			/>

			<Button className="w-full">회원가입</Button>
		</FormFieldWrapper>
	);
};

export default BasicUserInfoField;
