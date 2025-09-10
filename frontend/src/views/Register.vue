<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { authAPI } from '../api/services'

const router = useRouter()

const isLoading = ref(false)
const message = ref('')
const messageType = ref('')

const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  phone: ''
})

const register = async () => {
  // 表单验证
  if (!form.username || !form.password || !form.confirmPassword) {
    showMessage('请填写完整的注册信息', 'error')
    return
  }

  if (form.password !== form.confirmPassword) {
    showMessage('密码和确认密码不一致', 'error')
    return
  }

  if (form.password.length < 6) {
    showMessage('密码长度不能少于6位', 'error')
    return
  }

  isLoading.value = true
  try {
    const response = await authAPI.register(form)
    
    if (response.data.success) {
      showMessage('注册成功，即将跳转到登录页面', 'success')
      setTimeout(() => {
        router.push('/login')
      }, 2000)
    } else {
      showMessage(response.data.message || '注册失败', 'error')
    }
  } catch (error) {
    console.error('注册错误:', error)
    showMessage(error.response?.data?.message || '注册失败，请检查网络连接', 'error')
  } finally {
    isLoading.value = false
  }
}

const showMessage = (msg, type) => {
  message.value = msg
  messageType.value = type
  setTimeout(() => {
    message.value = ''
  }, 5000)
}
</script>

<template>
  <div class="register-page">
    <div class="register-container">
      <div class="card">
        <div class="card-header">
          <h2 class="card-title">用户注册</h2>
        </div>

        <!-- 消息提示 -->
        <div v-if="message" :class="['alert', `alert-${messageType}`]">
          {{ message }}
        </div>

        <form @submit.prevent="register">
          <div class="form-group">
            <label class="form-label">用户名 *</label>
            <input 
              v-model="form.username"
              type="text" 
              class="form-control"
              placeholder="请输入用户名（3-50字符）"
              :disabled="isLoading"
              maxlength="50"
            />
          </div>

          <div class="form-group">
            <label class="form-label">密码 *</label>
            <input 
              v-model="form.password"
              type="password" 
              class="form-control"
              placeholder="请输入密码（至少6字符）"
              :disabled="isLoading"
            />
          </div>

          <div class="form-group">
            <label class="form-label">确认密码 *</label>
            <input 
              v-model="form.confirmPassword"
              type="password" 
              class="form-control"
              placeholder="请再次输入密码"
              :disabled="isLoading"
            />
          </div>

          <div class="form-group">
            <label class="form-label">手机号</label>
            <input 
              v-model="form.phone"
              type="tel" 
              class="form-control"
              placeholder="请输入手机号（可选）"
              :disabled="isLoading"
              maxlength="20"
            />
          </div>

          <div class="form-group">
            <button 
              type="submit" 
              class="btn btn-primary btn-block"
              :disabled="isLoading"
            >
              {{ isLoading ? '注册中...' : '注册' }}
            </button>
          </div>
        </form>

        <!-- 登录链接 -->
        <div class="login-link">
          <p>已有账号？<router-link to="/login">立即登录</router-link></p>
        </div>

        <!-- 注册说明 -->
        <div class="register-info">
          <div class="alert alert-info">
            <strong>注册说明：</strong><br>
            • 用户名长度为3-50字符<br>
            • 密码长度至少6字符<br>
            • 手机号为可选项<br>
            • 注册后可立即使用系统功能
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.register-page {
  min-height: 80vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.register-container {
  width: 100%;
  max-width: 450px;
}

.btn-block {
  width: 100%;
}

.login-link {
  text-align: center;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.login-link a {
  color: #007bff;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}

.register-info {
  margin-top: 20px;
}

.register-info .alert {
  font-size: 12px;
  margin-bottom: 0;
}

.form-label {
  color: #333;
}

.form-group input:focus {
  border-color: #007bff;
  box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
}
</style>