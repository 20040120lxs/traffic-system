<template>
  <div class="home-container">
    <el-card class="welcome-card">
      <h1>欢迎使用 DiffLight 交通系统</h1>
      <p class="description">
        DiffLight 是一个专业的交通数据处理和分析系统，提供数据集管理、参数配置、
        结果分析等功能，帮助研究人员更好地处理和分析交通相关数据。
      </p>
      
      <div class="features">
        <h2>主要功能</h2>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card class="feature-card">
              <h3>数据集管理</h3>
              <p>上传、下载和管理交通数据集，支持公开和私有数据集</p>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="feature-card">
              <h3>参数配置</h3>
              <p>灵活的参数设置，支持多种交通分析场景</p>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="feature-card">
              <h3>结果分析</h3>
              <p>直观的图表展示和分析结果，便于研究和决策</p>
            </el-card>
          </el-col>
        </el-row>
      </div>
      
      <div class="quick-actions" v-if="!isLoggedIn">
        <h2>快速开始</h2>
        <el-space>
          <el-button type="primary" @click="$router.push('/register')">注册账号</el-button>
          <el-button @click="$router.push('/login')">立即登录</el-button>
        </el-space>
      </div>
      
      <div class="user-actions" v-else>
        <h2>欢迎回来，{{ username }}！</h2>
        <el-space>
          <el-button type="primary" @click="$router.push('/dataset')">管理数据集</el-button>
        </el-space>
      </div>
    </el-card>
  </div>
</template>

<script>
import { computed } from 'vue'
import { useAuthStore } from '../store/auth'

export default {
  name: 'Home',
  setup() {
    const authStore = useAuthStore()
    
    const isLoggedIn = computed(() => authStore.isLoggedIn)
    const username = computed(() => authStore.user?.username || '')
    
    return {
      isLoggedIn,
      username
    }
  }
}
</script>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.welcome-card {
  margin-bottom: 20px;
}

.description {
  font-size: 16px;
  line-height: 1.6;
  color: #666;
  margin: 20px 0;
}

.features {
  margin: 40px 0;
}

.feature-card {
  text-align: center;
  height: 150px;
}

.feature-card h3 {
  color: #409eff;
  margin-bottom: 10px;
}

.quick-actions, .user-actions {
  text-align: center;
  margin-top: 40px;
}
</style>