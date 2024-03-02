import type { Metadata } from "next";
import "./globals.css";
import { Open_Sans } from "next/font/google";

import { cn } from "@/lib/cn";

import { QueryProvider } from "@/components/providers/QueryProvider";
import { ProfileProvider } from "@/components/providers/ProfileProvider";
import { ThemeProvider } from "@/components/providers/ThemeProvider";
import { ModalProvider } from "@/components/providers/ModalProvider";
import { ToastProvider } from "@/components/providers/ToastProvider";

import Header from "@/components/header/Header";
import Footer from "@/components/footer/Footer";

const font = Open_Sans({ subsets: ["latin"] });

export const metadata: Metadata = {
	title: "다운",
	description: "같이 운동할 사람이 필요하다면?",
};

export default function RootLayout({
	children,
}: Readonly<{
	children: React.ReactNode;
}>) {
	return (
		<html
			lang="ko"
			suppressHydrationWarning
		>
			<body className={cn(font.className, "bg-white")}>
				<QueryProvider>
					<ToastProvider>
						<ProfileProvider>
							<ThemeProvider
								attribute="class"
								defaultTheme="system"
								enableSystem
							>
								<ModalProvider />
								<div className="relative flex min-h-screen flex-col">
									<Header />
									<main className="flex-1">{children}</main>
									<Footer />
								</div>
							</ThemeProvider>
						</ProfileProvider>
					</ToastProvider>
				</QueryProvider>
			</body>
		</html>
	);
}
