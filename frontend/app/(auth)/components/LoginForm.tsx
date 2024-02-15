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

const loginSchema = z
  .object({
    email: z
      .string({ required_error: "이메일을 입력해주세요." })
      .email({ message: "유효하지 않은 이메일 형식입니다." }),
    password: z
      .string({ required_error: "비밀번호를 입력해주세요." })
  });

const LoginForm = () => {
  const form = useForm<z.infer<typeof loginSchema>>({
		resolver: zodResolver(loginSchema),
  });

  const onSubmit = async () => {
    // 로그인 폼 전송
  }

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
        
				<Button type="submit">로그인</Button>
			</form>
		</Form>
  )
}

export default LoginForm