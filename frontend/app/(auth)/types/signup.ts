// 회원가입 양식필드 공통
export interface FieldProps {
  onNext: () => void;
};

export type EmailCodeSendingStatus = "sending" | "success" | "error" | null;

// EmailCheckField
export type EmailCheckResponse = {
	success: boolean;
	data: { checkedEmail: string, available: boolean; } | { errorMessage: string; };
	message: string;
};

// EmailCodeField
export type EmailCodeResponse = {
	success: boolean;
	data: { checkedEmail: string; result: boolean } | { errorMessage: string };
	message: string;
};

// api/signup.ts
export interface NewUserInfo {
  email: string;
  password: string;
  nickName: string;
  gender: "male" | "female";
  birth: string;
  userName: string;
  code: string;
  termsAgree: boolean;
}