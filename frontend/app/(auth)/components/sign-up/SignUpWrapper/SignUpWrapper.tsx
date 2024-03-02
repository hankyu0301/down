"use client";
import { useStep } from "@/app/(auth)/hooks/sign-up/useStep";
import { SignupContextProvider } from "@/app/(auth)/contexts/sign-up/SignUpContext";
import { useProfile } from "@/hooks/user/useProfile";
import { useRouter } from "next/navigation";

const SignUpWrapper = () => {
	const router = useRouter();
	const INITIAL_STEP = "이메일중복확인";
	const { onNext, CurrentComponent } = useStep(INITIAL_STEP);

	const user = useProfile();

	if (user) {
		router.push("/");
	}

	return (
		<SignupContextProvider>
			{CurrentComponent && <CurrentComponent onNext={onNext} />}
		</SignupContextProvider>
	);
};

export default SignUpWrapper;
