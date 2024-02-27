import { z } from "zod";

export const loginSchema = z.object({
	email: z
		.string({ required_error: "이메일을 입력해주세요." })
		.email({ message: "유효하지 않은 이메일 형식입니다." }),
	password: z.string({ required_error: "비밀번호를 입력해주세요." }),
	rememberMe: z.boolean().optional(),
});
