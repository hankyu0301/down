export interface LoginUserInfo {
	email: string;
	password: string;
	rememberMe: boolean;
}

export type LoginSuccessResponse = {
	success: boolean;
	data: { accessToken: string; refreshToken: string; tokenType: string };
	message: string;
};

export type LoginFailResponse = {
	success: boolean;
	data: string;
	message: string;
};

export type LoginResponse = LoginSuccessResponse | LoginFailResponse;
