import { SignUpWrapper } from "@/app/(auth)/components/sign-up";

const SignUpPage = () => {
	return (
		<section className="w-full flex flex-col items-center justify-center">
			<div className="md:w-[32rem] w-full py-28 px-10 flex flex-col gap-8">
				<h1 className="font-semibold text-xl">회원가입 하기</h1>
				<SignUpWrapper />
			</div>
		</section>
	);
};

export default SignUpPage;
