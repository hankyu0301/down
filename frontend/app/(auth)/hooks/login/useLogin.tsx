import { useRouter } from "next/navigation";

import { postUserLogin } from "@/api/login";

import { setCookie } from "@/lib/cookie";

import { ToastError, ToastSuccess } from "@/lib/toastifyAlert";

import {
	ACCESS_TOKEN_COOKIE_KEY,
	REFRESH_TOKEN_COOKIE_KEY,
} from "@/constants/cookie";

export const useLogin = () => {
	const router = useRouter();

	const login = async (email: string, password: string) => {
		try {
			const result = await postUserLogin(email, password);
			if (result.success) {
				console.log("성공", result.data);
				const accessToken: string = await result.data.accessToken;
				const refreshToken: string = await result.data.refreshToken;

				setCookie(ACCESS_TOKEN_COOKIE_KEY, accessToken, 1);
				setCookie(REFRESH_TOKEN_COOKIE_KEY, refreshToken, 1);

				ToastSuccess("로그인 성공!");
				router.push("/");
			} else {
				ToastError(result.message);
			}
		} catch (error) {
			console.log("로그인 실패", error);
			ToastError("로그인에 실패하였습니다. 다시 시도해주세요.");
		}
	};
	return { login };
};
