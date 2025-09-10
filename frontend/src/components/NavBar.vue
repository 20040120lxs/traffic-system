<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const isAuthenticated = computed(() => authStore.isAuthenticated)
const isAdmin = computed(() => authStore.isAdmin)
const currentUser = computed(() => authStore.user)

const logout = () => {
  authStore.logout()
  router.push('/')
}
</script>

<template>
  <nav class="navbar">
    <div class="nav-container">
      <!-- 左侧品牌和导航 -->
      <div class="nav-left">
        <router-link to="/" class="nav-brand">DiffLight</router-link>
        
        <div class="nav-links">
          <router-link to="/" class="nav-link">首页</router-link>
          
          <template v-if="isAuthenticated">
            <router-link to="/dataset" class="nav-link">数据集</router-link>
            <router-link to="/param-settings" class="nav-link">参数设置</router-link>
            <router-link to="/profile" class="nav-link">个人信息</router-link>
            
            <!-- 管理员专用导航 -->
            <template v-if="isAdmin">
              <div class="nav-divider"></div>
              <router-link to="/admin/dataset" class="nav-link admin-link">数据集管理</router-link>
              <router-link to="/admin/users" class="nav-link admin-link">用户信息管理</router-link>
              <router-link to="/admin/param-options" class="nav-link admin-link">参数选项管理</router-link>
            </template>
          </template>
        </div>
      </div>

      <!-- 右侧用户信息 -->
      <div class="nav-right">
        <template v-if="isAuthenticated">
          <span class="user-info">
            {{ currentUser?.username }}
            <span v-if="isAdmin" class="admin-badge">管理员</span>
          </span>
          <button @click="logout" class="btn btn-secondary">退出</button>
        </template>
        <template v-else>
          <router-link to="/login" class="btn btn-primary">登录</router-link>
          <router-link to="/register" class="btn btn-secondary">注册</router-link>
        </template>
      </div>
    </div>
  </nav>
</template>

<style scoped>
.navbar {
  background-color: #2c3e50;
  color: white;
  padding: 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.nav-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
}

.nav-left {
  display: flex;
  align-items: center;
  gap: 30px;
}

.nav-brand {
  font-size: 24px;
  font-weight: bold;
  color: white;
  text-decoration: none;
  transition: color 0.3s;
}

.nav-brand:hover {
  color: #3498db;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 20px;
}

.nav-link {
  color: white;
  text-decoration: none;
  padding: 8px 12px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.nav-link:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.nav-link.router-link-active {
  background-color: #3498db;
}

.admin-link {
  background-color: rgba(231, 76, 60, 0.2);
}

.admin-link:hover {
  background-color: rgba(231, 76, 60, 0.3);
}

.nav-divider {
  width: 1px;
  height: 20px;
  background-color: rgba(255, 255, 255, 0.3);
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-info {
  color: white;
  font-size: 14px;
}

.admin-badge {
  background-color: #e74c3c;
  color: white;
  padding: 2px 6px;
  border-radius: 3px;
  font-size: 12px;
  margin-left: 8px;
}

.btn {
  margin-left: 5px;
}

@media (max-width: 768px) {
  .nav-container {
    flex-direction: column;
    height: auto;
    padding: 10px 20px;
  }

  .nav-left {
    flex-direction: column;
    gap: 10px;
    width: 100%;
  }

  .nav-links {
    flex-wrap: wrap;
    justify-content: center;
    gap: 10px;
  }

  .nav-right {
    margin-top: 10px;
  }
}
</style>