import apiClient from './apiClient';
import axios from 'axios';
import qs from 'qs';
import { UserData } from './userData';
import { PrototypeData } from './prototypeData';

// 401が返ったときの処理
apiClient.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      const requestUrl = error.config?.url || '';
      const excludedUrls = ['/api/users/sign_in', '/api/users/sign_up'];
      const isAuthRequest = excludedUrls.some(url => requestUrl.includes(url));
      if (!isAuthRequest) {
        // ローカルに残っているユーザー情報を消す
        localStorage.removeItem('user'); 
        // ログイン画面に飛ばす
        window.location.href = '/users/sign_in';

      }
    }
      return Promise.reject(error);
  }
);

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
  email: string;
}

interface LoginCredentials {
  email: string;
  password: string;
}

export interface UserProfileResponse {
  user: UserData;
  prototypes: PrototypeData[];
}

// 新規登録
export const signUp = async (formData: SignUpForm): Promise<UserResponse> => {
  try {
    const response = await apiClient.post<UserResponse>('/api/users/sign_up', formData);
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
    const response = await apiClient.post<UserResponse >('/api/users/sign_in', qs.stringify({
      email: credentials.email,
      password: credentials.password,
    }), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    });

    console.log('APIレスポンス:', response.data);
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
    await apiClient.get('/api/users/sign_out');
  } catch (error) {
    if (axios.isAxiosError(error)) {
      console.error('Logout error:', error.response?.data);
      throw new Error('ログアウトに失敗しました');
    }
  }
};

// ユーザー取得
export const fetchUserById = async (userId: number): Promise<UserProfileResponse> => {
  const response = await apiClient.get<UserProfileResponse>(`/api/users/${userId}`);
  return response.data;
};