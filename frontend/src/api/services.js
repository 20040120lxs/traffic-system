import api from './index'

/**
 * 认证相关 API
 */
export const authAPI = {
  // 用户注册
  register(data) {
    return api.post('/auth/register', data)
  },

  // 用户登录
  login(data) {
    return api.post('/auth/login', data)
  },

  // 管理员登录
  adminLogin(data) {
    return api.post('/auth/admin/login', data)
  }
}

/**
 * 参数相关 API
 */
export const paramAPI = {
  // 获取参数选项
  getOptions() {
    return api.get('/params/options')
  },

  // 获取有效的缺失选项
  getValidMissing(params) {
    return api.get('/params/valid-missing', { params })
  },

  // 选择参数
  selectParams(data) {
    return api.post('/params/select', data)
  },

  // 管理员上传结果图片
  uploadResults(formData) {
    return api.post('/params/admin/results/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  }
}

/**
 * 管理员参数选项 API
 */
export const adminParamAPI = {
  // 获取所有参数选项
  getAll() {
    return api.get('/admin/param-options')
  },

  // 根据类型获取参数选项
  getByType(type) {
    return api.get(`/admin/param-options/type/${type}`)
  },

  // 创建参数选项
  create(data) {
    return api.post('/admin/param-options', data)
  },

  // 更新参数选项
  update(id, data) {
    return api.put(`/admin/param-options/${id}`, data)
  },

  // 删除参数选项
  delete(id) {
    return api.delete(`/admin/param-options/${id}`)
  }
}

/**
 * 数据集相关 API（桩实现）
 */
export const datasetAPI = {
  // 获取我的数据集
  getMy() {
    return Promise.resolve({ data: { success: true, data: [] } })
  },

  // 获取公共数据集
  getPublic() {
    return Promise.resolve({ data: { success: true, data: [] } })
  },

  // 上传数据集
  upload(formData) {
    return Promise.resolve({ data: { success: true, message: '上传成功（桩实现）' } })
  }
}