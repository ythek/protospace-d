// export interface UserData {
//   username: string;
// }

export interface UserData {
  id?: number;              // IDを追加（任意取得可能に）
  username?: string;
  email?: string;
  isAuthenticated?: boolean;
}