import React from 'react';
import Link from 'next/link';
import { PrototypeData } from '../lib/prototypeData';
import styles from './PrototypeView.module.css';

interface Props {
  prototype: PrototypeData;
}

// バックエンドのベースURL
const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080';

export default function PrototypeView({ prototype }: Props) {

  // 画像のフルURLを生成する関数
  const getImageUrl = (imagePath?: string) => {
    if (!imagePath) return '/no-image.png'; // 画像がない場合
    
    // すでにフルURL（http://〜 または https://〜）の場合はそのまま返す
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

  return (
    <div className={styles.card}>
      <Link href={`/prototypes/${prototype.id}`} className={styles.imageWrapper}>
        <img 
          src={getImageUrl(prototype.image)} 
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
  );
}