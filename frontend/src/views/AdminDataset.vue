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

const manageDatasets = () => {
  showMessage('管理员数据集管理功能正在开发中（桩实现）', 'info')
}
</script>

<template>
  <div class="admin-dataset">
    <div class="card">
      <div class="card-header">
        <h2 class="card-title">数据集管理（管理员）</h2>
      </div>

      <!-- 消息提示 -->
      <div v-if="message" :class="['alert', `alert-${messageType}`]">
        {{ message }}
      </div>

      <div class="admin-content">
        <div class="empty-state">
          <h3>🔧 功能开发中</h3>
          <p>管理员数据集管理功能正在开发中...</p>
          <p>此功能将包括：</p>
          <ul>
            <li>查看所有用户上传的数据集</li>
            <li>审核和管理数据集</li>
            <li>上传公共数据集</li>
            <li>数据集下载统计</li>
          </ul>
          <button @click="manageDatasets" class="btn btn-primary">
            模拟操作
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.admin-dataset {
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