import axios from "axios";
import instance from "@/lib/axios/instance";

export const getUserProfile = async (userId: number) => {
	try {
		const response = await instance.get(`/users/${userId}`);
		return response.data.data;
	} catch (error) {
		if (axios.isAxiosError(error)) {
			return error.response?.data;
		} else {
			console.log(
				"회원정보를 가져오는 중 다음 문제가 발생하였습니다. 다시 시도해주세요.",
				error
			);
			return new Error(
				"회원정보를 가져오는 중 문제가 발생하였습니다. 다시 시도해주세요."
			);
		}
	}
};
