//子
"use client"

import React, { useEffect, useState } from "react";
import { PrototypeEditData } from "../lib/PrototypeEditData";
import { PrototypeData } from "../lib/prototypeData";

import styles from './PrototypeEdit.module.css';

type Props = {
initialData?: PrototypeData; //オプショナル
onSubmit: (editData: PrototypeEditData) => void;
isSubmitting?: boolean;
};

export const PrototypeEdit = ({ initialData, onSubmit, isSubmitting = false }: Props) => {
  const [editData, setEditData]= useState<PrototypeEditData>({
    title: '',
    catchcopy: '',
    concept: '',
    image: ''
  });

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
      setEditData((prev) => ({ //prevは編集前データ
        ...prev, //スプレッド構文で変更されていないデータが消えるの防ぐ。全部を一度コピー
        [name]: value,  //対象のみを上書き　
      }));
    }

    //ファイル変更時の処理
  //const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
  //if (e.target.files && e.target.files[0]) {
    //const file = e.target.files[0];
   // const reader = new FileReader();

    // ファイルの読み込みが完了した時の処理
    //reader.onloadend = () => {
      //setEditData((prev) => ({
        //...prev,
       // image: reader.result as string, 
      //}));
   // };

    //reader.readAsDataURL(file);
 // }
//};
    //送信時の処理
    const handleSubmit = (e: React.FormEvent) => {
      e.preventDefault();
    
  
    if (
      !editData.title.trim() ||
      !editData.catchcopy.trim() ||
      !editData.concept.trim() ||
      !editData.image.trim()
    ) {
      return;
    }
  //バリデーション
  if (editData.title.length > 128) {
    alert('タイトルは128文字以内で入力してください。');
    return; 
  }

  if (editData.catchcopy.length > 128) {
    alert('キャッチコピーは128文字以内で入力してください。');
    return;
  }

  if (editData.concept.length > 128) {
    alert('コンセプトは128文字以内で入力してください。');
    return;
  }


    onSubmit(editData);
  };


  return (
    <div className={styles.container}>
      <h2>プロトタイプ編集</h2>

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
          //onChange={handleFileChange}
          onChange={handleChange}/>
        </div>

        <button type="submit" className={styles.form_submit_btn} disabled={isSubmitting}>保存する</button>

        </form>

        </div>
  )
}