//詳細画面の作成
"use client";

import Link from 'next/link'
import styles from './PrototypeDetail.module.css';
import React, { useEffect, useState } from 'react';


  export interface PrototypeData {
    id: number;
    title: string;
    catchCopy: string;
    concept: string;
    userName: string;
    image: string;

}

interface Props{
  prototype: PrototypeData;
  currentUserName?: string | null; //ログイン中のユーザー名OR　NULL
}
 
export default function PrototypeDetail ({ prototype, currentUserName }: Props )  {
   const [commentText, setCommentText] = useState(""); //コメントはcommentTextにしてます

   const isOwner = currentUserName === prototype.userName;

   const handelSubmit = (e: React.SubmitEvent) => {
    e.preventDefault(); //ページリロードを防ぐメソッド

  
   
   };

  return (
  <div className={styles.container}>
  

    <div className={styles.prototype_title}>{prototype.title}</div>
    <a href={'/'} className={styles.userName}>{prototype.userName}</a> 
   
   { isOwner &&(
    <div className={styles.prototype_manage}>
      <a href={'/'} className={styles.prototype_button}>編集する</a>
      <a href={'/'} className={styles.prototype_button}>削除する</a>
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
        <p className={styles.detail_messages}>{prototype.catchCopy}</p>
        </div>
      </div>
    <div className={styles.prototype_body}>
      <div className={styles.prototype_detail}>
        <p className={styles.detail_title}>コンセプト</p>
        <p className={styles.detail_message}>{prototype.concept}</p>
      </div>
     </div>

     <div className={styles.prototype_comments}>
      <form>
        <div className={styles.field}>
          <label htmlFor="comment_content">コメント</label>
            <br />
             <input type="text" id="comment_content" 
             />
             <div className={styles.actions}>
            <button  type='submit' className={styles.form_btn}>送信する</button>
          </div>
          </div>
       </form>

     </div>
    </div>

  


);
}
