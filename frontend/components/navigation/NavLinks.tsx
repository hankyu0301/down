import Link from "next/link";
import { Button } from "@/components/ui";

const navLinks = [
	{ label: "모임", href: "/gathering" },
	{ label: "강습", href: "/lesson" },
	{ label: "중고거래", href: "/fleamarket" },
  { label: "로그인/회원가입", href: "/login", button: true },
];

const NavLinks = () => {
  return (
    <ul className="flex gap-6 items-center">
      {navLinks.map((link) =>
        link.button ? (
          <li key={link.label}>
            <Link href={link.href}>
              <Button variant="outline">{link.label}</Button>
            </Link>
          </li>
        ) : (
          <li key={link.label}>
            <Link href={link.href}>{link.label}</Link>
          </li>
        )
      )}
    </ul>
  );
}

export default NavLinks