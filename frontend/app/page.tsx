'use client';

import { useEffect, useState } from 'react';
import Link from 'next/link';

// プロトタイプの型定義
type Prototype = {
  id: number;
  title: string;
  catchCopy: string;
  concept: string;
};

export default function Home() {
  const [prototypes, setPrototypes] = useState<Prototype[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    // Spring Bootからデータ一覧を取得
    fetch('http://localhost:8080/api/prototypes')
      .then((res) => {
        if (!res.ok) {
          throw new Error('データの取得に失敗しました');
        }
        return res.json();
      })
      .then((data) => {
        setPrototypes(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error(err);
        setError('バックエンドとの通信でエラーが発生しました。');
        setLoading(false);
      });
  }, []);

  return (
    <main className="main-container">
      <div className="header-area">
        <h1 className="page-title">Protospace</h1>
        
        {/* 新規投稿画面への遷移ボタン */}
        <Link href="/prototypes/new" className="btn-primary">
          新規投稿
        </Link>
      </div>

      {loading && <p className="status-message">読み込み中...</p>}
      
      {error && <p className="error-message">{error}</p>}

      {!loading && !error && (
        <div className="card-list">
          {prototypes.length === 0 ? (
            <p className="empty-message">プロトタイプがまだありません。新規投稿から追加してください。</p>
          ) : (
            prototypes.map((proto) => (
              <div key={proto.id} className="prototype-card">
                <h2 className="card-title">{proto.title}</h2>
                <p className="card-catchcopy">{proto.catchCopy}</p>
                <p className="card-concept">{proto.concept}</p>
              </div>
            ))
          )}
        </div>
      )}
    </main>
  );
}