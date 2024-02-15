import Link from "next/link";

import LoginForm from "@/app/(auth)/components/LoginForm";
import { Button } from "@/components/ui";

const LoginPage = () => {
	return (
		<section className="w-full flex flex-col items-center justify-center">
			<div className="md:w-[32rem] w-full py-28 px-10 flex flex-col gap-8">
				<h1 className="font-semibold text-xl">로그인 하기</h1>
				<LoginForm />
				<div className="flex flex-col gap-2 mt-10">
					<p className="text-sm text-stone-500">아직 다운의 회원이 아니신가요?</p>
					<Link href="/sign-up" className="py-1">
						<Button variant="outline">회원가입</Button>
					</Link>
				</div>
			</div>
		</section>
	);
};

export default LoginPage;
