"use client";
import React, { useContext, createContext, ReactNode, useMemo, useState, useEffect } from "react";

interface UserInfo {
  email: string;
  password: string;
  nickname: string;
  gender: "MALE" | "FEMALE" | null;
}

// useState에서 반환하는 타입과 일치하도록 setUserInfo 타입 조정
interface SignupContextValue {
  userInfo: UserInfo;
  setUserInfo: React.Dispatch<React.SetStateAction<UserInfo>>;
}

const SignupContext = createContext<SignupContextValue | null>(null);

const SignupContextProvider = ({ children }: { children: ReactNode }) => {
  const [userInfo, setUserInfo] = useState<UserInfo>({
    email: "",
    password: "",
    nickname: "",
    gender: null,
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
