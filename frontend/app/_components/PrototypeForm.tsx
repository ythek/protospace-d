// import { error } from 'console';
// import { defaultConfig } from 'next/dist/server/config-shared';
// import { useForm } from 'react-hook-form';

// interface PrototypeForm{
//   text: string;
//   image: string;
// }

// interface PrototypeFormProps{
//   initialData: PrototypeForm;
//   errorMessages: string[];
//   onSubmit: (data: PrototypeForm) => void;
// }

// const PrototypeForm = ({ errorMessages, onSubmit, initialData }: PrototypeFormProps) => {

//   const { register, handleSubmit, formState: { errors } } = useForm<PrototypeForm>({
//     defaultValues: initialData
//   });

//   return (
//     <form onSubmit={handleSubmit(onSubmit)}>
//       {errorMessages.map((error, index) =>(
//         <div key={index} className="error-message">{error}</div>
//       ))}

//       {errors.image && <span className="error-message">{errors.image.message}</span>}
//       <input
//        type="text"
//        {...register('image')}
//        placeholder='Image Url'
//       />

//       {errors.text && <span className="error-message">{errors.text.message}</span>}
//       <textarea
//         {...register('text', { required: "Text can't be blanck"})}
//         placeholder='text'
//         rows={10}
//       />

//       <input type="submit" value="SEND" /> 
//     </form>
//   );
// };

// export default PrototypeForm;


// 'use client';

// import { useState } from 'react';
// import { PrototypeFormData } from '../prototypes/new/page';

// interface PrototypeFormProps {
//   initialData: PrototypeFormData;
//   errorMessages: string[];
//   onSubmit: (formData: PrototypeFormData) => void;
// }

// export const PrototypeForm = ({
//   initialData,
//   errorMessages,
//   onSubmit,
// }: PrototypeFormProps) => {
//   const [formData, setFormData] = useState<PrototypeFormData>(initialData);

//   const handleChange = (
//     e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
//   ) => {
//     const { name, value } = e.target;
//     setFormData((prev) => ({
//       ...prev,
//       [name]: value,
//     }));
//   };

//   const handleSubmit = (e: React.FormEvent) => {
//     e.preventDefault();
//     onSubmit(formData);
//   };

//   return (
//     <form onSubmit={handleSubmit} className="space-y-4">
//       {errorMessages.length > 0 && (
//         <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
//           <ul className="list-disc list-inside">
//             {errorMessages.map((msg, index) => (
//               <li key={index}>{msg}</li>
//             ))}
//           </ul>
//         </div>
//       )}

//       <div>
//         <label className="block text-sm font-medium mb-1">プロトタイプの名称</label>
//         <input
//           type="text"
//           name="title"
//           value={formData.title}
//           onChange={handleChange}
//           required
//           className="w-full p-2 border rounded-md"
//           placeholder="プロトタイプ名を入力"
//         />
//       </div>

//       <div>
//         <label className="block text-sm font-medium mb-1">キャッチコピー</label>
//         <input
//           type="text"
//           name="catchcopy"
//           value={formData.catchcopy}
//           onChange={handleChange}
//           required
//           className="w-full p-2 border rounded-md"
//           placeholder="キャッチコピーを入力"
//         />
//       </div>

//       <div>
//         <label className="block text-sm font-medium mb-1">コンセプト</label>
//         <textarea
//           name="concept"
//           value={formData.concept}
//           onChange={handleChange}
//           required
//           rows={4}
//           className="w-full p-2 border rounded-md"
//           placeholder="コンセプトを入力"
//         />
//       </div>

//       <div>
//         <label className="block text-sm font-medium mb-1">画像URL</label>
//         <input
//           type="text"
//           name="image"
//           value={formData.image}
//           onChange={handleChange}
//           className="w-full p-2 border rounded-md"
//           placeholder="https://example.com/image.png"
//         />
//       </div>

//       <button
//         type="submit"
//         className="w-full bg-blue-600 text-white font-bold py-2 rounded-md hover:bg-blue-700 transition"
//       >
//         送信する
//       </button>
//     </form>
//   );
// };

// export default PrototypeForm;
// 'use client';

// import { useState } from 'react';
// import { PrototypeFormData } from '../prototypes/new/page';
// // ① 独自のCSSファイルをインポート（パスは実際の配置場所に合わせて調整してください）
// import '@/app/styles/style.css';

