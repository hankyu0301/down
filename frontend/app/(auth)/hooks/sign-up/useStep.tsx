"use client";
import { useState } from "react";
import { EmailCheckField, EmailCodeField, BasicUserInfoField } from "@/app/(auth)/components/sign-up";

// type SignUpStep = "이메일중복확인" | "이메일인증" | "기본회원정보입력" | "추가회원정보입력";
type SignUpStep = "이메일중복확인" | "이메일인증" | "기본회원정보입력"

export const useStep = (initialStep: SignUpStep) => {
  const [currentStep, setCurrentStep] = useState(initialStep);

  const setStep = (nextStep: SignUpStep) => {
    setCurrentStep(nextStep);
  };

  const stepComponents = {
    이메일중복확인: EmailCheckField,
    이메일인증: EmailCodeField,
    기본회원정보입력: BasicUserInfoField,
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
