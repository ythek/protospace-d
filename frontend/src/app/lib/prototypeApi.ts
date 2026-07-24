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

export const fetchPrototypesByUserId = async (userId: number): Promise<PrototypeData[]> => {
  const response = await apiClient.get<PrototypeData[]>(`/api/users/${userId}/prototypes`);
  return response.data;
};