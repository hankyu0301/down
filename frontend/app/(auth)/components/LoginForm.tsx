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

const loginSchema = z.object({
	email: z
		.string({ required_error: "이메일을 입력해주세요." })
		.email({ message: "유효하지 않은 이메일 형식입니다." }),
	password: z.string({ required_error: "비밀번호를 입력해주세요." }),
	rememberMe: z.boolean().optional(),
});

const LoginForm = () => {
	const form = useForm<z.infer<typeof loginSchema>>({
		resolver: zodResolver(loginSchema),
	});

	const onSubmit = async () => {
		// 로그인 폼 전송
	};

	return (
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
							<div className="flex gap-2">
								<FormControl>
									<Input
										placeholder="이메일을 입력해주세요."
										{...field}
									/>
								</FormControl>
							</div>
							<FormMessage />
						</FormItem>
					)}
				/>

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

				<div className="flex items-center justify-between">
          <FormField
						control={form.control}
						name="rememberMe"
						render={({ field }) => {
							const { value, ...fieldProps } = field;
							return (
								<FormItem className="flex gap-2 items-center space-y-0">
									<FormLabel>로그인 상태 유지하기</FormLabel>
									<FormControl>
                    <input
											type="checkbox"
											{...fieldProps} // value 속성을 제외한 나머지 속성들을 적용
										/>
									</FormControl>
									<FormMessage />
								</FormItem>
							);
            }}
					/>
					<button type="button" className="text-sm text-stone-500">아이디/비밀번호 찾기</button>
				</div>

				<Button
					className="w-full"
					type="submit"
				>
					로그인
				</Button>
			</form>
		</Form>
	);
};

export default LoginForm;
