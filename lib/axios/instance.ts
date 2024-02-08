import axios from "axios";

const headers = {
  "Content-Type": "application/json;charset=UTF-8",
  "Access-Control-Allow-Origin": "*",
  Authorization: null,
  Refresh_Token: null,
};

const instance = axios.create({
  baseURL: process.env.REACT_APP_SERVER_URL,
  headers,
});

export default instance;