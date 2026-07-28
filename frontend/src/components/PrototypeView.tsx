import React from 'react';
import Link from 'next/link'; // ← Link をインポート
import { PrototypeData } from '../lib/prototypeData';
import styles from './PrototypeView.module.css';
import { userAgent } from 'next/server';

interface Props {
  prototype: PrototypeData;
}

export default function PrototypeView({ prototype }: Props) {
  return (
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
          {/* 紐づくユーザーidを取得する */}
          by <a href={`/users/${prototype.user?.id}`}>{prototype.user?.username}</a>
        </div>
      </div>
    </div>
  );
}