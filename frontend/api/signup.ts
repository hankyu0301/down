import axios from "axios";
import instance from "@/lib/axios/instance";

export const postEmailCheck = async (email: string) => {
  try {
    const response = await instance.post("/users/email/check", { email });
    return response.data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      // console.log("postEmailCheck Api response", error.response?.data);
      return error.response?.data;
    } else {
      console.log("Different emailCheck Error than axios", error);
      return new Error("Different emailCheck Error than axios");
    }
  }
};

export const postSendEmailCode = async (email: string) => {
  try {
    const response = await instance.post("/users/email/send", { email });
    return response.data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      return error.response?.data;
    } else {
      console.log("Different sendEmailCode Error than axios", error);
      return new Error("Different sendEmailCode Error than axios");
    }
  }
};

export const postCheckEmailCode = async (email: string, code: string) => {
  try {
    const response = await instance.post("/users/email/verify", { email, code });
    console.log(response.data);
    return response.data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      return error.response?.data;
    } else {
      console.log("Different checkEmailCode Error than axios", error);
      return new Error("Different checkEmailCode Error than axios");
    }
  }
};

interface NewUserInfo {
  email: string;
  password: string;
  nickName: string;
  gender: "male" | "female";
  birth: string;
  userName: string;
  code: string;
  termsAgree: boolean;
}

export const postSignUp = async (newUserInfo: NewUserInfo) => {
  try {
    const response = await instance.post("/users", newUserInfo);
    return response.data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      console.log("postEmailCheck Api response", error.response);
      return error.response?.data;
    } else {
      console.log("Different signUp Error than axios", error);
      return new Error("Different signUp Error than axios");
    }
  }
};