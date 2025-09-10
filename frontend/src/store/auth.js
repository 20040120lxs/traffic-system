import { defineStore } from 'pinia'

/**
 * 认证状态管理
 * 
 * 管理用户登录状态、令牌和用户信息
 */
export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || null,
    user: null,
    userType: localStorage.getItem('userType') || null
  }),
  
  getters: {
    isLoggedIn: (state) => !!state.token,
    isAdmin: (state) => state.userType === 'ADMIN'
  },
  
  actions: {
    /**
     * 登录成功后设置认证信息
     * @param {Object} authData 认证数据
     */
    setAuth(authData) {
      this.token = authData.token
      this.user = {
        id: authData.userId,
        username: authData.username,
        phoneNumber: authData.phoneNumber
      }
      this.userType = authData.userType
      
      // 持久化到本地存储
      localStorage.setItem('token', authData.token)
      localStorage.setItem('userType', authData.userType)
    },
    
    /**
     * 退出登录，清除认证信息
     */
    logout() {
      this.token = null
      this.user = null
      this.userType = null
      
      // 清除本地存储
      localStorage.removeItem('token')
      localStorage.removeItem('userType')
    },
    
    /**
     * 初始化时从本地存储恢复认证状态
     */
    restoreAuth() {
      const token = localStorage.getItem('token')
      const userType = localStorage.getItem('userType')
      
      if (token && userType) {
        this.token = token
        this.userType = userType
        // 注意：这里只恢复了token和userType，用户详细信息可能需要从服务器重新获取
      }
    }
  }
})