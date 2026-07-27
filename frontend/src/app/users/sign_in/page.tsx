'use client';
import { useForm } from 'react-hook-form';
import { useAuthContext } from '@/app/context/AuthContext';
import { useState } from 'react';
import { useRouter } from 'next/navigation';
import styles from '../style.module.css';

interface LoginCredentials {
  email: string;
  password: string;
}


// ログインページ表示
const LoginPage = () => {
  const router = useRouter();
  const { register, handleSubmit, formState: { errors } } = useForm<LoginCredentials>();
  const [errorMessages, setErrorMessages] = useState<string[]>([]);
  const { login } = useAuthContext();

  // ログイン送信
  const onSubmit = async(formData: LoginCredentials) => {
    try {
      await login(formData.email, formData.password);
      router.push('/');
    } catch (error) {
      setErrorMessages([error instanceof Error ? error.message : 'エラーが発生しました']);
    }
  };

  // HTMLを返す
  return (
    <>
      <div className={styles.contents}>
        <div className={styles.container}>
          <h2 className={styles.page_heading}>ユーザーログイン</h2>
          {errorMessages.map((error, index) => (
            <div key={index} className={styles.error}>{error}</div>
          ))}

          <form onSubmit={handleSubmit(onSubmit)} className={styles.user}>
            <div className={styles.field}>
              <label className={styles.heading}>メールアドレス</label><br />
              {errors.email && <span className={styles.error}>{errors.email.message}</span>}
              <input
                type="email"
                className={styles.input_field}
                {...register('email', { required: 'メールアドレスは必須です', pattern: { value: /^[^\s@]+@[^\s@]+$/, message: '無効なメールアドレスです' } })}
              />
            </div>

            <div className={styles.field}>
              <label className={styles.heading}>パスワード(6文字以上)</label><br />
              {errors.password && <span className={styles.error}>{errors.password.message}</span>}
              <input
                type="password"
                className={styles.input_field}
                {...register('password', { required: 'パスワードは必須です', minLength: { value: 6, message: 'パスワードは6文字以上である必要があります' } })}
              />
            </div>
            
            <div className={styles.actions}>
              <input type="submit" className={styles.form_btn} value="ログイン" />
            </div>
          </form>
        </div>
      </div>
    </>
  );
};

export default LoginPage;