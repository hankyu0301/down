"use client";

import "react-toastify/dist/ReactToastify.css";
import "@/app/globals.css";
import { ToastContainer } from "react-toastify";

interface ToastProviderProps {
	children: React.ReactNode;
}

export const ToastProvider = ({ children }: ToastProviderProps) => {
	return (
		<>
			{children}
			<ToastContainer />
		</>
	);
};
