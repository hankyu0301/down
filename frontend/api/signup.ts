import axios, { AxiosError } from "axios";
import instance from "@/lib/axios/instance";

export const postEmailCheck = async (email: string) => {
  try {
    const response = await instance.post("/user/join/check-email", { email });
    return response.data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      console.log("postEmailCheck Api response", error.response?.data);
      return error.response?.data;
    } else {
      console.log("Different emailCheck Error than axios", error);
      return new Error("Different emailCheck Error than axios");
    }
  }
};

export const postSendEmailCode = async (email: string) => {
  try {
    const response = await instance.post("/user/join/send-email-verification-code", { email });
    return response.data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      return error.response;
    } else {
      console.log("Different sendEmailCode Error than axios", error);
      return new Error("Different sendEmailCode Error than axios");
    }
  }
};

export const postCheckEmailCode = async (email: string, code: string) => {
  try {
    const response = await instance.post("/user/join/check-email-verification-code", { email, code });
    console.log(response.data);
    return response.data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      return error.response;
    } else {
      console.log("Different checkEmailCode Error than axios", error);
      return new Error("Different checkEmailCode Error than axios");
    }
  }
};


export const postSignUp = async (userInfo: any) => { };