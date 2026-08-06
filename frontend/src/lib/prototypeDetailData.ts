import { UserData } from './userData';

export interface PrototypeDetailData {
  id: number;
  title: string;
  catchcopy: string;
  concept: string;
  image: string;
  user? :UserData;
  likecount :number;
  likecheck :boolean;
}