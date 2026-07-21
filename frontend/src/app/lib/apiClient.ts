// axiosの基本設定

import axios from 'axios';

export const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true,
});