"use client";

import React, { useEffect, useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import styles from './PrototypeDetail.module.css';
import { PrototypeData } from '../lib/prototypeData';
import { CommentData } from '../lib/commentData';
import { useAuthContext } from '../context/AuthContext';
import { fetchComments, createComment, deletePrototype } from '../lib/prototypeApi';

interface Props {
  prototype: PrototypeData;
  currentUserName?: string | null;
}

// バックエンドのベースURL
const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080';

export default function PrototypeDetail({ prototype }: Props) {
  const { user } = useAuthContext();
  const isLoggedIn = user?.isAuthenticated ?? !!user;
  const router = useRouter();

  const [comments, setComments] = useState<CommentData[]>([]);
  const [commentText, setCommentText] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  // ★ 画像のフルURLを生成する関数（/uploads/ 自動付与対応）
  const getImageUrl = (imagePath?: string | null) => {
    if (!imagePath) return '/no-image.png'; // 画像がない場合のフォールバック
    if (imagePath.startsWith('http://') || imagePath.startsWith('https://')) {
      return imagePath;
    }
    
    // 先頭の斜線を整える
    const cleanPath = imagePath.startsWith('/') ? imagePath : `/${imagePath}`;

    // 先頭に /uploads/ が含まれていなければ付与する
    const finalPath = cleanPath.startsWith('/uploads/') 
      ? cleanPath 
      : `/uploads${cleanPath}`;

    return `${API_BASE_URL}${finalPath}`;
  };

  // 1. ログインユーザーIDを取得
  const currentUserId = user?.id ? Number(user.id) : null;

  // 2. 投稿者のIDを取得
  const authorId = prototype?.user?.id 
    ? Number(prototype.user.id) 
    : (prototype as any)?.userId 
      ? Number((prototype as any).userId) 
      : null;

  // 3. 安全に所有者判定
  const isOwner = Boolean(
    currentUserId !== null && 
    authorId !== null && 
    currentUserId === authorId
  );

  // コメント一覧を表示
  const loadComments = async () => {
    try {
      const data = await fetchComments(prototype.id);
      setComments(data);
    } catch (error) {
      console.error('コメント取得時エラー：', error);
    }
  };

  useEffect(() => {
    if (prototype?.id) {
      loadComments();
    }
  }, [prototype?.id]);

  // 削除ボタンを押したときの処理
  const handleDelete = async () => {
    if (!window.confirm("このプロトタイプを削除しますか？")) {
      return;
    }

    try {
      await deletePrototype(prototype.id);
      router.push('/');
      router.refresh();
    } catch (error) {
      console.error('削除エラー:', error);
      alert('削除に失敗しました');
    }
  };

  // コメント送信処理
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
      setCommentText('');
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

      {/* 投稿者本人の場合のみ編集・削除ボタンを表示 */}
      {isOwner && (
        <div className={styles.prototype_manage}>
          <Link href={`/prototypes/${prototype.id}/edit`} className={styles.prototype_button}>編集する</Link>
          <button onClick={handleDelete} className={styles.prototype_button}>削除する</button>
        </div>
      )}

      <div className={styles.prototype_image}>
        <img 
          src={getImageUrl(prototype.image)} 
          alt={prototype.title} 
          className={styles.image} 
        />
      </div>

      <div className={styles.prototype_body}>
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
        {isLoggedIn ? (
          <form onSubmit={handleSubmitComment}>
            <div className={styles.field}>
              <label htmlFor="comment_content">コメント</label>
              <br />
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