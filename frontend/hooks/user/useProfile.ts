import { UserProfile } from "@/types/user";
import { useQuery } from "@tanstack/react-query";

import QUERY_KEYS from "@/constants/queryKeys";

const useProfile = () => {
	const { data: user } = useQuery<UserProfile | undefined>({
		queryKey: QUERY_KEYS.user.profile,
		staleTime: Infinity,
		retry: 1,
	});

	return user;
};
export default useProfile;
