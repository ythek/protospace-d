import apiClient from './apiClient';
//import { PrototypeData } from './prototypeData';
import { PrototypeData } from "@/app/components/PrototypeDetail";

export const fetchPrototypes = async (): Promise<PrototypeData[]> => {
  const response = await apiClient.get('/api/prototypes');
  return response.data;
};

export const fetchPrototypeById = async (prototypeId: number | string): Promise<PrototypeData> => {
  const response = await apiClient.get(`/api/prototypes/${prototypeId}`);
  return response.data;
};
