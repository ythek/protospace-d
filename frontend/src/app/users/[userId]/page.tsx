'use client';

import PrototypeView from "@/components/PrototypeView";
import { PrototypeData } from "@/lib/prototypeData";
import { useEffect, useState } from "react";

import styles from './style.module.css';

import { useParams } from "next/navigation";
import { fetchUserById } from "@/lib/userApi";
import { UserData } from "@/lib/userData";
import { useAuthContext } from "@/app/context/AuthContext";



export default function UserDetail() {
  const params = useParams();
  const id = params.userId;
  const [prototypes, setPrototypes] = useState<PrototypeData[]>([]);
  const {user} = useAuthContext();
  const [searchedUser, setUser] = useState<UserData>();

  useEffect(() => {
      const userId = typeof id === 'string' ? Number(id) : null;
    if(userId){
        const getPrototypesByUser = async () => {
          try {
            const selectedUser = await fetchUserById(userId);
            setUser(selectedUser.user);
            setPrototypes(selectedUser.prototypes);
          } catch (error) {
            console.error("取得に失敗しました", error);
          }
        };
        getPrototypesByUser();
    }
   }, [id]);

  if (!searchedUser) return <div className={styles.container}>読み込み中・・・</div>;

  if(searchedUser.id){
  return (
    <main className={styles.container}>
      {user?.id === searchedUser.id ? (
        <h2 className={styles.user_info}>あなたの情報</h2>
      ) : (
        <h2 className={styles.user_info}>{searchedUser.username}さんの情報</h2>
      )}
      <table className={styles.userinfo}>
        <tbody>
          <tr>
            <td className={styles.recordname}>名前</td>
            <td>{searchedUser.username}</td>
          </tr>
          <tr>
            <td className={styles.recordname}>プロフィール</td>
            <td>{searchedUser.profile}</td>
          </tr>
          <tr>
            <td className={styles.recordname}>所属</td>
            <td>{searchedUser.affiliation}</td>
          </tr>
          <tr>
            <td className={styles.recordname}>役職</td>
            <td>{searchedUser.position}</td>
          </tr>
        </tbody>
     </table>

      {user?.id === searchedUser.id ? (
        <h2 className={styles.user_info}>あなたの投稿</h2>
      ) : (
        <h2 className={styles.user_info}>{searchedUser.username}さんの投稿</h2>
      )}
      <div className={styles.grid}>
        {prototypes.map((prototype) => (
          <PrototypeView key={prototype.id} prototype={prototype} />
        ))}
      </div>
    </main>
  );

  }
  return(
    <main className={styles.container}>
      <h2 className="user_info">存在しないユーザーです</h2>     
    </main>
  );
}