"use client";
import React, {
	useContext,
	createContext,
	ReactNode,
	useMemo,
	useState,
} from "react";

export type UserEmailInfoType = {
	email: string;
	emailCode: string;
};

// useState에서 반환하는 타입과 일치하도록 setUserEmailInfo 타입 조정
interface SignupContextValue {
	userEmailInfo: UserEmailInfoType;
	setUserEmailInfo: React.Dispatch<React.SetStateAction<UserEmailInfoType>>;
}

const SignupContext = createContext<SignupContextValue | null>(null);

const SignupContextProvider = ({ children }: { children: ReactNode }) => {
	const [userEmailInfo, setUserEmailInfo] = useState<UserEmailInfoType>({
		email: "",
		emailCode: "",
	});

	const contextValue = useMemo(
		() => ({ userEmailInfo, setUserEmailInfo }),
		[userEmailInfo, setUserEmailInfo]
	);

	return (
		<SignupContext.Provider value={contextValue}>
			{children}
		</SignupContext.Provider>
	);
};

const useSignupContext = () => {
	const ctx = useContext(SignupContext);
	if (!ctx)
		throw new Error(
			"SignupContext를 찾을 수 없습니다. SignupContextProvider 내부에서 사용해야 합니다."
		);
	return ctx;
};

export { SignupContextProvider, useSignupContext };
