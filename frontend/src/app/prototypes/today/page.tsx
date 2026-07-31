"use client"

import { useEffect, useState } from "react";
import { PrototypeData } from "@/lib/prototypeData";
import { fetchPrototypeToday } from "@/lib/prototypeApi";
import { useAuthContext } from "@/app/context/AuthContext";
import styles from './style.module.css';
import PrototypeView from "@/components/PrototypeView";



export default function PrototypeTodayPage() {
  const [prototype, setPrototype] = useState<PrototypeData | null>(null);
  const [luck, setLuck] = useState(7);
  const { user } = useAuthContext();


  useEffect(() => {
    const getPrototypesToday = async () => {
      try {
        const data = await fetchPrototypeToday();
        setPrototype(data.prototype);
        setLuck(data.luck);
      } catch (error) {
        console.error("取得に失敗しました", error);
      }
    };
    getPrototypesToday();
  }
    , []);

  if (!user) return <div>読み込み中・・・</div>;

  if (!prototype || luck == 7) {
    return (
      <div>
        <h1>大凶</h1>
        <p>運勢の取得に何らかの問題が生じたため、今日は最悪な一日になることでしょう。</p>
      </div>
    );
  }

  const result = {
    0: "凶",
    1: "吉",
    2: "大吉",
    3: "小吉",
    4: "末吉",
    5: "小吉",
    6: "凶"
  }[luck] ?? "大凶";

  const message = {
    "凶" : "今日は何をしてもうまくいかない日。",
    "小吉" : "今日はちょっとだけいいことがあるかもしれない日。",
    "末吉" : "今日はいつもと変わらない平凡な一日。",
    "大吉" : "今日はすべてがうまくいく日。",
    "吉" : "今日はいい日。",
  }[result] ?? "";

  return (
    <main className={styles.container}>
      <h1>今日の運勢は{result}</h1>
      <p>{message}</p>

      <h3 className={styles.title}>おすすめのラッキープロトタイプ👇</h3>
      <div className={styles.grid}>
        <PrototypeView key={prototype.id} prototype={prototype} />
      </div>
    </main>
  );
}