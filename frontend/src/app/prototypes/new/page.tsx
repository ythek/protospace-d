'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Header from '@/app/components/Header';
import Footer from '@/app/components/Footer';
import PrototypeForm from '@/app/components/PrototypeForm';
// ★ インポートパスと関数名を修正
import { createPrototype, PrototypeFormData } from '@/app/lib/prototypeApi';

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
      // ★ 直接 fetch していたのを削除して、共通関数を呼ぶ
      await createPrototype(data);

      router.push('/');
      router.refresh();
    } catch (err: any) {
      console.error(err);

      // Axiosのエラーハンドリング
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