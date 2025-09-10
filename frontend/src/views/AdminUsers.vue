<script setup>
import { ref } from 'vue'

const message = ref('')
const messageType = ref('')

const showMessage = (msg, type) => {
  message.value = msg
  messageType.value = type
  setTimeout(() => {
    message.value = ''
  }, 3000)
}

const manageUsers = () => {
  showMessage('用户信息管理功能正在开发中（桩实现）', 'info')
}
</script>

<template>
  <div class="admin-users">
    <div class="card">
      <div class="card-header">
        <h2 class="card-title">用户信息管理（管理员）</h2>
      </div>

      <!-- 消息提示 -->
      <div v-if="message" :class="['alert', `alert-${messageType}`]">
        {{ message }}
      </div>

      <div class="admin-content">
        <div class="empty-state">
          <h3>🔧 功能开发中</h3>
          <p>用户信息管理功能正在开发中...</p>
          <p>此功能将包括：</p>
          <ul>
            <li>查看所有用户信息</li>
            <li>修改用户账号和手机号</li>
            <li>重置用户密码</li>
            <li>用户权限管理</li>
          </ul>
          <button @click="manageUsers" class="btn btn-primary">
            模拟操作
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.admin-users {
  max-width: 800px;
  margin: 0 auto;
}

.admin-content {
  padding: 40px 20px;
}

.empty-state {
  text-align: center;
  color: #666;
}

.empty-state h3 {
  color: #2c3e50;
  margin-bottom: 20px;
}

.empty-state ul {
  text-align: left;
  max-width: 400px;
  margin: 20px auto;
}

.empty-state li {
  margin: 5px 0;
}
</style>