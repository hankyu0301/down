import { useQuery } from "@tanstack/react-query";

import { getUserProfile } from "@/api/users";
import { useAuth } from "@/components/providers/AuthProvider";

import { generateKeys } from "@/lib/query/generateKeys";

import { UserProfile } from "@/types/user";

import QUERY_KEYS from "@/constants/queryKeys";

export const useProfile = () => {
	const {
		authInfo: { userId, userToken },
	} = useAuth();

	const { data: user } = useQuery<UserProfile>({
		enabled: !!userToken,
		queryKey: generateKeys(QUERY_KEYS.user, userId, userToken),
		queryFn: () => getUserProfile(userId, userToken),
		staleTime: Infinity,
	});

	return { user };
};
