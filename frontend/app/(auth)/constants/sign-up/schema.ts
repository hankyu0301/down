import { z } from "zod";

export const emailCheckFieldSchema = z.object({
	email: z
		.string({ required_error: "이메일을 입력해주세요." })
		.email({ message: "유효하지 않은 이메일 형식입니다." }),
});

export const signUpSchema = z
	.object({
		email: z
			.string()
			.min(1, "이메일을 입력해주세요.")
			.email({ message: "유효하지 않은 이메일 형식입니다." }),
		emailCode: z
			.string()
			.min(1, "이메일 인증코드를 입력해주세요.")
			.length(6, { message: "인증코드를 올바르게 입력해주세요." }),
		nickname: z
			.string({ required_error: "닉네임을 입력해주세요." })
			.min(2, "닉네임은 최소 2자 이상이어야 합니다.")
			.max(12, "닉네임은 12자를 초과할 수 없습니다."),
		password: z
			.string({ required_error: "비밀번호를 입력해주세요." })
			.min(1, "비밀번호를 입력해주세요.")
			.refine(
				(value) => /^(?=.*[a-zA-Z])(?=.*\d).{8,20}$/.test(value),
				"영문과 숫자를 포함하여 8~20자로 입력해 주세요."
			),
		passwordCheck: z
			.string({ required_error: "비밀번호 확인을 입력해주세요." })
			.min(1, "비밀번호 확인을 입력해주세요."),
		termsOfService: z.boolean({
			required_error: "이용약관에 동의는 필수입니다.",
		}),
		privacyPolicy: z.boolean({
			required_error: "개인정보 수집 및 이용 동의는 필수입니다.",
		}),
		marketingConsent: z.boolean().optional(),
	})
	.refine((data) => data.password === data.passwordCheck, {
		message: "비밀번호와 비밀번호 확인이 일치하지 않습니다.",
		path: ["passwordCheck"],
	});
