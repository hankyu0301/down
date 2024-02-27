"use client";
import { createContext, useContext, useEffect, useMemo, useState } from "react";

export type AuthInfoType = {
	userId: number;
	userToken: string;
};

interface AuthContext {
	authInfo: AuthInfoType;
	isLoggedIn: boolean;
	setAuthInfo: React.Dispatch<React.SetStateAction<AuthInfoType>>;
}

const AuthContext = createContext<AuthContext | null>(null);

const AuthProvider = ({ children }: { children: React.ReactNode }) => {
	const [authInfo, setAuthInfo] = useState<AuthInfoType>({
		userId: -1,
		userToken: "",
	});

	const isLoggedIn = !!authInfo.userToken;

	useEffect(() => {
		const storedUserId = localStorage.getItem("down_user_id");
		const storedUserToken = localStorage.getItem("down_user_token");

		const userId = storedUserId ? parseInt(storedUserId) : -1;
		const userToken = storedUserToken || "";

		setAuthInfo({ userId, userToken });
	}, []);

	const contextValue = useMemo(
		() => ({ authInfo, isLoggedIn, setAuthInfo }),
		[authInfo, isLoggedIn, setAuthInfo]
	);

	return (
		<AuthContext.Provider value={contextValue}>{children}</AuthContext.Provider>
	);
};

const useAuth = () => {
	const ctx = useContext(AuthContext);
	if (!ctx)
		throw new Error(
			"AuthContext를 찾을 수 없습니다. AuthProvider 내부에서 사용해야 합니다."
		);
	return ctx;
};

export { AuthProvider, useAuth };
