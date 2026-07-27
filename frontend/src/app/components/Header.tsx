"use client";

import Link from "next/link";
import Image from "next/image";
import { useState } from "react";
import { useAuthContext } from "../context/AuthContext";
import { useRouter } from "next/navigation";
import { logout } from "../api/users";

export default function Header() {
  const router = useRouter();

  // ログイン状態
  const {user, logout} = useAuthContext();
  const isLoggedIn = user?.isAuthenticated ?? false;

  // ログアウト処理
  const handleLogout = async () => {
    try {
      await logout();
      router.push('/'); 
    } catch (error) {
      console.error('ログアウト処理に失敗しました:', error);
      alert('ログアウトに失敗しました。もう一度お試しください。');
    }
  };

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
            <button onClick={handleLogout}
              style={{ border: 'none', background: 'none', color: '#30a3f0', cursor: 'pointer', fontSize: '14px' }}>
              ログアウト
            </button>
            <Link href="/new" style={{ border: '1px solid #30a3f0', color: '#30a3f0', padding: '8px 24px', textDecoration: 'none', fontSize: '14px', backgroundColor: 'transparent' }}>
              New Proto
            </Link>
          </>
        ) : (
          // ログアウト時の表示
          <>
            <Link href="/users/sign_in" style={{ border: '1px solid #30a3f0', color: '#30a3f0', padding: '8px 24px', textDecoration: 'none', fontSize: '14px', backgroundColor: 'transparent' }}>
              ログイン
            </Link>
            <Link href="/users/sign_up" style={{ border: '1px solid #30a3f0', color: '#30a3f0', padding: '8px 24px', textDecoration: 'none', fontSize: '14px', backgroundColor: 'transparent' }}>
              新規登録
            </Link>
          </>
        )}
      </div>
    </header>
  );
}