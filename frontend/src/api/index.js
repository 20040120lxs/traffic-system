import axios from 'axios'
import { useAuthStore } from '../store/auth'

// 创建 axios 实例
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器 - 自动添加认证头
api.interceptors.request.use(
  (config) => {
    const authStore = useAuthStore()
    if (authStore.token) {
      config.headers.Authorization = `Bearer ${authStore.token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器 - 处理认证失败
api.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    if (error.response?.status === 401) {
      const authStore = useAuthStore()
      authStore.logout()
      // 可以在这里添加跳转到登录页面的逻辑
    }
    return Promise.reject(error)
  }
)

/**
 * 认证相关 API
 */
export const authAPI = {
  /**
   * 用户登录
   */
  login: (username, password) => {
    return api.post('/auth/login', { username, password })
  },
  
  /**
   * 管理员登录
   */
  adminLogin: (username, password) => {
    return api.post('/auth/admin/login', { username, password })
  },
  
  /**
   * 用户注册
   */
  register: (username, password, confirmPassword, phoneNumber) => {
    return api.post('/auth/register', {
      username,
      password,
      confirmPassword,
      phoneNumber
    })
  }
}

/**
 * 数据集相关 API
 */
export const datasetAPI = {
  /**
   * 上传数据集
   * @param {File} file 要上传的文件
   * @returns {Promise} 上传结果
   */
  uploadDataset: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    
    return api.post('/datasets/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },
  
  /**
   * 获取我的数据集列表
   */
  getMyDatasets: () => {
    return api.get('/datasets/my')
  },
  
  /**
   * 获取公开数据集列表
   */
  getPublicDatasets: () => {
    return api.get('/datasets/public')
  }
}

/**
 * 管理员相关 API
 */
export const adminAPI = {
  /**
   * 获取所有数据集（管理员视图）
   */
  getAllDatasets: () => {
    return api.get('/admin/datasets')
  },
  
  /**
   * 设置数据集公开状态
   */
  setDatasetPublic: (datasetId, isPublic) => {
    return api.put(`/admin/datasets/${datasetId}/public`, { isPublic })
  },
  
  /**
   * 管理员权限测试
   */
  testAdminAccess: () => {
    return api.get('/admin/test')
  }
}

export default api