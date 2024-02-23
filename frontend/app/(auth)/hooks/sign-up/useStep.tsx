"use client";
import { useState } from "react";
import { EmailCheckField, EmailCodeField } from "@/app/(auth)/components/sign-up";

// type SignUpStep = "이메일중복확인" | "이메일인증" | "비밀번호설정" | "초기회원정보";
type SignUpStep = "이메일중복확인" | "이메일인증"

export const useStep = (initialStep: SignUpStep) => {
  const [currentStep, setCurrentStep] = useState(initialStep);

  const setStep = (nextStep: SignUpStep) => {
    setCurrentStep(nextStep);
  };

  const stepComponents = {
    이메일중복확인: EmailCheckField,
    이메일인증: EmailCodeField
  };

  const getCurrentComponent = () => {
    return stepComponents[currentStep] || null;
  };

  const onNext = () => {
    const stepKeys: SignUpStep[] = Object.keys(stepComponents) as SignUpStep[];
    const currentStepIndex = stepKeys.indexOf(currentStep);
    const nextStep = stepKeys[currentStepIndex + 1];
    if (nextStep) {
      setCurrentStep(nextStep);
    }
  };

  const CurrentComponent = getCurrentComponent();

  return { currentStep, setStep, onNext, CurrentComponent };
};
