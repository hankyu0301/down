import axios, { AxiosError } from "axios";
import { getLocalStorage } from "../localstorage";
import { USER_TOKEN_LOCALSTORAGE_KEY } from "@/constants/localstorage";

// export const BASE_URL = `${
// 	process.env.NODE_ENV === "production"
// 		? process.env.PROD_SERVER
// 		: process.env.NEXT_PUBLIC_API_URL
// }`;

export type ErrorResponse = {
	message: string;
};

const instance = axios.create({
	baseURL: process.env.NEXT_PUBLIC_API_URL,
	headers: {
		"Content-Type": "application/json;charset=UTF-8",
		"Access-Control-Allow-Origin": "*",
	},
});

instance.interceptors.request.use((config) => {
	const token = getLocalStorage(USER_TOKEN_LOCALSTORAGE_KEY);

	if (token) {
		config.headers.Authorization = `Bearer ${token}`;
	}

	return config;
});

// Refresh token으로 access token 요청하는 로직
// instance.interceptors.response.use(
// 	(response) => response,
// 	async (error) => {
// 		const originalRequest = error.config;

// 		if (error.response) {
// 			if (
// 				error.response.status === 401 &&
// 				error.response.data.message === "Token"
// 			) {
// 				if (!originalRequest.shouldRetry) {
// 					originalRequest.shouldRetry = true;
// 					const refreshToken = getCookie("refresh_token");
// 					try {
// 						// refresh token으로 새로운 access token을 발급받는 API 호출
// 						const { data } = await instance.post("/auth/reissue", {
// 							refreshToken,
// 						});
// 						// 새로 받은 access token으로 기본 header 설정
// 						instance.defaults.headers.common.Authorization = `Bearer ${data.access_token}`;
// 						// 원래의 요청에도 새로운 access token 설정
// 						originalRequest.headers.Authorization = `Bearer ${data.access_token}`;

// 						// 원래의 요청 재시도
// 						return instance(originalRequest);
// 					} catch (reissueError) {
// 						// refresh token으로도 실패하면 로그인 페이지로 리다이렉트
// 						const axiosError = reissueError as AxiosError;

// 						if (
// 							axiosError.response?.status === 401 &&
// 							(axiosError.response.data as ErrorResponse).message ===
// 								"Expired Token"
// 						) {
// 							window.location.href = "/login";
// 						}
// 					}
// 				}
// 			} else {
// 				throw new Error(error.response.data.message);
// 			}
// 		}

// 		throw error;
// 	}
// );

export default instance;
