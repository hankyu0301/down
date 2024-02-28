import { useQuery } from "@tanstack/react-query";
import { jwtDecode } from "jwt-decode";
import { getCookie } from "cookies-next";

import { getUserProfile } from "@/api/users";

import { generateKeys } from "@/lib/query/generateKeys";

import QUERY_KEYS from "@/constants/queryKeys";
import { ACCESS_TOKEN_COOKIE_KEY } from "@/constants/cookie";

interface JwtPayload {
	sub: string;
	role: string;
	iss: string;
	id: number;
	exp: number;
	iat: number;
	username: string;
}

export const useProfile = () => {
	const token = getCookie(ACCESS_TOKEN_COOKIE_KEY);
	const payload: JwtPayload = jwtDecode(token!);
	const userId = payload.id;

	const {
		data: user,
		isLoading,
		error,
	} = useQuery({
		queryKey: generateKeys(QUERY_KEYS.user, userId),
		queryFn: () => getUserProfile(userId),
		retry: 1,
		staleTime: Infinity,
		enabled: !!userId,
	});

	return user;
};
