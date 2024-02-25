import type { Metadata } from "next";
import "./globals.css";
import { Open_Sans } from "next/font/google";

import { cn } from "@/lib/utils";
import { ThemeProvider } from "@/components/providers/ThemeProvider";
import { ModalProvider } from "@/components/providers/ModalProvider";

import NavBar from "@/components/navigation/NavBar";
import Footer from "@/components/footer/Footer";
import ToastProvider from "@/components/providers/ToastProvider";

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
    <html lang="ko" suppressHydrationWarning>
      <body className={cn(font.className, "bg-white")}>
        <ThemeProvider
          attribute="class"
          defaultTheme="system"
          enableSystem
        >
          <ToastProvider>
          <ModalProvider />
            <div className="relative flex min-h-screen flex-col">
              <NavBar />
              <main className="flex-1">
                {children}
              </main>
              <Footer />
            </div>
          </ToastProvider>
        </ThemeProvider>
      </body>
    </html>
  );
}
