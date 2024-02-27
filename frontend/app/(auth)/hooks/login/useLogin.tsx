"use client";
import { useRouter } from "next/navigation";

import { useAuth } from "@/components/providers/AuthProvider";

import { postUserLogin } from "@/api/login";

import { setLocalStorage } from "@/lib/localstorage";
import { parseJwt } from "@/lib/parseJwt";
import { ToastError, ToastSuccess } from "@/lib/toastifyAlert";

import {
	USER_ID_LOCALSTORAGE_KEY,
	USER_TOKEN_LOCALSTORAGE_KEY,
} from "@/constants/localstorage";

export const useLogin = () => {
	const router = useRouter();
	const { setAuthInfo } = useAuth();

	const login = async (email: string, password: string) => {
		try {
			const result = await postUserLogin(email, password);
			if (result.success) {
				console.log("성공", result.data);
				const userToken: string = await result.data.accessToken;
				const userId = parseJwt(userToken).id;

				setLocalStorage(USER_ID_LOCALSTORAGE_KEY, userId);
				setLocalStorage(USER_TOKEN_LOCALSTORAGE_KEY, userToken);

				setAuthInfo({ userId, userToken });

				ToastSuccess("로그인 성공!");
				router.push("/");
			} else {
				ToastError(result.message);
			}
		} catch (error) {}
	};
	return { login };
};
