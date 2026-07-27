'use client';

import PrototypeView from "@/components/PrototypeView";
import {fetchPrototypesByUserId } from "@/lib/prototypeApi";
import { PrototypeData } from "@/lib/prototypeData";
import { useEffect, useState } from "react";

import styles from './style.module.css';

import { useParams } from "next/navigation";
import { fetchUserById } from "@/lib/userApi";
import { UserData } from "@/lib/userData";



export default function UserDetail() {
  const params = useParams();
  const id = params.userId;
  const [prototypes, setPrototypes] = useState<PrototypeData[]>([]);
  
  const [user, setUser] = useState<UserData>();

  useEffect(() => {
      const userId = typeof id === 'string' ? Number(id) : null;
    if(userId){
        const getPrototypesByUser = async () => {
          try {
            const selectedUser = await fetchUserById(userId);
            setUser(selectedUser);
            const data = await fetchPrototypesByUserId(userId);
            setPrototypes(data);
          } catch (error) {
            console.error("取得に失敗しました", error);
          }
        };
        getPrototypesByUser();
    }
   }, [id]);

  if(user){
  return (
    <main className={styles.container}>
      <h2 className={styles.user_info}>{user.username}さんの情報</h2>

      <table className={styles.userinfo}>
        <tbody>
          <tr>
            <td className={styles.recordname}>名前</td>
            <td>{user.username}</td>
          </tr>
          <tr>
            <td className={styles.recordname}>プロフィール</td>
            <td>{user.profile}</td>
          </tr>
          <tr>
            <td className={styles.recordname}>所属</td>
            <td>{user.affiliation}</td>
          </tr>
          <tr>
            <td className={styles.recordname}>役職</td>
            <td>{user.position}</td>
          </tr>
        </tbody>
     </table>

      <h2 className={styles.user_info}>{user.username}さんの投稿</h2>
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