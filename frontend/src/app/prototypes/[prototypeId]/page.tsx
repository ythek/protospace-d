'use client';

import React, { useEffect, useState } from 'react';
import { useParams } from 'next/navigation';
import { fetchComments, createComment } from '@/app/lib/prototypeApi';
import { CommentData } from '@/app/lib/commentData';
import { UserData } from '@/app/lib/userData';
import styles from './page.module.css';

export default function PrototypeDetailPage() {
  const params = useParams();
  const prototypeId = params.prototypeId as string;

  // 仮のプロトタイプデータ
  const prototype = {
    title: 'ウェブアプリ１',
    author: 'testuser1',
    catchcopy: 'テキストテキスト',
    concept: 'テキストテキストテキストテキスト',
    imageUrl: 'sample1.png',
  };

  // コメント一覧と入力フォームの状態
  const [comments, setComments] = useState<CommentData[]>([]);
  const [newComment, setNewComment] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  // ログイン状態の変更用（動作確認用）
  const [currentUser, setCurrentUser] = useState<UserData | null>({ username: 'testuser1' });

  // 初回読み込み時にコメント一覧のみ取得
  useEffect(() => {
    if (!prototypeId) return;
    loadComments();
  }, [prototypeId]);

  // コメント一覧を取得する関数
  const loadComments = async () => {
    try {
      const data = await fetchComments(prototypeId);
      setComments(data);
    } catch (error) {
      console.error('コメント取得エラー:', error);
    }
  };

  // コメント送信処理
  const handleSubmitComment = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrorMessage('');

    // 空送信時のバリデーション
    if (!newComment.trim()) {
      setErrorMessage('コメントを入力してください');
      return;
    }

    try {
      await createComment(prototypeId, newComment);
      setNewComment(''); // フォームをクリア
      await loadComments(); // 最新のコメント一覧を取得して反映
    } catch (error: any) {
      if (error.response?.data?.comment) {
        setErrorMessage(error.response.data.comment);
      } else {
        setErrorMessage('コメントの送信に失敗しました');
      }
    }
  };

  return (
    <main className={styles.container}>
      {/* 動作確認用：ログイン状態の簡易的な切替ボタン */}
      <div className={styles.testBar}>
        状態：{currentUser ? 'ログイン中' : '未ログイン'}{' '}
        <button onClick={() => setCurrentUser(currentUser ? null : { username: 'testuser1' })}>
          ログイン状態を変更
        </button>
      </div>



{/* 
    <div className={styles.card}>
      <Link href={`/prototypes/${prototype.id}`} className={styles.imageWrapper}>
        <img 
          src={prototype.image} 
          alt={prototype.title} 
          className={styles.image}
        />
      </Link>
      <div className={styles.content}>
        <h2 className={styles.title}>
          <Link href={`/prototypes/${prototype.id}`} className={styles.titleLink}>
            {prototype.title}
          </Link>
        </h2>
        <p className={styles.catchcopy}>{prototype.catchcopy}</p>
        <div className={styles.author}>
          by <a href="#">{prototype.user?.username}</a>
        </div>
      </div>
    </div> 
*/}




      {/* プロトタイプ詳細エリア */}
      <h1 className={styles.title}>{prototype.title}</h1>
      <p className={styles.author}>
        by <span className={styles.authorName}>{prototype.author}</span>
        {/* by <span><link href={`users/${user.id}`} className={styles.authorName}>{prototype.author}</link></span> */}
      </p>

      <div className={styles.btnGroup}>
        <button className={styles.subBtn}>
          {/* <link href={`prototypes/${prototype.id}/edit`}> */}
            編集する
          {/* </link> */}
        </button>
        <button className={styles.subBtn}>
          {/* <link href={`prototypes/${prototype.id}/delete`}> */}
            削除する
          {/* </link> */}
        </button>
      </div>

      <div className={styles.imageWrapper}>
        <img src={prototype.imageUrl} alt={prototype.title} className={styles.image} />
      </div>

      <div className={styles.section}>
        <h2 className={styles.sectionTitle}>キャッチコピー</h2>
        <p className={styles.sectionText}>{prototype.catchcopy}</p>
      </div>

      <div className={styles.section}>
        <h2 className={styles.sectionTitle}>コンセプト</h2>
        <p className={styles.sectionText}>{prototype.concept}</p>
      </div>

      {/* コメントのエリア */}
      <div className={styles.commentArea}>
        <h3 className={styles.commentTitle}>コメント</h3>

        {/* ログインユーザーのみフォームを表示 */}
        {currentUser ? (
          <form onSubmit={handleSubmitComment} className={styles.commentForm}>
            {errorMessage && (
              <p className={styles.errorMessage}>{errorMessage}</p>
            )}
            <input
              type="text"
              value={newComment}
              onChange={(e) => setNewComment(e.target.value)}
              className={styles.textarea}
            />
            <button type="submit" className={styles.submitBtn}>
              送信する
            </button>
          </form>
        ) : (
          <p className={styles.loginNotice}>
            ※ コメントの投稿にはログインが必要です。
          </p>
        )}

        {/* コメント一覧 */}
        <div className={styles.commentList}>
          {comments.map((item) => (
            <div key={item.id} className={styles.commentItem}>
              ・{item.comment} <span className={styles.commentAuthor}>（{item.username}）</span>
              {/* ・{item.comment} <span><link href={`users/${user.id}`} className={styles.commentAuthor}>（{item.username}）</link></span> */}
            </div>
          ))}
        </div>
      </div>
    </main>
  );
}