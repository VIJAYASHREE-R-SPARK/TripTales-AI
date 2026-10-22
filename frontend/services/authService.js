import axios from "axios";
import API_BASE_URL from "./api";

const API_URL = `${API_BASE_URL}/auth`;

export const registerUser = async (userData) => {
const response = await axios.post(
`${API_URL}/register`,
userData
);

return response.data;
};

export const loginUser = async (credentials) => {
const response = await axios.post(
`${API_URL}/login`,
credentials
);

return response.data;
};
