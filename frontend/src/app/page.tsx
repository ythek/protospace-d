"use client";

import { useEffect, useState } from 'react';
import { fetchPrototypes } from '../lib/prototypeApi';
import { PrototypeData } from '../lib/prototypeData';
import PrototypeView from '../components/PrototypeView';
import styles from './page.module.css';
import { useAuthContext } from './context/AuthContext';

export default function Home() {
  const [prototypes, setPrototypes] = useState<PrototypeData[]>([]);
  const { user } = useAuthContext();

  useEffect(() => {
    const getPrototypes = async () => {
      try {
        const data = await fetchPrototypes();
        setPrototypes(data);
      } catch (error) {
        console.error("プロトタイプの取得に失敗しました", error);
      }
    };
    getPrototypes();
  }, []);

  return (
    <main className={styles.container}>
      {/* ログイン中（userが存在する）場合のみレンダリングされます */}
      {user && (
        <div className={styles.messageArea}>
          <div className={styles.greeting}>
            こんにちは、
            <a href={`/users/${user.id}`} className={styles.usernameLink}>
              {user.username}
            </a>
            さん
          </div>
          <div className={styles.greeting}>
            <a href={`/prototypes/today`} className={styles.luck}>
              運勢🔮
            </a>
          </div>
        </div>
      )}

      <div className={styles.grid}>
        {prototypes.map((prototype) => (
          <PrototypeView key={prototype.id} prototype={prototype} />
        ))}
      </div>
    </main>
  );
}