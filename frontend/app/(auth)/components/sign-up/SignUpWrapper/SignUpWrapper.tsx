"use client";
import { useStep } from "@/app/(auth)/hooks/sign-up/useStep";
import { SignupContextProvider } from "@/app/(auth)/contexts/sign-up/SignUpContext";

const SignUpWrapper = () => {
  const INITIAL_STEP = "이메일중복확인";
  const { onNext, CurrentComponent } = useStep(INITIAL_STEP);
  return (
    <SignupContextProvider>
      {CurrentComponent && <CurrentComponent onNext={onNext} />}
    </SignupContextProvider>
  )
}

export default SignUpWrapper