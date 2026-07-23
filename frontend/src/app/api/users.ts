import axios from 'axios';
import { responseCookiesToRequestCookies } from 'next/dist/server/web/spec-extension/adapters/request-cookies';
import qs from 'qs';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true,
});

interface SignUpForm {
  email: string;
  password: string;
  passwordConfirmation: string;
  username: string;
  profile: string;
  affiliation: string;
  position: string;
}

interface UserResponse {
  id: number;
  username: string;
}

interface UserInfo {
  isLoggedIn:boolean;
  id: number | null;
  username: string | null;
} 

interface LoginCredentials {
  email: string;
  password: string;
}


// 新規登録
export const signUp = async (formData: SignUpForm): Promise<UserResponse> => {
  try {
    const response = await api.post<UserResponse>('/users/', formData);
    return response.data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      console.error('Sign up error:', error.response?.data);
      const messages = error.response?.data?.messages;
      throw new Error(messages ? messages.join(', ') : '登録に失敗しました');
    }
    throw error;
  }
};

// ログイン
export const login = async (credentials: LoginCredentials): Promise<UserResponse> => {
  try {
    const response = await api.post<UserResponse >('/sign_in', qs.stringify({
      email: credentials.email,
      password: credentials.password,
    }), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    });
    return response.data;

  } catch (error) {
    if (axios.isAxiosError(error)) {
      console.error('Login error:', error.response?.data);
      const messages = error.response?.data?.messages;
      throw new Error(messages ? messages.join(', ') : 'ログインに失敗しました');
    }
    throw error;
  }
}; 

// ログアウト
export const logout = async (): Promise<void> => {
  try {
    await api.get('/sign_out');
  } catch (error) {
    if (axios.isAxiosError(error)) {
      console.error('Logout error:', error.response?.data);
      throw new Error('ログアウトに失敗しました');
    }
  }
};