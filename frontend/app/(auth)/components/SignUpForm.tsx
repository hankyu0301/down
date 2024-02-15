"use client";

import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";

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

const signUpSchema = z
	.object({
		email: z
			.string({ required_error: "이메일을 입력해주세요." })
			.email({ message: "유효하지 않은 이메일 형식입니다." }),
		emailCode: z
			.string({ required_error: "이메일 인증코드를 입력해주세요." })
			.length(6, { message: "인증코드를 올바르게 입력해주세요." }),
		username: z
			.string({ required_error: "닉네임을 입력해주세요." })
			.min(2, { message: "닉네임은 최소 2자 이상이어야 합니다." })
			.max(12, { message: "닉네임은 12자를 초과할 수 없습니다." }),
		password: z
			.string({ required_error: "비밀번호를 입력해주세요." })
			.min(8, { message: "비밀번호는 최소 8자 이상이어야 합니다." }),
		confirmPassword: z.string({
			required_error: "비밀번호 확인을 입력해주세요.",
		}),
	})
	.refine((data) => data.password === data.confirmPassword, {
		message: "비밀번호와 비밀번호 확인이 일치하지 않습니다.",
		path: ["confirmPassword"],
	});

const SignUpForm = () => {
	const form = useForm<z.infer<typeof signUpSchema>>({
		resolver: zodResolver(signUpSchema),
  });

  const onSendEmailCode = async () => {
		const email = await form.trigger("email");

		if (email) {
			// 이메일 입력 후 '인증코드 전송' 버튼 클릭 시 인증코드 전송 로직 실행
		}
	};
  
	const onCheckEmailCode = async () => {
		const emailCode = await form.trigger("emailCode");

		if (emailCode) {
			// 이메일 인증코드 확인 로직 실행
		}
	};

	const onSubmit = async (values: z.infer<typeof signUpSchema>) => {
		// 회원가입 양식 제출 로직 실행
	};

	return (
		<Form {...form}>
			<form
				onSubmit={form.handleSubmit(onSubmit)}
				className="space-y-8"
			>
				{/* 이메일 */}
				<FormField
					control={form.control}
					name="email"
					render={({ field }) => (
						<FormItem>
              <FormLabel>이메일</FormLabel>
              <div className="flex gap-2">
                <FormControl>
                  <Input
                    placeholder="이메일을 입력해주세요."
                    {...field}
                  />
                </FormControl>
                <Button type="button" onClick={onSendEmailCode}>인증코드 전송</Button>
              </div>
							<FormMessage />
						</FormItem>
					)}
				/>

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
                    type="text"
                    placeholder="인증코드를 입력해주세요."
                    {...field}
                  />
                </FormControl>
                <Button type="button" onClick={onCheckEmailCode}>인증코드 확인</Button>
              </div>
							<FormMessage />
						</FormItem>
					)}
        />

				{/* 닉네임 */}
				<FormField
					control={form.control}
					name="username"
					render={({ field }) => (
						<FormItem>
							<FormLabel>닉네임</FormLabel>
							<FormControl>
								<Input
									placeholder="닉네임을 입력해주세요."
									{...field}
								/>
              </FormControl>
              <FormDescription>닉네임은 2자 이상 12자 미만으로 입력해주세요.</FormDescription>
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
					name="confirmPassword"
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

				<Button type="submit">가입하기</Button>
			</form>
		</Form>
	);
};

export default SignUpForm;
