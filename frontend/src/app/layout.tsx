import type { Metadata } from "next";
import Header from "./components/Header";
import { AuthProvider } from "./context/AuthContext";

export const metadata: Metadata = {
  title: "PROTO SPACE",
  description: "Share the prototype",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ja">
      <body>
        <AuthProvider>
          <Header />
          
          {children}

          <footer style={{ textAlign: 'center', padding: '20px', backgroundColor: '#999', color: '#fff', fontSize: '12px', marginTop: '40px' }}>
            <p>Copyright © PROTO SPACE All rights reserved.</p>
          </footer>
        </AuthProvider>
      </body>
    </html>
  );
}