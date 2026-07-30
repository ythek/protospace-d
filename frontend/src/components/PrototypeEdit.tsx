//子
"use client"

import React, { useEffect, useState } from "react";
import { PrototypeEditData } from "../lib/PrototypeEditData";
import { PrototypeData } from "../lib/prototypeData";

import styles from './PrototypeEdit.module.css';
import { updatePrototype } from "@/lib/prototypeApi";
import { title } from "process";

type Props = {
initialData?: PrototypeData; //オプショナル
onSubmit: (editData: FormData) => void;
isSubmitting?: boolean;
};

export const PrototypeEdit = ({ initialData, onSubmit, isSubmitting = false }: Props) => {
  const [editData, setEditData]= useState<PrototypeEditData>({
    title: '',
    catchcopy: '',
    concept: '',
    image:''
  });

  const [errorMessages, setErrorMessages ] = useState<String>("");
  const [imageFile, setImageFile] = useState<File | null>(null);

  useEffect(() => {
    if (initialData) {
      setEditData({
        title: initialData.title || "",
        catchcopy: initialData.catchcopy || "",
        concept: initialData.concept || "",
        image: initialData.image || '',
      
        });
      }
    },[ initialData ]);

    //入力変更時の処理
    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
      const { name, value } = e.target; //JSXでname要素をつくるのでここはname

      if (value.trim() === '') {
        setErrorMessages('空欄にすることはできません');
      } else if (value.length >= 128) {
        setErrorMessages('128文字未満で入力してください');
      } else {
        setErrorMessages('');
      }

      setEditData((prev) => ({
        ...prev, //スプレッド構文で変更されていないデータが消えるの防ぐ。全部を一度コピー
        [name]: value,  //対象のみを上書き　
      }));
    }

    //ファイル変更時の処理
  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
  if (e.target.files && e.target.files[0]) {
    setImageFile(e.target.files[0]);
      }
  };
  
    
    //送信時の処理
    const handleSubmit = (e: React.FormEvent) => {
      e.preventDefault();

      if (editData.title.trim() === '') {
        setErrorMessages('空欄にすることはできません');
        return;
      }

      if (editData.title.length >=128) {
        setErrorMessages('128文字未満で入力してください。');
        return;
      }

      if (editData.concept.trim() === '') {
        setErrorMessages('空欄にすることはできません');
        return;
      }

      if (editData.concept.length >=128) {
        setErrorMessages('128文字未満で入力してください。');
        return;
      }

      if (editData.catchcopy.trim() === '') {
        setErrorMessages('空欄にすることはできません');
        return;
      }

      if (editData.catchcopy.length >=128) {
        setErrorMessages('128文字未満で入力してください。');
        return;
      }

        const formData = new FormData();

      const jsonBlob = new Blob([JSON.stringify(editData)], { type: "application/json" });
      formData.append("dto", jsonBlob);

      if (imageFile) {
    formData.append("image", imageFile);
  }

   onSubmit(formData);
    
};

  
  return (
    <div className={styles.container}>
      <h2>プロトタイプ編集</h2>

      {errorMessages && (
        <p className={styles.error_message}>{errorMessages}</p>
      )}

      <form onSubmit={handleSubmit}>
        <div className={styles.form_group}>
        <label htmlFor="prototype_title">プロトタイプの名称</label>
        <input 
        className={styles.form_input}
         type="text"
         id="prototype_title" 
         name="title" 
         value={editData.title}
         onChange={handleChange} />
        </div>

        <div className={styles.form_group}>
          <label htmlFor="prototype_catchcopy"> キャッチコピー</label>
          <input
          className={styles.form_input}
          type="text"
          id="prototype_catchcopy"
          name="catchcopy" 
          value={editData.catchcopy}
          onChange={handleChange} />
        </div>

        <div className={styles.form_group}>
          <label htmlFor="prototype_concept">コンセプト</label>
          <input 
          className={styles.form_textarea}
          type="text"
          id="prototype_concept"
          name="concept"
          value={editData.concept}
          onChange={handleChange}/>
        </div>

        <div className={styles.form_group}>
          <label htmlFor="prototype_image">プロトタイプの画像 </label>

          <input 
          className={styles.form_input_file}
          type="file"
          id="prototype_image"
          name="image"
          onChange={handleFileChange}
           />
         </div>

        <button type="submit" className={styles.form_submit_btn} disabled={isSubmitting}>保存する</button>

        </form>

        </div>
  )
}