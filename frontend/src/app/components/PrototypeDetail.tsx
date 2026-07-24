//詳細画面の作成
"use client";

import React, { useEffect, useState } from 'react';
import Link from 'next/link'
import styles from './PrototypeDetail.module.css';
import { PrototypeData } from '../lib/prototypeData';
import { CommentData } from '../lib/commentData';
import { useAuthContext } from '../context/AuthContext';
import { fetchComments, createComment } from '../lib/prototypeApi';


interface Props{
  prototype: PrototypeData;
}

export default function PrototypeDetail ({ prototype }: Props ) {
  // AuthContextからログイン中のユーザー情報を取得
  const { user } = useAuthContext();
  const isLoggedIn = user?.isAuthenticated ?? false;

  const [comments, setComments] = useState<CommentData[]>([]);
  const [commentText, setCommentText] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  // ログイン中のユーザー＝プロトタイプ投稿ユーザーの判定
  const isOwner = user?.username === prototype.user?.username;

  // コメント一覧を表示
  const loadComments = async () => {
    try {
      const data = await fetchComments(prototype.id);
      setComments(data);
    } catch(error) {
      console.log('コメント取得時エラー：', error);
    }
  };

  // プロトタイプIDが変わる（詳細表示するプロトタイプが変わる）度にコメント一覧を取得・表示
  useEffect(() => {
    if (prototype?.id) {
      loadComments();
    }
  }, [prototype?.id]);

  // コメント送信
  const handleSubmitComment = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrorMessage('');

    if (!commentText.trim()) {
      setErrorMessage('コメントを入力してください');
      return;
    }

    try {
      setIsSubmitting(true);
      await createComment(prototype.id, commentText);
      setCommentText(''); // 入力欄をクリア
      await loadComments();
    } catch (error: any) {
      if (error.response?.data?.comment) {
        setErrorMessage(error.response.data.comment);
      } else {
        setErrorMessage('コメントの送信に失敗しました');
      }
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className={styles.container}>
    
      <div className={styles.prototype_title}>{prototype.title}</div>
      <Link href={'/#'} className={styles.userName}>{prototype.user?.username}</Link> 
      {/* <Link href={`users/${prototype.user?.id}`} className={styles.userName}>{prototype.user?.username}</Link>  */}
    
      { isOwner &&(
        <div className={styles.prototype_manage}>
          <Link href={`prototypes/${prototype.id}/edit`} className={styles.prototype_button}>編集する</Link>
          <Link href={`prototypes/${prototype.id}/delete`} className={styles.prototype_button}>削除する</Link>
        </div>
      )}

      <div className={styles.prototype_image}>
          <img 
            src={prototype.image} 
            alt={prototype.title} 
            className={styles.image} />
      </div>

      <div className ={styles.prototype_body}>
        <div className={styles.prototype_detail}>
          <p className={styles.detail_title}>キャッチコピー</p>
          <p className={styles.detail_messages}>{prototype.catchcopy}</p>
          </div>
        </div>
      <div className={styles.prototype_body}>
        <div className={styles.prototype_detail}>
          <p className={styles.detail_title}>コンセプト</p>
          <p className={styles.detail_message}>{prototype.concept}</p>
        </div>
      </div>

      <div className={styles.prototype_comments}>
        {/* ログイン時のみコメント投稿フォームを表示 */}
        {isLoggedIn ? (
          <form onSubmit={handleSubmitComment}>
            <div className={styles.field}>
              <label htmlFor="comment_content">コメント</label>
              {errorMessage && <p className={styles.errorMessage}>{errorMessage}</p>}
              <input
                type="text"
                id="comment_content"
                value={commentText}
                onChange={(e) => setCommentText(e.target.value)}
                className={styles.comment_input}
              />
              <div className={styles.actions}>
                <button type="submit" className={styles.form_btn} disabled={isSubmitting}>
                  {isSubmitting ? '送信中...' : '送信する'}
                </button>
              </div>
            </div>
          </form>
        ) : (
          <p className={styles.loginNotice}>※ コメントの投稿にはログインが必要です。</p>
        )}

        {/* コメント一覧表示 */}
        <div className={styles.commentList}>
          {comments.map((item) => (
            <div key={item.id} className={styles.commentItem}>
              <p className={styles.commentText}>
                ・{item.comment} 
                <Link href={`/users/${item.userId}`} className={styles.commentAuthor}>
                  （{item.username}）
                </Link>
              </p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}