"use client";

import { useEffect, useState } from 'react';
import { fetchPrototypes } from './lib/prototypeApi';
import { PrototypeData } from './lib/prototypeData';
import { UserData } from './lib/userData';
import PrototypeView from './components/PrototypeView';
import styles from './page.module.css';

export default function Home() {
  const [prototypes, setPrototypes] = useState<PrototypeData[]>([]);
  const [currentUser, setCurrentUser] = useState<UserData | null>(null);

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
      {/* ログイン中（currentUserが存在する）場合のみレンダリングされます */}
      {currentUser && (
        <div className={styles.greeting}>
          こんにちは、
          <a href="#" className={styles.usernameLink}>
            {currentUser.username}
          </a>
          さん
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