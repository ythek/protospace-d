"use client";

import { useEffect, useState } from "react";
import { useAuthContext } from '../app/context/AuthContext';
import { useRouter } from "next/navigation";
import { likePrototype } from "@/lib/prototypeApi";
import styles from './PrototypeLike.module.css';


interface LikeButtonProps { //親コンポーネントからの受け皿　この３つが親から渡されれば、このPrototypeLikeが動く
  prototypeId : number
  initialLikeCheck : boolean;
  initialLikeCount :number

}

export const PrototypeLike = ({ //親から受け取った情報を直接とりだしてこれら↓の道具を使ってPrototypeLikeを作成します
  prototypeId,
  initialLikeCount,
  initialLikeCheck,
  } : LikeButtonProps) => {

    const [isLiked, setIsLiked] = useState<boolean>(initialLikeCheck);
    const [likeCount, setLikeCount] = useState<number>(initialLikeCount);
    const [isLoading, setIsLoading] = useState(false);
  
  //ログインしてるかどうか確認
  const { user }= useAuthContext();;
  const isLoggedIn = user?.isAuthenticated ?? false;
  const router = useRouter();

  
  useEffect(() => {
  setIsLiked(initialLikeCheck);
  setLikeCount(initialLikeCount);
}, [initialLikeCheck, initialLikeCount]);


  const handelLikeToggle = async () => {
    if (!isLoggedIn) {
      alert('いいね機能を使用するにはログインしてください')
      router.push('/users/sign_in')
      return;
    } 

    if (isLoading) //連打禁止
      return;

      //以下からこの処理がないといいね押した後バックエンドの通信に時間がかかりユーザーに反映されるのが遅れてしまう
    const previousIsLiked = isLiked;
    const previousLikeCount = likeCount;

    //押した後の状態を計算
    //現在いいねしているなら解除、いいねしていないなら追加
    const nextIsLiked = !isLiked; //もともとfalseスタートだから!isLikedの時がtrue
    const nextLikeCount = nextIsLiked ? likeCount + 1 : likeCount -1;

    //最初に押した状態にしてあとからバックエンドに通信
    setIsLiked(nextIsLiked);
    setLikeCount(nextLikeCount);
    //通信が終わるまで連打だめ
    setIsLoading(true);

    try {
      await likePrototype(prototypeId); //APIをぼこぼこにたたく
    } catch ( error: any) { //エラーがでたら以前の言い値数に戻す
      setIsLiked(previousIsLiked);
      setLikeCount(previousLikeCount);
    } finally {
      setIsLoading(false);
    }

  }
  
    return (
    <button 
      onClick={handelLikeToggle}
      disabled={isLoading}
      className={`${styles.likeButton} ${isLiked ? styles.liked : ''}`}
    >
      <span className={styles.heart}>{isLiked ? '♥' : '♡'}</span>
      <span>{likeCount}：LIKE</span>
    </button>
    );
  
}


