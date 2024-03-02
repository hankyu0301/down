import { useRouter } from "next/navigation";

import { postUserLogin } from "@/api/login";

import { setCookie } from "cookies-next";

import { ToastError, ToastSuccess } from "@/lib/toastifyAlert";

import {
	ACCESS_TOKEN_COOKIE_KEY,
	REFRESH_TOKEN_COOKIE_KEY,
} from "@/constants/cookie";
import { TOAST_MESSAGE } from "@/constants/toastMessage/login";

export const useLogin = () => {
	const router = useRouter();

	const login = async (email: string, password: string) => {
		try {
			const result = await postUserLogin(email, password);
			if (result.success) {
				// console.log("성공", result.data);
				const accessToken: string = await result.data.accessToken;
				const refreshToken: string = await result.data.refreshToken;

				setCookie(ACCESS_TOKEN_COOKIE_KEY, accessToken);
				setCookie(REFRESH_TOKEN_COOKIE_KEY, refreshToken);

				ToastSuccess(TOAST_MESSAGE.SUCCESS_LOGIN);
				router.push("/");
				router.refresh();
			} else {
				ToastError(result.message);
			}
		} catch (error) {
			console.log("로그인 실패", error);
			ToastError(TOAST_MESSAGE.FAIL_LOGIN);
		}
	};
	return login;
};
