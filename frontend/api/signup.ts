import instance from "@/lib/axios/instance";

export const checkEmailDuplication = async (email: string) => {
  const response = await instance.post("/user/join/check-email", { email });
  return response.data;
};