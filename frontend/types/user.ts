import { SportType } from "./sport";

export type AuthData = {
	userId: number;
	userToken: string;
};

export type UserProfileType = UserProfile | "Unauthorized";

export interface UserProfile {
	id: number;
	email: string;
	nickName: string;
	userName: string;
}
