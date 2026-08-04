"use client";

import { useEffect, useState } from 'react';
import { fetchPrototypes } from '../lib/prototypeApi';
import { PrototypeData } from '../lib/prototypeData';
import PrototypeView from '../components/PrototypeView';
import styles from './page.module.css';
import { useAuthContext } from './context/AuthContext';

export default function Home() {
  const [prototypes, setPrototypes] = useState<PrototypeData[]>([]);
  const {user} = useAuthContext();
  const [isOpen, setIsOpen] = useState(false);
  const [changeOrder, setChangeOrder] = useState<'desc' | 'asc' | 'likes'>('desc');


  const sortedPrototypes = prototypes.toSorted((a, b) => {
    const dateA = new Date(a.createdAt).getTime();
    const dateB = new Date(b.createdAt) .getTime();
    return changeOrder === 'desc' ? dateB - dateA :dateA - dateB;
  });
  // changeOrder が 'desc' なら dateB - dateA（新しい順）
  // そうでなければ dateA - dateB（古い順）



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

      <div className={styles.list}>
        <button onClick={() => setIsOpen(!isOpen)}>
              メニュー {isOpen ? '▲' : '▼'}
        </button>

        {isOpen && (
          <ul className={styles.dropdownMenu}> 
          <li>
            <button
             onClick={() => {
              setChangeOrder('desc'); //新着順
              setIsOpen(false);
            }}>新着順 {changeOrder === 'desc' && '✓'}</button>
          </li>

            <li>
            <button 
            onClick= {() => {
              setChangeOrder('asc');
              setIsOpen(false);
            }}
            > 古い順 {changeOrder === 'asc' && '✓'}</button>
            </li>
            
            <li>
            {user && (
            <button>
              いいね順 {changeOrder === 'likes' && '✓' }</button>
            )}
            </li>

           </ul>
        )}
     </div>

      <div className={styles.grid}>
        {sortedPrototypes.map((prototype) => (
          <PrototypeView key={prototype.id} prototype={prototype} />
        ))}
      </div>
    </main>
  );
}