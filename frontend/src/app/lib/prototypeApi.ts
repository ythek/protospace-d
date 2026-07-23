import apiClient from './apiClient';
import { PrototypeData } from './prototypeData';
import { CommentData } from './commentData';

export const fetchPrototypes = async (): Promise<PrototypeData[]> => {
  const response = await apiClient.get('/api/prototypes');
  return response.data;
};

// 詳細画面取得（担当外なのでコメントアウトしておきます）
// export const fetchByPrototypeId = async (prototypeId: string ): Promise<PrototypeData[]> => {
//   const response = await apiClient.get(`/api/prototypes/${prototypeId}`);
//   return response.data;
// }

// コメント一覧取得
export const fetchComments = async (prototypeId: string ): Promise<CommentData[]> => {
  const response = await apiClient.get<CommentData[]>(`/api/prototypes/${prototypeId}/comments`);
  return response.data;
};

// コメント投稿
export const createComment = async (prototypeId: string , comment: string) => {
  const response = await apiClient.post(`/api/prototypes/${prototypeId}/comments`, {
    comment,
  });
  return response.data;
};