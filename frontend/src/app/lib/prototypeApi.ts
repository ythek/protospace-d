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

export interface PrototypeFormData {
  title: string;
  catchcopy: string;
  concept: string;
  imageFile: File | null;
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
      'Content-Type': 'multipart/form-data',
    },
    withCredentials: true, // セッションCookieを送信
  });
  return response.data;
};

// プロトタイプ削除
export const deletePrototype = async (prototypeId: number | string) => {
  const response = await apiClient.delete(`/api/prototypes/${prototypeId}`, {
    withCredentials: true, // 削除権限チェックのためセッションCookieを送信
  });
  return response.data;
};

// コメント一覧取得
export const fetchComments = async (prototypeId: number | string): Promise<CommentData[]> => {
  const response = await apiClient.get<CommentData[]>(`/api/prototypes/${prototypeId}/comments`);
  return response.data;
};

// コメント投稿
export const createComment = async (prototypeId: number | string, comment: string) => {
  const response = await apiClient.post(
    `/api/prototypes/${prototypeId}/comments`,
    { 
      // Javaの CommentForm のフィールド名と型に100%合わせる
      comment: String(comment),         // @NotBlank の対象
      prototypeId: Number(prototypeId)  // @NotNull の対象（数値型として渡す）
    },
    {
      withCredentials: true,
    }
  );
  return response.data;
};

// プロトタイプ更新
export const updatePrototype = async (
  prototypeId: number | string,
  data: PrototypeFormData
): Promise<PrototypeData> => {
  const formData = new FormData();
  formData.append('title', data.title);
  formData.append('catchcopy', data.catchcopy);
  formData.append('concept', data.concept);

  // 新しい画像が選択されている場合のみ送信
  if (data.imageFile) {
    formData.append('imageFile', data.imageFile);
  }

  const response = await apiClient.put<PrototypeData>(`/api/prototypes/${prototypeId}`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
    withCredentials: true,
  });
  return response.data;
};