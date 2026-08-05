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

      // 隠しコマンドを配列で設定
      const secretCommand = ['g', 'a', 'c', 'h', 'a'];

      //ユーザーの入力履歴を保存する配列を定義
      let inputKeys: string[] = [];

      //キーボードが押されたときのイベントリスナーを設定
      window.addEventListener('keydown', (e) => {
        // 押されたキーを履歴配列の最後に追加する
        inputKeys.push(e.key);

        // 履歴がコマンドの文字数を超えたら、一番先頭の文字を削除する
        if (inputKeys.length > secretCommand.length) {
          inputKeys.shift();
        }

        //隠しコマンド成功時の処理
        // 入力履歴と正解コマンドが完全に一致したか判定
        if (inputKeys.join(',') === secretCommand.join(',')) {
          alert("隠しコマンド：ガチャページに移動します");
          sessionStorage.setItem('isGachaUnlocked', 'true');
          window.location.href = `/prototypes/gacha`;
          inputKeys = [];
        }
      });
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