import axios from 'axios';

export const instance = axios.create({
  baseURL: import.meta.env.VITE_REQUEST_URL,
  timeout: 1000,
  withCredentials: true,
});
