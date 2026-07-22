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


'use client';

import { useState } from 'react';
import { PrototypeFormData } from '../prototypes/new/page';

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

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      {errorMessages.length > 0 && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
          <ul className="list-disc list-inside">
            {errorMessages.map((msg, index) => (
              <li key={index}>{msg}</li>
            ))}
          </ul>
        </div>
      )}

      <div>
        <label className="block text-sm font-medium mb-1">プロトタイプの名称</label>
        <input
          type="text"
          name="title"
          value={formData.title}
          onChange={handleChange}
          required
          className="w-full p-2 border rounded-md"
          placeholder="プロトタイプ名を入力"
        />
      </div>

      <div>
        <label className="block text-sm font-medium mb-1">キャッチコピー</label>
        <input
          type="text"
          name="catchcopy"
          value={formData.catchcopy}
          onChange={handleChange}
          required
          className="w-full p-2 border rounded-md"
          placeholder="キャッチコピーを入力"
        />
      </div>

      <div>
        <label className="block text-sm font-medium mb-1">コンセプト</label>
        <textarea
          name="concept"
          value={formData.concept}
          onChange={handleChange}
          required
          rows={4}
          className="w-full p-2 border rounded-md"
          placeholder="コンセプトを入力"
        />
      </div>

      <div>
        <label className="block text-sm font-medium mb-1">画像URL (任意)</label>
        <input
          type="text"
          name="image"
          value={formData.image}
          onChange={handleChange}
          className="w-full p-2 border rounded-md"
          placeholder="https://example.com/image.png"
        />
      </div>

      <button
        type="submit"
        className="w-full bg-blue-600 text-white font-bold py-2 rounded-md hover:bg-blue-700 transition"
      >
        送信する
      </button>
    </form>
  );
};

export default PrototypeForm;