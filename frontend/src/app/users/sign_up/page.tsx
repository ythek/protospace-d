'use client';
import { useForm } from 'react-hook-form';
import { signUp } from '@/app/api/users';
import { useState } from 'react';
import { useRouter } from 'next/navigation';
import styles from '../style.module.css';

interface SignUpForm {
  email: string;
  password: string;
  passwordConfirmation: string;
  username: string;
  profile: string;
  affiliation: string;
  position: string;
}

const SignUpPage = () => {
  const router = useRouter();
  const { register, handleSubmit, formState: { errors }, watch } = useForm<SignUpForm>();
  const [errorMessages, setErrorMessages] = useState<string[]>([]);

  // 新規登録処理
  const onSubmit = async (formData: SignUpForm) => {
    try {
      await signUp(formData);
      router.push('/');
    } catch (error) {
      setErrorMessages([error instanceof Error ? error.message : 'エラーが発生しました']);
    }
  }; 

  return (
    <>
      <div className={styles.contents}>
        <div className={styles.container}>
          <h2 className={styles.page_heading}>ユーザー新規登録</h2>
          {errorMessages.map((error, index) => (
            <div key={index} className={styles.error}>{error}</div>
          ))}

          <form onSubmit={handleSubmit(onSubmit)} className="new_user">
            <div className={styles.field}>
              <label>メールアドレス</label><br />
              {errors.email && <span className={styles.error}>{errors.email.message}</span>}
              <input

                type="email"
                className={styles.input_field}
                {...register('email',{
                  required: '必須です',
                  pattern: { value: /^[^\s@]+@[^\s@]+$/, message: '無効なメールアドレスです',}
              })}
              />
            </div>
            <div className={styles.field}>
              <label>パスワード</label><em>(6文字以上)</em><br />
              {errors.password && <span className={styles.error}>{errors.password.message}</span>}
              <input
                type="password"
                className={styles.input_field}
                {...register('password', {
                  required: '必須です',
                  minLength: { value: 6, message: 'パスワードは6文字以上である必要があります' },
                  maxLength: { value: 128, message: 'パスワードは128文字以内である必要があります' }
                })}
              />
            </div>

            <div className={styles.field}>
              <label>パスワード再入力</label><br />
              {errors.passwordConfirmation && <span className={styles.error}>{errors.passwordConfirmation.message}</span>}
              <input
                type="password"
                className={styles.input_field}
                {...register('passwordConfirmation', {
                  required: '必須です',
                  validate: (value: string) => value === watch('password') || '確認用パスワードがパスワードと一致しません'
                })}
              />
            </div>

            <div className={styles.field}>
              <label>ユーザー名</label><br />
              {errors.username && <span className={styles.error}>{errors.username.message}</span>}
              <input
                type="text"
                className={styles.input_field}
                {...register('username', {
                  required: '必須です'
                })}
              />
            </div>

            <div className={styles.field}>
              <label>プロフィール</label><br />
              {errors.profile && <span className={styles.error}>{errors.profile.message}</span>}
              <input
                type="text"
                className={styles.input_field}
                {...register('profile', {
                  required: '必須です'
                })}
              />
            </div>

            <div className={styles.field}>
              <label>所属</label><br />
              {errors.affiliation && <span className={styles.error}>{errors.affiliation.message}</span>}
              <input
                type="text"
                className={styles.input_field}
                {...register('affiliation', {
                  required: '必須です'
                })}
              />
            </div>

            <div className={styles.field}>
              <label>役職</label><br />
              {errors.position && <span className={styles.error}>{errors.position.message}</span>}
              <input
                type="text"
                className={styles.input_field}
                {...register('position', {
                  required: '必須です'
                })}
              />
            </div>

            <div className="actions">
              <input type="submit" className={styles.form_btn} value="新規登録" />
            </div>
          </form>
        </div>
      </div>
    </>
  );
};

export default SignUpPage;