import { UserData } from './userData';

export interface PrototypeData {
  id: number;
  title: string;
  catchcopy: string;
  concept: string;
  image: string;
  user?: UserData;
}