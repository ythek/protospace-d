import { apiClient } from './apiClient';
import { PrototypeData } from './prototypeData';


export const getPrototypes = async (): Promise<PrototypeData[]> => {
  const response = await apiClient.get<PrototypeData[]>('/prototypes/');
  return response.data;
};