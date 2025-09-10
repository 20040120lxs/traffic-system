<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>用户登录</h2>
      
      <el-tabs v-model="loginType" class="login-tabs">
        <el-tab-pane label="普通用户" name="user"></el-tab-pane>
        <el-tab-pane label="管理员" name="admin"></el-tab-pane>
      </el-tabs>
      
      <el-form
        ref="loginForm"
        :model="form"
        :rules="rules"
        label-width="80px"
        @submit.prevent="handleLogin"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="form.username"
            placeholder="请输入用户名"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            style="width: 100%"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="login-footer">
        <span>还没有账号？</span>
        <el-link type="primary" @click="$router.push('/register')">立即注册</el-link>
      </div>
      
      <div class="admin-hint" v-if="loginType === 'admin'">
        <el-alert
          title="管理员默认账号"
          description="用户名：admin，密码：Admin@123"
          type="info"
          show-icon
          :closable="false"
        />
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '../store/auth'
import { authAPI } from '../api'

export default {
  name: 'Login',
  setup() {
    const router = useRouter()
    const authStore = useAuthStore()
    
    const loginForm = ref(null)
    const loading = ref(false)
    const loginType = ref('user')
    
    const form = reactive({
      username: '',
      password: ''
    })
    
    const rules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' }
      ]
    }
    
    const handleLogin = async () => {
      if (!loginForm.value) return
      
      const valid = await loginForm.value.validate()
      if (!valid) return
      
      loading.value = true
      
      try {
        let response
        if (loginType.value === 'admin') {
          response = await authAPI.adminLogin(form.username, form.password)
        } else {
          response = await authAPI.login(form.username, form.password)
        }
        
        if (response.data.success) {
          authStore.setAuth(response.data)
          ElMessage.success('登录成功')
          router.push('/')
        } else {
          ElMessage.error(response.data.message || '登录失败')
        }
      } catch (error) {
        console.error('登录失败:', error)
        ElMessage.error(error.response?.data?.message || '登录失败，请检查网络连接')
      } finally {
        loading.value = false
      }
    }
    
    return {
      loginForm,
      loading,
      loginType,
      form,
      rules,
      handleLogin
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
  padding: 20px;
}

.login-card {
  width: 100%;
  max-width: 400px;
}

.login-tabs {
  margin-bottom: 20px;
}

.login-footer {
  text-align: center;
  margin-top: 20px;
}

.admin-hint {
  margin-top: 20px;
}
</style>