"use client"

import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import { PrototypeData } from "@/lib/prototypeData";
import { fetchPrototypeById } from "@/lib/prototypeApi";
import PrototypeDetail from "@/components/PrototypeDetail";



export default function PrototypeDetailPage() {
  const params = useParams();
  const prototypeId = params.id as string;
  const [prototype, setPrototype]= useState<PrototypeData | null>(null);
  const [isNotFound, setIsNotFound] = useState(false);
  useEffect(() => {
    if (prototypeId) {
      fetchPrototypeById(prototypeId)
      .then((data) => 
        setPrototype(data)                  
      )
      .catch((err) => {
        console.error("データ取得失敗:", err);
        if (err.response && err.response.status === 404) {
            setIsNotFound(true);
          } else {
            setIsNotFound(true);
          }
       });
    }
  }, [prototypeId]);

  if (isNotFound) {
  return (
    <div>
      <h1>404 Not Found</h1>
      <p>存在しないプロトタイプです</p>
    </div>
    );
  }

    if (!prototype) return <div>読み込み中・・・</div>;

  return (<PrototypeDetail
    prototype={prototype} 
    />);
}