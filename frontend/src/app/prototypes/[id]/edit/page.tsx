"use client"

import { fetchPrototype, updatePrototype } from "@/app/lib/prototypeApi"
import { PrototypeEdit } from "@/app/components/PrototypeEdit"
import { PrototypeEditData } from "@/app/lib/PrototypeEditData"
import { useParams } from "next/navigation"
import { useRouter } from "next/navigation"
import { useEffect, useState } from "react"
import { PrototypeData } from "@/app/lib/prototypeData"
import { useAuthContext } from "@/app/context/AuthContext"

export default function PrototypeEditPage() {
  const params = useParams();
  const router = useRouter();
  const {user, isLoading: isAuthLoading } = useAuthContext();
  const prototypeId = params.id as string;


  const [initialData, setInitialData] = useState<PrototypeData | undefined>();
  const [isSubmitting, setIsSubmitting]=useState(false);
  const [loading, setLoading] =useState<boolean>(true); //画面開いてからAPIからデータが届くまでの間。初期から読み込みしてるので初期値はtrue
//未ログインの処理
  if (!user) { 
    router.replace('/login');
    return;
  }
//投稿者IDとログインIDが一致しない場合
  if(initialData?.user?.id !== user.id) {
  router.replace('/');
  return;
  }

  useEffect(() => {
    if (isAuthLoading)  //ログイン承認終わるまで待つ
      return;

      //未ログインの処理
  if (!user) { 
    router.replace('/login');
    return;
  }

  //APIのとこ
    if(prototypeId) {
      fetchPrototype(prototypeId) //SpringBootへリクエストを飛ばす
      .then((data) => { //データの取得成功したら
        if  (String(data?.user?.id) !== String(user.id)) {
          router.replace('/')
          return;
        }
        setInitialData(data); 
      })
      .catch((err) => {
        console.error("エラー: ", err);
      })
      .finally(() => {
        setLoading(false);
      });
    }
  }, [prototypeId, user, isAuthLoading, router]); //IDが変わったときにUseEffectを処理する


  const handleSubmit = async (editData: PrototypeEditData) => {
    setIsSubmitting(true)
  
  try {
    await updatePrototype(prototypeId, editData as PrototypeData)

    router.push(`/prototypes/${prototypeId}`);
  } catch (err) {
    console.error("更新失敗", err);
  } finally {
    setIsSubmitting(false);
  }
  };


  if (isAuthLoading || loading) {
    return <div> 読み込み中...</div>;
  }
  
  if(loading) { 
    return <div>読み込み中…</div>;
  }

  if (!initialData) {
    return <div> データが見つかりません</div>;
  }  

  return (
    <PrototypeEdit 
    initialData={initialData}
    onSubmit={handleSubmit}
    isSubmitting={isSubmitting}/>
  );

}