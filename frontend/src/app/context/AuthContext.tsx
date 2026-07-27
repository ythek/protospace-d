'use client'

import React, { createContext, useContext, useState, useEffect } from 'react'
import { login as apiLogin, logout as apiLogout } from '@/app/api/users';

type User = {
  id: number
  username: string
  isAuthenticated: boolean
} | null

type AuthContextType = {
  user: User
  setUser: React.Dispatch<React.SetStateAction<User>>
  login: (email: string, password: string) => Promise<void>
  logout: () => Promise<void>
  isLoading: boolean
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<User>(null)
  const [isLoading, setIsLoading] = useState(true)
  

  useEffect(() => {
    const storedUser = localStorage.getItem('user')
    if (storedUser) {
      try {
        const parsedUser = JSON.parse(storedUser);
        if (typeof parsedUser === 'object' && parsedUser !== null 
            && 'id' in parsedUser && 'username' in parsedUser && 'isAuthenticated' in parsedUser) {
          setUser(parsedUser as User);
        }
      } catch (error) {
        console.error('Error parsing stored user data:', error);
      }
    }
    setIsLoading(false)
  }, [])

  const login = async (email: string, password: string) => {
    try {
      const userData = await apiLogin({ email, password });
      const user = { id: userData.id, username: userData.username, isAuthenticated: true };
      setUser(user);
      localStorage.setItem('user', JSON.stringify(user));
    } catch (error) {
      console.error('Login error:', error);
      throw error;
    }
  }

  const logout = async () => {
    try {
      await apiLogout();
      setUser(null);
      localStorage.removeItem('user');
    } catch (error) {
      console.error('Logout error:', error);
      throw error;
    }
  };

  return (
    <AuthContext.Provider value={{ user, setUser, login, logout, isLoading }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuthContext() {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}