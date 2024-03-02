import axios from "axios";
import instance from "@/lib/axios/instance";

export const postUserLogin = async (email: string, password: string) => {
	try {
		const response = await instance.post("/user/login", { email, password });
		return response.data;
	} catch (error) {
		if (axios.isAxiosError(error)) {
			return error.response?.data;
		} else {
			console.log(
				"로그인 중 다음 문제가 발생하였습니다. 다시 시도해주세요.",
				error
			);
			return new Error("로그인 중 문제가 발생하였습니다. 다시 시도해주세요.");
		}
	}
};

export const getKakaoLogin = async () => {};

export const getLogout = async () => {
	try {
		const response = await instance.get("/user/login/logout");
		return response.data;
	} catch (error) {
		console.log(
			"로그아웃 중 다음 문제가 발생하였습니다. 다시 시도해주세요.",
			error
		);
		return new Error("로그아웃 중 문제가 발생하였습니다. 다시 시도해주세요.");
	}
};
