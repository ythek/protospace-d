'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import PrototypeForm from '@/components/PrototypeForm';
import { createPrototype, PrototypeFormData } from '@/lib/prototypeApi';
const CreatePrototypePage = () => {
  const router = useRouter();
  const [errorMessages, setErrorMessages] = useState<string[]>([]);

  const initialFormData: PrototypeFormData = {
    title: '',
    catchcopy: '',
    concept: '',
    imageFile: null,
  };

  const handleSubmit = async (data: PrototypeFormData) => {
    setErrorMessages([]);

    try {
      // 投稿処理を実行
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
    }
  };

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