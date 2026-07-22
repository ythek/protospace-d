'use client'
import Link from 'next/link'

const Header = () => {
  return (
    <header className="header">
      <div className="header__bar row">
        <h1 className="grid-6"><Link href="/">ProtoSpace</Link></h1>
        <div className="grid-6">
          <Link href="/" className="post">新規登録</Link>
          <Link href="/" className="post">ログイン</Link>
        </div>
      </div>
   </header>
  )
}

export default Header