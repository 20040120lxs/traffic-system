<template>
  <div id="app">
    <el-container>
      <!-- 头部导航 -->
      <el-header height="60px" style="background-color: #409eff; color: white; display: flex; align-items: center; justify-content: space-between;">
        <div style="display: flex; align-items: center;">
          <h2 style="margin: 0;">DiffLight 交通系统</h2>
        </div>
        <el-menu mode="horizontal" :default-active="currentRoute" style="background-color: transparent; border: none;">
          <el-menu-item index="/" @click="$router.push('/')">首页</el-menu-item>
          <el-menu-item index="/dataset" @click="$router.push('/dataset')" v-if="isLoggedIn">数据集管理</el-menu-item>
          <el-menu-item index="/login" @click="$router.push('/login')" v-if="!isLoggedIn">登录</el-menu-item>
          <el-menu-item index="/register" @click="$router.push('/register')" v-if="!isLoggedIn">注册</el-menu-item>
          <el-sub-menu index="user" v-if="isLoggedIn">
            <template #title>{{ username }}</template>
            <el-menu-item @click="logout">退出登录</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-header>
      
      <!-- 主要内容区域 -->
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from './store/auth'

export default {
  name: 'App',
  setup() {
    const router = useRouter()
    const route = useRoute()
    const authStore = useAuthStore()
    
    const currentRoute = computed(() => route.path)
    const isLoggedIn = computed(() => authStore.isLoggedIn)
    const username = computed(() => authStore.user?.username || '')
    
    const logout = () => {
      authStore.logout()
      router.push('/')
    }
    
    return {
      currentRoute,
      isLoggedIn,
      username,
      logout
    }
  }
}
</script>

<style>
.el-menu--horizontal .el-menu-item {
  color: white !important;
}

.el-menu--horizontal .el-menu-item:hover {
  background-color: rgba(255, 255, 255, 0.1) !important;
}

.el-menu--horizontal .el-sub-menu .el-sub-menu__title {
  color: white !important;
}
</style>