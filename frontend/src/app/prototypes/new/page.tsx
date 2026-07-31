'use client';

import { useState } from 'react';
import { redirect, useRouter } from 'next/navigation';
import PrototypeForm from '@/components/PrototypeForm';
import { createPrototype, PrototypeFormData } from '@/lib/prototypeApi';
import { useAuthContext } from '@/app/context/AuthContext';
const CreatePrototypePage = () => {
  const router = useRouter();
  const [errorMessages, setErrorMessages] = useState<string[]>([]);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const {user, isLoading} = useAuthContext();

  const initialFormData: PrototypeFormData = {
    title: '',
    catchcopy: '',
    concept: '',
    imageFile: null,
  };

  const handleSubmit = async (data: PrototypeFormData) => {
    setErrorMessages([]);

    if (isSubmitting) 
      return;

    setErrorMessages([]);

    try {
      // 投稿処理を実行
      setIsSubmitting(true);
      await createPrototype(data);
    
      // キャッシュを更新してトップ（一覧画面）へ遷移
     
      router.refresh();
      router.push('/');

    } catch (err: any) {
      console.error(err);

      if (err.response?.data?.messages && Array.isArray(err.response.data.messages)) {
        setErrorMessages(err.response.data.messages);
      } else {
        setErrorMessages(['投稿に失敗しました']);
      }
    }finally {
      setIsSubmitting(false);

    }
  };
  if(isLoading) return <div>読み込み中</div>
  if(!user)  return redirect('/users/sign_in');
  
  return (
    <div className="contents row">
      <div className="container">
        <h3 className="form-title">新規プロトタイプ投稿</h3>
        <PrototypeForm
          initialData={initialFormData}
          errorMessages={errorMessages}
          onSubmit={handleSubmit}
        />
      </div>
    </div>
  );
};

export default CreatePrototypePage;