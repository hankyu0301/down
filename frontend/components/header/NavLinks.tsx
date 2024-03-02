import Link from "next/link";
import { Button } from "@/components/ui";
import useProfile from "@/hooks/user/useProfile";

const navLinks = [
	{ label: "모임", href: "/gathering" },
	{ label: "강습", href: "/lesson" },
	{ label: "중고거래", href: "/fleamarket" },
];

const NavLinks = () => {
	const user = useProfile();
	console.log("USER!!!", user);
	return (
		<ul className="flex gap-6 items-center">
			{navLinks.map((link) => (
				<li key={link.label}>
					<Link href={link.href}>{link.label}</Link>
				</li>
			))}
			{user ? (
				<Button variant="outline">{user.nickName} 님</Button>
			) : (
				<Link href="/login">
					<Button variant="outline">로그인</Button>
				</Link>
			)}
		</ul>
	);
};

export default NavLinks;
