"use client";
import React, { useContext, createContext, ReactNode, useMemo, useState, useEffect } from "react";

export type UserInfoType = {
  email: string;
  emailCode: string;
}

// useState에서 반환하는 타입과 일치하도록 setUserInfo 타입 조정
interface SignupContextValue {
  userInfo: UserInfoType;
  setUserInfo: React.Dispatch<React.SetStateAction<UserInfoType>>;
}

const SignupContext = createContext<SignupContextValue | null>(null);

const SignupContextProvider = ({ children }: { children: ReactNode }) => {
  const [userInfo, setUserInfo] = useState<UserInfoType>({
    email: "",
    emailCode: "",
  });

  useEffect(() => {
    console.log("SignUpContext userInfo", userInfo);
  }, [userInfo])

  const contextValue = useMemo(() => ({ userInfo, setUserInfo }), [userInfo, setUserInfo]);

  return <SignupContext.Provider value={contextValue}>{children}</SignupContext.Provider>;
};

const useSignupContext = () => {
  const ctx = useContext(SignupContext);
  if (!ctx) throw new Error("SignupContext를 찾을 수 없습니다. SignupContextProvider 내부에서 사용해야 합니다.");
  return ctx;
};

export { SignupContextProvider, useSignupContext };
