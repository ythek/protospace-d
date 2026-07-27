"use client"

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import { PrototypeData } from "@/app/lib/prototypeData";
import { fetchPrototypeById } from "@/app/lib/prototypeApi";
import PrototypeDetail from "@/app/components/PrototypeDetail";



export default function PrototypeDetailPage() {
  const params = useParams();
  const prototypeId = params.id as string;
  const [prototype, setPrototype]= useState<PrototypeData | null>(null);
  //const [currentUserName, serCurrentUserName]= useState<string | null> ("")
  useEffect(() => {
    if (prototypeId) {
      fetchPrototypeById(prototypeId)
      .then((data) => setPrototype(data))
      .catch((err) => console.error("データ取得失敗:", err))
    }
  }, [prototypeId]);

    if (!prototype) return <div>読み込み中・・・</div>;

  return (<PrototypeDetail
    prototype={prototype} 
    //currentUserName={currentUserName}
    />);
}