import apiClient from './apiClient';
import { PrototypeData } from './prototypeData';
import { prototypeTodayData } from './prototypeTodayData';
import { CommentData } from './commentData';
import axios from 'axios';
import { PrototypeGachaData } from './PrototypeGachaData';

// プロトタイプ一覧取得
export const fetchPrototypes = async (): Promise<PrototypeData[]> => {
  const response = await apiClient.get('/api/prototypes');
  return response.data;
};

// 詳細画面取得
export const fetchPrototypeById = async (prototypeId: number | string): Promise<PrototypeData> => {
  const response = await apiClient.get(`/api/prototypes/${prototypeId}`);
  return response.data;
};

// 今日のプロトタイプの取得
export const fetchPrototypeToday = async (): Promise<prototypeTodayData> => {
  const response = await apiClient.get(`/api/prototype/today`);
  return response.data;
};

// コメント一覧取得
export const fetchComments = async (prototypeId: number | string): Promise<CommentData[]> => {
  const response = await apiClient.get<CommentData[]>(`/api/prototypes/${prototypeId}/comments`);
  return response.data;
};

// コメント投稿
export const createComment = async (prototypeId: number | string, comment: string) => {
  const response = await apiClient.post(`/api/prototypes/${prototypeId}/comments`, {
    comment,
  });
  return response.data;
};

// 削除機能
export const deletePrototype = async (prototypeId: number | string) => {
  const response = await apiClient.delete(`/api/prototypes/${prototypeId}`);
  return response.data;
};

//プロトタイプ一件だけ取得
export const fetchPrototype = async (prototypeId: number | string): Promise<PrototypeData> => {
  const response = await apiClient.get(`/api/prototypes/${prototypeId}/edit`);
  return response.data;
}
//編集更新処理
export const updatePrototype = async (prototypeId: number | string, formData: FormData) => {
  const response = await apiClient.post(`/api/prototypes/${prototypeId}`, formData, {
    headers: {
      'Content-Type': undefined,
    },
  });

  return response.data;
}

// ★ export キーワードをつけて型を定義・エクスポートする
export interface PrototypeFormData {
  title: string;
  catchcopy: string;
  concept: string;
  imageFile?: File | null; // 画像選択用
}

// プロトタイプ新規作成
export const createPrototype = async (data: PrototypeFormData): Promise<PrototypeData> => {
  const formData = new FormData();
  formData.append('title', data.title);
  formData.append('catchcopy', data.catchcopy);
  formData.append('concept', data.concept);

  if (data.imageFile) {
    formData.append('imageFile', data.imageFile);
  }

  const response = await apiClient.post<PrototypeData>('/api/prototypes', formData, {
    headers: {
      'Content-Type': undefined,
    },
  });


  return response.data;
};


//プロトタイプ一件だけ取得
export const fetchPrototypeGacha = async (): Promise<PrototypeGachaData> => {
  try {
    const response = await apiClient.get(`/api/prototype/gacha`);
    return response.data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      console.error('fetch gacha error:', error.response?.data);
      const messages = error.response?.data?.messages;
      throw new Error(messages ? messages.join(', ') : '登録に失敗しました');
    }
    throw error;
  }
}
