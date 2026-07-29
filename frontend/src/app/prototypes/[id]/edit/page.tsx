"use client"

import { fetchPrototype, updatePrototype } from "@/lib/prototypeApi"
import { PrototypeEdit } from "@/components/PrototypeEdit"
import { PrototypeEditData } from "@/lib/PrototypeEditData"
import { useParams } from "next/navigation"
import { useRouter } from "next/navigation"
import { useEffect, useState } from "react"
import { PrototypeData } from "@/lib/prototypeData"
import { useAuthContext } from "@/app/context/AuthContext"

export default function PrototypeEditPage() {
  const params = useParams();
  const router = useRouter();
  const {user, isLoading: isAuthLoading } = useAuthContext();
  const prototypeId = params.id as string;


  const [initialData, setInitialData] = useState<PrototypeData | undefined>();
  const [isSubmitting, setIsSubmitting]=useState(false);
  const [loading, setLoading] =useState<boolean>(true); //画面開いてからAPIからデータが届くまでの間。初期から読み込みしてるので初期値はtrue


  useEffect(() => {
    if (isAuthLoading)  //ログイン承認終わるまで待つ
      return;
   
      //未ログインの処理
  if (!user) { 
    router.replace('/users/sign_in');
    return;
  }

  //APIのとこ
    if(prototypeId) {
      fetchPrototype(prototypeId) //SpringBootへリクエストを飛ばす
      .then((data) => { //データの取得成功したら
        console.log("APIから届いたデータ(data):", data);
        const postUserId = data.userId;
        console.log("判定したpostUserId:", postUserId);

  console.log("投稿者のID(data?.user?.id):", data.userId);
  console.log("ログイン中のユーザーID(user?.id):", user?.id);
if (user && postUserId !== undefined && String(postUserId) !== String(user.id)) {           
            console.log("不一致のためトップページへリダイレクト");
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


  const handleSubmit = async (editData: FormData) => {
    setIsSubmitting(true)
  
  try {
    await updatePrototype(prototypeId, editData);

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
  

  if  (!initialData) {
    return <div> データが見つかりません</div>;
  }  

  return (
    <PrototypeEdit 
    initialData={initialData}
    onSubmit={handleSubmit}
    isSubmitting={isSubmitting}/>
  );

}