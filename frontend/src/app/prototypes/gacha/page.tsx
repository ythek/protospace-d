'use client';

import PrototypeView from "@/components/PrototypeView";

import { useEffect, useState } from "react";

import styles from './style.module.css';

import { useAuthContext } from "@/app/context/AuthContext";
import { fetchPrototypeGacha } from "@/lib/prototypeApi";
import { PrototypeGachaData } from "@/lib/PrototypeGachaData";


export default function UserDetail() {
  const [prototype, setPrototypes] = useState<PrototypeGachaData>();
  const [isLoading, setIsLoading] = useState(true);
  const [rarity, setRarity] = useState<string>();
  const { user } = useAuthContext();

  useEffect(() => {
    const getPrototypeGacha = async () => {
      try {
        const prototype = await fetchPrototypeGacha();
        setPrototypes(prototype);
        setRarity(calc(prototype.rarity));
      } catch (error) {
        console.error("取得に失敗しました", error);
      } finally {
        setIsLoading(false)
          ;
      }
    };
    getPrototypeGacha();
  }, []);

  const calc = (value: number) => {
    let rarity = "";
    if (value > 9990) {
      rarity = "LR";  //9991～10000 0.1%
    } else if (value > 9950) {
      rarity = "UR";   //9951～9990 0.4%
    } else if (value > 9500) {
      rarity = "SSR";   //9501～9950 4.5%
    } else if (value > 8700) {
      rarity = "SR";     //8701～9500 8.0%
    } else if (value > 7000) {
      rarity = "R";       //7001～8700 17.0%
    } else if (value > 4500) {
      rarity = "UC";       //4501～7000 25.0%
    } else {
      rarity = "C";     // 1〜4500 45.0%
    }
    return rarity;
  }

  if (isLoading) {
    <p>読み込み中…</p>
  } else {

    if (prototype?.id) {

      return (
        <div className={styles.area}>
          <table className={styles.userinfo}>
            <tbody>
              <tr>
                <td className={styles.recordname}>RANK</td>
                <td>{rarity}</td>
              </tr>
              <tr>
                <td className={styles.recordname}>TITLE</td>
                <td>{prototype.title}</td>
              </tr>
              <tr>
                <td className={styles.recordname}>ATK</td>
                <td>{prototype.attack}</td>
              </tr>
              <tr>
                <td className={styles.recordname}>DEF</td>
                <td>{prototype.defense}</td>
              </tr>
            </tbody>
          </table>
          <div className={styles.grid}>
            <PrototypeView key={prototype.id} prototype={prototype} />
          </div>
        </div>
      );
    }

    return (
      <div>
        <h1>プロトタイプは見つかりませんでした</h1>
      </div>
    );
  }
}