import { useRouter } from "next/navigation";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { deleteCookie } from "cookies-next";

import { getLogout } from "@/api/login";

import { ToastError, ToastSuccess } from "@/lib/toastifyAlert";
import { TOAST_MESSAGE } from "@/constants/toastMessage/login";

import QUERY_KEYS from "@/constants/queryKeys";
import {
	ACCESS_TOKEN_COOKIE_KEY,
	REFRESH_TOKEN_COOKIE_KEY,
} from "@/constants/cookie";

export const useLogout = () => {
	const router = useRouter();
	const queryClient = useQueryClient();

	const { mutateAsync: logout } = useMutation({
		mutationFn: () => getLogout(),
		onSuccess: () => {
			void queryClient.removeQueries({ queryKey: QUERY_KEYS.user.profile });
			deleteCookie(ACCESS_TOKEN_COOKIE_KEY);
			deleteCookie(REFRESH_TOKEN_COOKIE_KEY);
			ToastSuccess(TOAST_MESSAGE.SUCCESS_LOGOUT);
			router.push("/");
		},
		onError: () => {
			ToastError(TOAST_MESSAGE.FAIL_LOGOUT);
		},
	});
	return logout;
};
