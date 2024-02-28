import { useQuery } from "@tanstack/react-query";

import { getUserProfile } from "@/api/users";

import { generateKeys } from "@/lib/query/generateKeys";
import { getCookie } from "@/lib/cookie";
import { parseJwt } from "@/lib/parseJwt";

import QUERY_KEYS from "@/constants/queryKeys";
import { ACCESS_TOKEN_COOKIE_KEY } from "@/constants/cookie";

export const useProfile = () => {
	const token = getCookie(ACCESS_TOKEN_COOKIE_KEY);
	const userId = token ? parseJwt(token).id : null;

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
