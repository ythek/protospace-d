//詳細画面の作成
"use client";

import React, { useEffect, useState } from 'react';
import Link from 'next/link'
import { useRouter } from 'next/navigation';
import styles from './PrototypeDetail.module.css';
import { CommentData } from '../lib/commentData';
import { useAuthContext } from '../app/context/AuthContext';
import { fetchComments, createComment, deletePrototype } from '../lib/prototypeApi';
import { PrototypeDetailData } from '@/lib/prototypeDetailData';
import { PrototypeLike } from './PrototypeLike';


interface Props{
  prototype: PrototypeDetailData;
}

export default function PrototypeDetail ({ prototype }: Props ) {

  
  // AuthContextからログイン中のユーザー情報を取得
  const { user } = useAuthContext();user?.id;

  const isLoggedIn = user?.isAuthenticated ?? false;

    const router = useRouter();

  const [comments, setComments] = useState<CommentData[]>([]);
  const [commentText, setCommentText] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [openModal, setOpenModal] = useState(false);
  const [isCopied, setIsCopied] = useState(false);

  // ログイン中のユーザー＝プロトタイプ投稿ユーザーの判定
  const isOwner = user?.id === prototype.user?.id
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

  // 削除ボタンを押したときの処理
  const handleDelete = async () => {
    // 確認アラート
    if (!window.confirm("このプロトタイプを削除しますか？")) {
      return;
    }

    try {
      await deletePrototype(prototype.id);
      router.push('/');
    } catch (error) {
      console.error('削除エラー:', error);
      alert('削除に失敗しました');
    }
  };

  //現在のUrLと共有したいテキストを準備
  const currentUrl = typeof window !== 'undefined' ? window.location.href : '';
   //ブラウザにWindowオブジェクトがある時にwindow.location.hrefでリンクを取得して、ない時は空を返す


  //Xにシェアする
  const shareText = `[${prototype.title}]  ${prototype.catchcopy}`; //ｘなどで開かれた際のタイトルとキャッチコピーの表示のされ方
  const XShareUrl = `https://x.com/intent/tweet?url=${encodeURIComponent(currentUrl)}&text=${encodeURIComponent(shareText)}`;

  //LINEにシェアする
  const LineShareUrl = `https://social-plugins.line.me/lineit/share?url=${encodeURIComponent(currentUrl)}`;

  //クリップボードにコピーする
  const handleCopyLink = async () => {
    try {
      await navigator.clipboard.writeText(window.location.href); //今開いているリンクのURLを取得

      setIsCopied(true);

      setTimeout (()=> {
        setIsCopied(false); //2秒後に元に戻す
      }, 2000);
      
    } catch (error) {
    console.log ('コピーに失敗しました')
    }

    }
  
  //コメント送信処理
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
    
    <div className={styles.title_container}>
      <div className={styles.prototype_title}>{prototype.title}</div>
      {prototype && (
      <PrototypeLike
              key={`${prototype.id}-${prototype.likecount}`}
              prototypeId={prototype.id}
              initialLikeCount={prototype.likecount ?? 0}
              initialLikeCheck={prototype.likecheck ?? false}
              />
      )}
      {/* 共有ボタン */}
        <button 
          type='button'
          onClick={() => setOpenModal(true)}
          className={styles.share_button}>共有する</button>    
        </div>  
  
        {openModal && (
          <div className={styles.modal_overlay}>
            <div className={styles.modal_content}>
              <h2 style={{color: '#ffffff'}}>プロトタイプの共有</h2>
              <h3 style={{color: '#ffffff'}}>プロトタイプの投稿内容</h3>

                <div className={styles.image}>
                  <img
                   src={
                        prototype.image?.startsWith('http')
                        ? prototype.image
                        : prototype.image?.startsWith('/uploads/')
                        ? `${process.env.NEXT_PUBLIC_API_BASE_URL}${prototype.image}`
                        : `${process.env.NEXT_PUBLIC_API_BASE_URL}/uploads/${prototype.image}`  }
                        alt={prototype.title || 'プロトタイプ画像'} 
                        className={styles.image} />
                        </div>

                  <div className={styles.prototype_detail}>
                    <h3 className={styles.detail_label} style={{color: '#ffffff'}}>------タイトル------</h3>
                    <p className={styles.detail_title_share} style={{color: '#ffffff'}}>{prototype.title}</p>
                    <h3 style={{color: '#ffffff'}}> ---キャッチコピー---</h3>
                    <p className={styles.detail_messages} style={{color: '#ffffff'}}>{prototype.catchcopy}</p>
                  </div>
                
                <div className={styles.share_container}>

                 <a href={XShareUrl} 
                    className={styles.Xshare_button}
                    target='_blank'
                    rel='noopener noreferrer'>
                      Xでシェア
                    </a>

                 <a href={LineShareUrl}
                    className={styles.lineShare_button}
                    target='_blank'
                    rel='noopener noreferrer'
                    > LINEでシェア
                    </a>

                  <button type='button'
                          className={styles.copy_button}
                          onClick={handleCopyLink}>
                          {isCopied ? 'コピーが完了しました' : 'URLをコピーする'}
                          </button>
                </div>

                {/*閉じる処理*/}
                  <button 
                  className={styles.close_share_button}
                  type='button' 
                  onClick={() => setOpenModal(false)}> 閉じる</button> 
            </div>
          </div>
          )}

      <Link href={`/users/${prototype.user?.id}`} className={styles.userName}>{prototype.user?.username}</Link> 


      { isOwner &&(
        <div className={styles.prototype_manage}>
          <Link href={`/prototypes/${prototype.id}/edit`} className={styles.prototype_button}>編集する</Link>
          <button onClick={handleDelete} className={styles.prototype_button}>削除する</button>
        </div>
      )}

      <div className={styles.prototype_image}>
          <img
            src={
            prototype.image?.startsWith('http')
            ? prototype.image
            : prototype.image?.startsWith('/uploads/')
            ? `${process.env.NEXT_PUBLIC_API_BASE_URL}${prototype.image}`
            : `${process.env.NEXT_PUBLIC_API_BASE_URL}/uploads/${prototype.image}` 
    }
            alt={prototype.title || 'プロトタイプ画像'} 
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
