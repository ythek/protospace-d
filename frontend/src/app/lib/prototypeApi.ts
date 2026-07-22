import apiClient from './apiClient';
import { PrototypeData } from './prototypeData';

export const fetchPrototypes = async (): Promise<PrototypeData[]> => {
  const response = await apiClient.get('/api/prototypes');
  return response.data;
};