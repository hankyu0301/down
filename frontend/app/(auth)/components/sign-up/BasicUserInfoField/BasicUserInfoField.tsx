"use client";
import { useRouter } from "next/navigation";
import { FieldValues } from "react-hook-form";

import { useSignupContext } from "@/app/(auth)/contexts/sign-up/SignUpContext";
import { FormFieldWrapper } from "@/app/(auth)/components/sign-up";

import { useCommonForm } from "@/app/(auth)/hooks/sign-up/useCommonForm";
import { basicUserInfoFieldSchema } from "@/app/(auth)/constants/sign-up/schema";

import { postSignUp } from "@/api/signup";

import { ToastError, ToastSuccess } from "@/lib/toastifyAlert";

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
import { useEffect } from "react";

const BasicUserInfoField = () => {
	const router = useRouter();
	const { userEmailInfo } = useSignupContext();
	const { method } = useCommonForm({
		schema: basicUserInfoFieldSchema,
		checkMode: "onSubmit",
	});

	const password = method.watch("password");
	const passwordCheck = method.watch("passwordCheck");

	useEffect(() => {
		if (password && passwordCheck) {
			if (password !== passwordCheck) {
				method.setError("passwordCheck", {
					type: "onChange",
					message: "비밀번호와 비밀번호 확인이 일치하지 않습니다."
				});
			} else {
				method.clearErrors("passwordCheck");
			}
		}
	// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [password, passwordCheck])

	const onSubmit = async (value: FieldValues) => {
		console.log("userInfo", userEmailInfo);
		if (!userEmailInfo.email || !userEmailInfo.emailCode) return;
		
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
			ToastSuccess("회원가입이 완료되었습니다.")
			router.push("/login")
		} else {
			ToastError("회원가입 중 에러가 발생했습니다. 다시 시도해주세요.")
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
						<FormControl>
							<Input
								placeholder="닉네임을 입력해주세요."
								{...field}
							/>
						</FormControl>
						<FormMessage />
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
