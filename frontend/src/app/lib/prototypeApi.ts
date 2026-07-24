import apiClient from './apiClient';
import { PrototypeData } from './prototypeData';
import { CommentData } from './commentData';

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

// 削除機能
export const deletePrototype = async (prototypeId: number | string ): Promise<void> => {
  await apiClient.delete(`/api/prototypes/${prototypeId}/delete`)
}

// コメント一覧取得
export const fetchComments = async (prototypeId: number | string ): Promise<CommentData[]> => {
  const response = await apiClient.get<CommentData[]>(`/api/prototypes/${prototypeId}/comments`);
  return response.data;
};

// コメント投稿
export const createComment = async (prototypeId: number | string , comment: string) => {
  const response = await apiClient.post(`/api/prototypes/${prototypeId}/comments`, {
    comment,
  });
  return response.data;
};
