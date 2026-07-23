import type { Metadata } from 'next';
import './styles/style.css'; // 独自のCSSを読み込み

export const metadata: Metadata = {
  title: 'ProtoSpace',
  description: 'ツイート投稿アプリ',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="ja">
      <body>{children}</body>
    </html>
  );
}
