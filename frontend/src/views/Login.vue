<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { authAPI } from '../api/services'

const router = useRouter()
const authStore = useAuthStore()

const isLoading = ref(false)
const message = ref('')
const messageType = ref('')

const form = reactive({
  username: '',
  password: ''
})

const adminForm = reactive({
  username: '',
  password: ''
})

const showAdminLogin = ref(false)

// 用户登录
const login = async () => {
  if (!form.username || !form.password) {
    showMessage('请填写用户名和密码', 'error')
    return
  }

  isLoading.value = true
  try {
    const response = await authAPI.login(form)
    
    if (response.data.success) {
      const { token, username, role, phone } = response.data.data
      authStore.setAuth({ username, role, phone }, token)
      
      showMessage('登录成功', 'success')
      setTimeout(() => {
        router.push('/')
      }, 1000)
    } else {
      showMessage(response.data.message || '登录失败', 'error')
    }
  } catch (error) {
    console.error('登录错误:', error)
    showMessage(error.response?.data?.message || '登录失败，请检查网络连接', 'error')
  } finally {
    isLoading.value = false
  }
}

// 管理员登录
const adminLogin = async () => {
  if (!adminForm.username || !adminForm.password) {
    showMessage('请填写管理员用户名和密码', 'error')
    return
  }

  isLoading.value = true
  try {
    const response = await authAPI.adminLogin(adminForm)
    
    if (response.data.success) {
      const { token, username, role, phone } = response.data.data
      authStore.setAuth({ username, role, phone }, token)
      
      showMessage('管理员登录成功', 'success')
      setTimeout(() => {
        router.push('/')
      }, 1000)
    } else {
      showMessage(response.data.message || '管理员登录失败', 'error')
    }
  } catch (error) {
    console.error('管理员登录错误:', error)
    showMessage(error.response?.data?.message || '管理员登录失败，请检查网络连接', 'error')
  } finally {
    isLoading.value = false
  }
}

const showMessage = (msg, type) => {
  message.value = msg
  messageType.value = type
  setTimeout(() => {
    message.value = ''
  }, 3000)
}

const toggleLoginType = () => {
  showAdminLogin.value = !showAdminLogin.value
  message.value = ''
  // 清空表单
  if (showAdminLogin.value) {
    adminForm.username = ''
    adminForm.password = ''
  } else {
    form.username = ''
    form.password = ''
  }
}
</script>

<template>
  <div class="login-page">
    <div class="login-container">
      <div class="card">
        <div class="card-header">
          <h2 class="card-title">
            {{ showAdminLogin ? '管理员登录' : '用户登录' }}
          </h2>
        </div>

        <!-- 消息提示 -->
        <div v-if="message" :class="['alert', `alert-${messageType}`]">
          {{ message }}
        </div>

        <!-- 用户登录表单 -->
        <form v-if="!showAdminLogin" @submit.prevent="login">
          <div class="form-group">
            <label class="form-label">用户名</label>
            <input 
              v-model="form.username"
              type="text" 
              class="form-control"
              placeholder="请输入用户名"
              :disabled="isLoading"
            />
          </div>

          <div class="form-group">
            <label class="form-label">密码</label>
            <input 
              v-model="form.password"
              type="password" 
              class="form-control"
              placeholder="请输入密码"
              :disabled="isLoading"
            />
          </div>

          <div class="form-group">
            <button 
              type="submit" 
              class="btn btn-primary btn-block"
              :disabled="isLoading"
            >
              {{ isLoading ? '登录中...' : '登录' }}
            </button>
          </div>
        </form>

        <!-- 管理员登录表单 -->
        <form v-else @submit.prevent="adminLogin">
          <div class="form-group">
            <label class="form-label">管理员用户名</label>
            <input 
              v-model="adminForm.username"
              type="text" 
              class="form-control"
              placeholder="请输入管理员用户名"
              :disabled="isLoading"
            />
          </div>

          <div class="form-group">
            <label class="form-label">管理员密码</label>
            <input 
              v-model="adminForm.password"
              type="password" 
              class="form-control"
              placeholder="请输入管理员密码"
              :disabled="isLoading"
            />
          </div>

          <div class="form-group">
            <button 
              type="submit" 
              class="btn btn-primary btn-block admin-btn"
              :disabled="isLoading"
            >
              {{ isLoading ? '登录中...' : '管理员登录' }}
            </button>
          </div>
        </form>

        <!-- 切换登录类型 -->
        <div class="login-switch">
          <button 
            @click="toggleLoginType" 
            class="btn btn-secondary btn-block"
            :disabled="isLoading"
          >
            {{ showAdminLogin ? '切换到用户登录' : '切换到管理员登录' }}
          </button>
        </div>

        <!-- 注册链接 -->
        <div class="register-link" v-if="!showAdminLogin">
          <p>还没有账号？<router-link to="/register">立即注册</router-link></p>
        </div>

        <!-- 默认账号提示 -->
        <div class="default-account" v-if="showAdminLogin">
          <div class="alert alert-info">
            <strong>默认管理员账号：</strong><br>
            用户名：admin<br>
            密码：Admin@123
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  min-height: 80vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.login-container {
  width: 100%;
  max-width: 400px;
}

.btn-block {
  width: 100%;
}

.admin-btn {
  background-color: #e74c3c;
}

.admin-btn:hover {
  background-color: #c0392b;
}

.login-switch {
  margin-top: 15px;
}

.register-link {
  text-align: center;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.register-link a {
  color: #007bff;
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}

.default-account {
  margin-top: 20px;
}

.default-account .alert {
  font-size: 12px;
  margin-bottom: 0;
}
</style>