import Image from "next/image";
import Link from "next/link";
import { PrototypeData } from "@/app/lib/prototypeData";
import { UserData } from "@/app/lib/userData";
import styles from "./PrototypeView.module.css";

interface PrototypeViewProps {
  prototypes: PrototypeData[];
  users: UserData[];
}

const Header = () => {
  return (
    <header className={styles.header}>
      <div className={styles.headerInner}>
        <div className={styles.logo}>
          <span className={styles.logoTop}>SHARE THE PROTOTYPE</span>
          <span className={styles.logoBottom}>
            PROTO<span className={styles.logoAccent}>SPACE</span>
          </span>
        </div>
        
        <div className={styles.nav}>
          <Link href="#" className={styles.logoutLink}>ログアウト</Link>
          <button className={styles.newProtoBtn}>
            New Proto
          </button>
        </div>
      </div>
    </header>
  );
};

const WelcomeMessage = ({ user }: { user: UserData }) => {
  return (
    <div className={styles.contentWrapper}>
      <div className={styles.greetingArea}>
        <p className={styles.greetingText}>
          こんにちは、
          <Link href="#" className={styles.userName}>
            {user.username}さん
          </Link>
        </p>
      </div>
    </div>
  );
};

const PrototypeCard = ({ prototype }: { prototype: PrototypeData }) => {
  return (
    <div className={styles.card}>
      <div className={styles.imageWrapper}>
        {/* Next.jsのImageコンポーネントを使用 */}
        <Image 
          src={prototype.image} 
          alt={prototype.title} 
          fill
          style={{ objectFit: "cover" }}
          sizes="(max-width: 768px) 100vw, 33vw"
        />
      </div>
      
      <div className={styles.cardContent}>
        <h2 className={styles.cardTitle}>{prototype.title}</h2>
        <p className={styles.cardCatchcopy}>{prototype.catchcopy}</p>
        
        <div className={styles.cardAuthor}>
          <Link href="#" className={styles.authorLink}>
            by 名前
          </Link>
        </div>
      </div>
    </div>
  );
};

const Footer = () => {
  return (
    <footer className={styles.footer}>
      <div className={styles.footerInner}>
        <p className={styles.footerText}>
          Copyright © PROTO SPACE All rights reserved.
        </p>
      </div>
    </footer>
  );
};

export const PrototypeView = ({ prototypes, users }: PrototypeViewProps) => {
  const currentUser = users[0]; 

  return (
    <div className={styles.pageContainer}>
      <Header />
      
      <main className={styles.mainContent}>
        {currentUser && <WelcomeMessage user={currentUser} />}

        <div className={styles.contentWrapper}>
          <div className={styles.gridContainer}>
            {prototypes.map((prototype) => (
              <PrototypeCard key={prototype.id} prototype={prototype} />
            ))}
          </div>
        </div>
      </main>
      
      <Footer />
    </div>
  );
};