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