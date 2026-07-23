'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Header from '@/app/_components/Header';
import Footer from '@/app/_components/Footer';
import PrototypeForm from '@/app/_components/PrototypeForm';

// ★ 画像は File オブジェクトとして扱う（null 許容）
export interface PrototypeFormData {
  title: string;
  catchcopy: string;
  concept: string;
  imageFile: File | null;
}

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
      // ★ JSON ではなく FormData オブジェクトを作成する
      const formData = new FormData();
      formData.append('title', data.title);
      formData.append('catchcopy', data.catchcopy);
      formData.append('concept', data.concept);

      // 画像ファイルが選択されていれば append
      if (data.imageFile) {
        formData.append('imageFile', data.imageFile);
      }

      // ★ multipart/form-data で送信する
      // (headers に Content-Type を指定しないことで、ブラウザが自動的に境界値を含めた Content-Type を設定してくれます)
      const res = await fetch('http://localhost:8080/api/prototypes', {
        method: 'POST',
        body: formData,
      });

      if (!res.ok) {
        const errorData = await res.json();
        if (Array.isArray(errorData.messages)) {
          setErrorMessages(errorData.messages);
        } else {
          setErrorMessages(['投稿に失敗しました']);
        }
        return;
      }

      router.push('/');
      router.refresh();
    } catch (err) {
      console.error(err);
      setErrorMessages(['通信エラーが発生しました']);
    }
  };

  return (
    <>
      <Header />
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
      <Footer />
    </>
  );
};

export default CreatePrototypePage;