// interface PrototypeFormProps {
//   initialData: PrototypeFormData;
//   errorMessages: string[];
//   onSubmit: (formData: PrototypeFormData) => void;
// }

// export const PrototypeForm = ({
//   initialData,
//   errorMessages,
//   onSubmit,
// }: PrototypeFormProps) => {
//   const [formData, setFormData] = useState<PrototypeFormData>(initialData);

//   const handleChange = (
//     e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
//   ) => {
//     const { name, value } = e.target;
//     setFormData((prev) => ({
//       ...prev,
//       [name]: value,
//     }));
//   };

//   const handleSubmit = (e: React.FormEvent) => {
//     e.preventDefault();
//     onSubmit(formData);
//   };

//   return (
//     // ② 独自のクラス名（例: form-container, form-group, form-input など）を適用
//     <form onSubmit={handleSubmit} className="form-container">
//       {errorMessages.length > 0 && (
//         <div className="error-box">
//           <ul className="error-list">
//             {errorMessages.map((msg, index) => (
//               <li key={index}>{msg}</li>
//             ))}
//           </ul>
//         </div>
//       )}

//       <div className="form-group">
//         <label className="form-label">プロトタイプの名称</label>
//         <input
//           type="text"
//           name="title"
//           value={formData.title}
//           onChange={handleChange}
//           required
//           className="form-input"
//           placeholder="プロトタイプ名を入力"
//         />
//       </div>

//       <div className="form-group">
//         <label className="form-label">キャッチコピー</label>
//         <input
//           type="text"
//           name="catchcopy"
//           value={formData.catchcopy}
//           onChange={handleChange}
//           required
//           className="form-input"
//           placeholder="キャッチコピーを入力"
//         />
//       </div>

//       <div className="form-group">
//         <label className="form-label">コンセプト</label>
//         <textarea
//           name="concept"
//           value={formData.concept}
//           onChange={handleChange}
//           required
//           rows={4}
//           className="form-textarea"
//           placeholder="コンセプトを入力"
//         />
//       </div>

//       <div className="form-group">
//         <label className="form-label">画像URL</label>
//         <input
//           type="text"
//           name="image"
//           value={formData.image}
//           onChange={handleChange}
//           className="form-input"
//           placeholder="https://example.com/image.png"
//         />
//       </div>

//       <button type="submit" className="form-submit-btn">
//         送信する
//       </button>
//     </form>
//   );
// };

// export default PrototypeForm;

// 'use client';

// import { useState } from 'react';
// import { PrototypeFormData } from '../prototypes/new/page';
// import '@/app/styles/style.css';

// interface PrototypeFormProps {
//   initialData: PrototypeFormData;
//   errorMessages: string[];
//   onSubmit: (formData: PrototypeFormData) => void;
// }

// export const PrototypeForm = ({
//   initialData,
//   errorMessages,
//   onSubmit,
// }: PrototypeFormProps) => {
//   const [formData, setFormData] = useState<PrototypeFormData>(initialData);
//   // 画像のプレビュー用URLを管理するステート
//   const [previewUrl, setPreviewUrl] = useState<string | null>(null);

//   const handleChange = (
//     e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
//   ) => {
//     const { name, value } = e.target;
//     setFormData((prev) => ({
//       ...prev,
//       [name]: value,
//     }));
//   };

//   // ★ 画像ファイル選択時のハンドラー
//   const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
//     const file = e.target.files?.[0] || null;
    
//     // formData 側の image に File オブジェクトをセット
//     setFormData((prev) => ({
//       ...prev,
//       image: file,
//     }));

//     // プレビュー表示用の処理
//     if (file) {
//       const url = URL.createObjectURL(file);
//       setPreviewUrl(url);
//     } else {
//       setPreviewUrl(null);
//     }
//   };

//   const handleSubmit = (e: React.FormEvent) => {
//     e.preventDefault();
//     onSubmit(formData);
//   };

//   return (
//     <form onSubmit={handleSubmit} className="form-container">
//       {errorMessages.length > 0 && (
//         <div className="error-box">
//           <ul className="error-list">
//             {errorMessages.map((msg, index) => (
//               <li key={index}>{msg}</li>
//             ))}
//           </ul>
//         </div>
//       )}

