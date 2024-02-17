import { useState } from "react";
import { SignUpForm } from "../components";

// type SignUpStep = "회원가입" | "추가정보입력";
type SignUpStep = "회원가입";

export const useStep = (initialStep: SignUpStep) => {
  const [currentStep, setCurrentStep] = useState(initialStep);

  const setStep = (nextStep: SignUpStep) => {
    setCurrentStep(nextStep);
  };

  const stepComponents = {
    회원가입: SignUpForm,
    // 추가정보입력: 
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
