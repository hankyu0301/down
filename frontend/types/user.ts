import { SportType } from "./sport";

export type AuthData = {
	userId: number;
	userToken: string;
};

export interface UserProfile {
	id: number;
	email: string;
	nickName: string;
	userName: string;
}
