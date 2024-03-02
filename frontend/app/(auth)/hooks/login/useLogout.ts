import { useRouter } from "next/navigation";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { deleteCookie } from "cookies-next";

import { getLogout } from "@/api/login";

import { ToastError, ToastSuccess } from "@/lib/toastifyAlert";

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
			ToastSuccess("로그아웃 되었습니다.");
			router.push("/");
		},
		onError: () => {
			ToastError("로그아웃 중 문제가 발생하였습니다. 다시 시도해주세요.");
		},
	});
	return { logout };
};
