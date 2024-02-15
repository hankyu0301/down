import SignUpForm from "@/app/(auth)/components/SignUpForm";

const SignUpPage = () => {
	return (
    <section className="max-w-96 mx-auto">
      <h1 className="font-semibold text-xl">회원가입</h1>
      <SignUpForm />
    </section>
	);
};

export default SignUpPage;