//       <div className="form-group">
//         <label className="form-label">プロトタイプの名称</label>
//         <input
//           type="text"
//           name="title"
//           value={formData.title}
//           onChange={handleChange}
//           required
//           className="form-input"
//           placeholder="プロトタイプ名を入力"
//         />
//       </div>

//       <div className="form-group">
//         <label className="form-label">キャッチコピー</label>
//         <input
//           type="text"
//           name="catchcopy"
//           value={formData.catchcopy}
//           onChange={handleChange}
//           required
//           className="form-input"
//           placeholder="キャッチコピーを入力"
//         />
//       </div>

//       <div className="form-group">
//         <label className="form-label">コンセプト</label>
//         <textarea
//           name="concept"
//           value={formData.concept}
//           onChange={handleChange}
//           required
//           rows={4}
//           className="form-textarea"
//           placeholder="コンセプトを入力"
//         />
//       </div>

//       {/* ★ 画像のファイル選択フォーム領域 */}
//       <div className="form-group">
//         <label className="form-label">プロトタイプの画像</label>
//         <input
//           type="file"
//           name="image"
//           accept="image/*"
//           onChange={handleImageChange}
//           className="form-input-file"
//         />
        
//         {/* 選択した画像のプレビュー表示 */}
//         {previewUrl && (
//           <div style={{ marginTop: '10px' }}>
//             <img
//               src={previewUrl}
//               alt="選択した画像のプレビュー"
//               style={{ maxWidth: '200px', maxHeight: '200px', borderRadius: '4px' }}
//             />
//           </div>
//         )}
//       </div>

//       <button type="submit" className="form-submit-btn">
//         送信する
//       </button>
//     </form>
//   );
// };

// export default PrototypeForm;
'use client';

import { useState } from 'react';
import { PrototypeFormData } from '../prototypes/new/page';
import '@/app/styles/style.css';

interface PrototypeFormProps {
  initialData: PrototypeFormData;
  errorMessages: string[];
  onSubmit: (formData: PrototypeFormData) => void;
}

export const PrototypeForm = ({
  initialData,
  errorMessages,
  onSubmit,
}: PrototypeFormProps) => {
  const [formData, setFormData] = useState<PrototypeFormData>(initialData);
  const [previewUrl, setPreviewUrl] = useState<string | null>(null);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  // ★ 画像ファイルを File オブジェクトとして formData に保持する処理
  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    // File オブジェクトを imageFile プロパティに保存
    setFormData((prev) => ({
      ...prev,
      imageFile: file,
    }));

    // 画面でのプレビュー用に一時的なブラウザ内URLを生成
    setPreviewUrl(URL.createObjectURL(file));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <form onSubmit={handleSubmit} className="form-container">
      {errorMessages.length > 0 && (
        <div className="error-box">
          <ul className="error-list">
            {errorMessages.map((msg, index) => (
              <li key={index}>{msg}</li>
            ))}
          </ul>
        </div>
      )}

      <div className="form-group">
        <label className="form-label">プロトタイプの名称</label>
        <input
          type="text"
          name="title"
          value={formData.title}
          onChange={handleChange}
          required
          className="form-input"
          placeholder="プロトタイプ名を入力"
        />
      </div>

      <div className="form-group">
        <label className="form-label">キャッチコピー</label>
        <input
          type="text"
          name="catchcopy"
          value={formData.catchcopy}
          onChange={handleChange}
          required
          className="form-input"
          placeholder="キャッチコピーを入力"
        />
      </div>

      <div className="form-group">
        <label className="form-label">コンセプト</label>
        <textarea
          name="concept"
          value={formData.concept}
          onChange={handleChange}
          required
          rows={4}
          className="form-textarea"
          placeholder="コンセプトを入力"
        />
      </div>

      {/* ファイル選択 UI */}
      <div className="form-group">
        <label className="form-label">プロトタイプの画像</label>
        <input
          type="file"
          accept="image/*"
          onChange={handleImageChange}
          className="form-input-file"
        />
        
        {/* プレビュー表示 */}
        {previewUrl && (
          <div style={{ marginTop: '10px' }}>
            <img
              src={previewUrl}
              alt="選択した画像のプレビュー"
              style={{ maxWidth: '200px', maxHeight: '200px', borderRadius: '4px' }}
            />
          </div>
        )}
      </div>

      <button type="submit" className="form-submit-btn">
        送信する
      </button>
    </form>
  );
};

export default PrototypeForm;