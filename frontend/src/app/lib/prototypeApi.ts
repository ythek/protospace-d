import apiClient from './apiClient';
import { PrototypeData } from './prototypeData';

export const fetchPrototypes = async (): Promise<PrototypeData[]> => {
  const response = await apiClient.get('/api/prototypes');
  return response.data;
};

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

// ★ 戻り値の型 Promise<PrototypeData> に PrototypeData を使用
export const createPrototype = async (data: PrototypeFormData): Promise<PrototypeData> => {
  const formData = new FormData();
  formData.append('title', data.title);
  formData.append('catchcopy', data.catchcopy);
  formData.append('concept', data.concept);

  if (data.imageFile) {
    formData.append('imageFile', data.imageFile);
  }

  // 💡 第3引数に headers を指定します
  const response = await apiClient.post<PrototypeData>('/api/prototypes', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
  return response.data;
};