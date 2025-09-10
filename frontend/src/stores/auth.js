import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null,
    token: localStorage.getItem('token') || null
  }),

  getters: {
    isAuthenticated: (state) => !!state.token,
    isAdmin: (state) => state.user?.role === 'ADMIN'
  },

  actions: {
    /**
     * 设置认证信息
     */
    setAuth(user, token) {
      this.user = user
      this.token = token
      localStorage.setItem('token', token)
      localStorage.setItem('user', JSON.stringify(user))
    },

    /**
     * 清除认证信息
     */
    clearAuth() {
      this.user = null
      this.token = null
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    },

    /**
     * 初始化认证状态（从本地存储恢复）
     */
    initAuth() {
      const token = localStorage.getItem('token')
      const userStr = localStorage.getItem('user')
      
      if (token && userStr) {
        try {
          this.token = token
          this.user = JSON.parse(userStr)
        } catch (error) {
          console.error('恢复用户信息失败:', error)
          this.clearAuth()
        }
      }
    },

    /**
     * 登出
     */
    logout() {
      this.clearAuth()
      // 可以在这里添加调用后端登出接口的逻辑
    }
  }
})