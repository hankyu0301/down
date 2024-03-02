import { cookies } from "next/headers";
import { dehydrate, HydrationBoundary } from "@tanstack/react-query";
import { jwtDecode } from "jwt-decode";

import { getUserProfile } from "@/api/users";

import { getQueryClient } from "@/lib/query/getQueryClient";

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

export const getUserId = () => {
	const cookieStore = cookies();
	const cookie = cookieStore.get(ACCESS_TOKEN_COOKIE_KEY);
	if (!cookie) return null;

	const token = cookie?.value;
	const user: JwtPayload = jwtDecode(token);

	return user.id;
};

export const ProfileProvider = async ({
	children,
}: {
	children: React.ReactNode;
}) => {
	const userId = getUserId();
	if (!userId) return <>{children}</>;

	const queryClient = getQueryClient();
	await queryClient.prefetchQuery({
		queryKey: QUERY_KEYS.user.profile,
		queryFn: () => getUserProfile(userId),
		staleTime: Infinity,
	});

	const dehydratedState = dehydrate(queryClient);

	return (
		<HydrationBoundary state={dehydratedState}>{children}</HydrationBoundary>
	);
};
