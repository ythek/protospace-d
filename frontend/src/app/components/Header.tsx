"use client";

import Link from "next/link";
import Image from "next/image";
import { useState } from "react";

export default function Header() {
  // 仮のログイン状態（実際はバックエンドと連携）
  const [isLoggedIn, setIsLoggedIn] = useState(true);

  return (
    <header style={{ display: 'flex', justifyContent: 'space-between', padding: '20px 40px', backgroundColor: '#eef6fc', alignItems: 'center' }}>
      <div>
        <Link href="/" style={{ textDecoration: 'none', display: 'block' }}>
          <Image 
            src="/logo.png" 
            alt="PROTO SPACE" 
            width={220} 
            height={40} 
            style={{ objectFit: 'contain' }}
            priority
          />
        </Link>
      </div>
      <div style={{ display: 'flex', gap: '20px', alignItems: 'center' }}>
        {isLoggedIn ? (
          // ログイン時の表示
          <>
            <button 
              onClick={() => setIsLoggedIn(false)} 
              style={{ border: 'none', background: 'none', color: '#30a3f0', cursor: 'pointer', fontSize: '14px' }}
            >
              ログアウト
            </button>
            <Link href="/new" style={{ border: '1px solid #30a3f0', color: '#30a3f0', padding: '8px 24px', textDecoration: 'none', fontSize: '14px', backgroundColor: 'transparent' }}>
              New Proto
            </Link>
          </>
        ) : (
          // ログアウト時の表示
          <>
            <Link href="/login" style={{ border: 'none', background: 'none', color: '#30a3f0', cursor: 'pointer', fontSize: '14px', textDecoration: 'none' }}>
              ログイン
            </Link>
            <Link href="/register" style={{ border: '1px solid #30a3f0', color: '#30a3f0', padding: '8px 24px', textDecoration: 'none', fontSize: '14px', backgroundColor: 'transparent' }}>
              新規登録
            </Link>
          </>
        )}
      </div>
    </header>
  );
}