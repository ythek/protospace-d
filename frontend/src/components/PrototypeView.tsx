import React from 'react';
import Link from 'next/link';
import { PrototypeData } from '../lib/prototypeData';
import styles from './PrototypeView.module.css';
import { userAgent } from 'next/server';
import { PrototypeLike } from './PrototypeLike';

interface Props {
  prototype: PrototypeData;
}

export default function PrototypeView({ prototype }: Props) {

  console.log("【詳細画面のデータ確認】", {
  likecheck: prototype?.likecheck,
  likecount: prototype?.likecount,
  // もし大文字混ざりのプロパティ名で届いている可能性もチェック
  likeCheck: (prototype as any).likeCheck,
  likeCount: (prototype as any).likeCount,
});
  return (
    <div className={styles.card}>
      <Link href={`/prototypes/${prototype.id}`} className={styles.imageWrapper}>
        <img 
            src={
              prototype.image?.startsWith('http')
              ? prototype.image
              : prototype.image?.startsWith('/uploads/')
              ? `${process.env.NEXT_PUBLIC_API_BASE_URL}${prototype.image}`
              : `${process.env.NEXT_PUBLIC_API_BASE_URL}/uploads/${prototype.image}` 
            }
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
        
        {prototype && (
        <PrototypeLike
        key={`${prototype.id}-${prototype.likecount}`}
        prototypeId={prototype.id}
        initialLikeCount={prototype.likecount ?? 0}
        initialLikeCheck={prototype.likecheck ?? false}
        />
        )}
                <div className={styles.author}>
          {/* 紐づくユーザーidを取得する */}
          by <a href={`/users/${prototype.userId}`}>{prototype.username}</a>
        </div>
      </div>
    </div>
  );
}