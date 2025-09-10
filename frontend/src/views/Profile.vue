<script setup>
import { ref, computed } from 'vue'
import { useAuthStore } from '../stores/auth'

const authStore = useAuthStore()
const currentUser = computed(() => authStore.user)

const userForm = ref({
  username: currentUser.value?.username || '',
  phone: currentUser.value?.phone || '',
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const message = ref('')
const messageType = ref('')

const updateProfile = () => {
  // 这里是桩实现
  showMessage('个人信息更新功能正在开发中', 'info')
}

const changePassword = () => {
  if (!userForm.value.currentPassword || !userForm.value.newPassword || !userForm.value.confirmPassword) {
    showMessage('请填写完整的密码信息', 'error')
    return
  }

  if (userForm.value.newPassword !== userForm.value.confirmPassword) {
    showMessage('新密码和确认密码不一致', 'error')
    return
  }

  // 这里是桩实现
  showMessage('密码修改功能正在开发中', 'info')
}

const showMessage = (msg, type) => {
  message.value = msg
  messageType.value = type
  setTimeout(() => {
    message.value = ''
  }, 3000)
}
</script>

<template>
  <div class="profile">
    <div class="card">
      <div class="card-header">
        <h2 class="card-title">个人信息管理</h2>
      </div>

      <!-- 消息提示 -->
      <div v-if="message" :class="['alert', `alert-${messageType}`]">
        {{ message }}
      </div>

      <!-- 基本信息 -->
      <div class="info-section">
        <h3>基本信息</h3>
        <div class="form-group">
          <label class="form-label">用户名</label>
          <input v-model="userForm.username" type="text" class="form-control" readonly />
        </div>
        <div class="form-group">
          <label class="form-label">角色</label>
          <input :value="currentUser?.role === 'ADMIN' ? '管理员' : '普通用户'" type="text" class="form-control" readonly />
        </div>
        <div class="form-group">
          <label class="form-label">手机号</label>
          <input v-model="userForm.phone" type="text" class="form-control" />
        </div>
        <div class="form-actions">
          <button @click="updateProfile" class="btn btn-primary">更新信息</button>
        </div>
      </div>

      <!-- 密码修改 -->
      <div class="password-section">
        <h3>修改密码</h3>
        <div class="form-group">
          <label class="form-label">当前密码</label>
          <input v-model="userForm.currentPassword" type="password" class="form-control" />
        </div>
        <div class="form-group">
          <label class="form-label">新密码</label>
          <input v-model="userForm.newPassword" type="password" class="form-control" />
        </div>
        <div class="form-group">
          <label class="form-label">确认新密码</label>
          <input v-model="userForm.confirmPassword" type="password" class="form-control" />
        </div>
        <div class="form-actions">
          <button @click="changePassword" class="btn btn-primary">修改密码</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.profile {
  max-width: 600px;
  margin: 0 auto;
}

.info-section, .password-section {
  padding: 20px 0;
}

.password-section {
  border-top: 1px solid #eee;
  margin-top: 20px;
}

.info-section h3, .password-section h3 {
  margin-bottom: 20px;
  color: #2c3e50;
}

.form-actions {
  margin-top: 20px;
}
</style>