"use client";
import Link from "next/link";

import NavLinks from "./NavLinks";

const Header = () => {
	return (
		<header className="sticky top-0 z-50 w-screen h-14 flex items-center bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
			<div className="container flex justify-between items-center">
				<Link href="/">
					<button className="font-semibold text-xl">로고</button>
				</Link>
				<nav>
					<NavLinks />
				</nav>
			</div>
		</header>
	);
};

export default Header;
