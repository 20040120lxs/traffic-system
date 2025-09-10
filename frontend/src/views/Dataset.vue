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

const uploadDataset = () => {
  showMessage('数据集上传功能正在开发中（桩实现）', 'info')
}

const downloadDataset = () => {
  showMessage('数据集下载功能正在开发中（桩实现）', 'info')
}
</script>

<template>
  <div class="dataset">
    <div class="card">
      <div class="card-header">
        <h2 class="card-title">数据集管理</h2>
      </div>

      <!-- 消息提示 -->
      <div v-if="message" :class="['alert', `alert-${messageType}`]">
        {{ message }}
      </div>

      <!-- 上传区域 -->
      <div class="upload-section">
        <h3>上传数据集</h3>
        <div class="upload-area">
          <p>点击选择文件或拖拽文件到此处</p>
          <button @click="uploadDataset" class="btn btn-primary">选择文件</button>
        </div>
      </div>

      <!-- 我的数据集 -->
      <div class="my-datasets-section">
        <h3>我的数据集</h3>
        <div class="empty-state">
          <p>暂无数据集（桩实现）</p>
        </div>
      </div>

      <!-- 公共数据集 -->
      <div class="public-datasets-section">
        <h3>公共数据集</h3>
        <div class="dataset-list">
          <div class="dataset-item">
            <div class="dataset-info">
              <h4>杭州交通数据集</h4>
              <p>包含杭州市区主要路段的交通流量数据</p>
            </div>
            <button @click="downloadDataset" class="btn btn-secondary">下载</button>
          </div>
          <div class="dataset-item">
            <div class="dataset-info">
              <h4>济南交通数据集</h4>
              <p>济南市交通信号灯控制数据</p>
            </div>
            <button @click="downloadDataset" class="btn btn-secondary">下载</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.dataset {
  max-width: 800px;
  margin: 0 auto;
}

.upload-section, .my-datasets-section, .public-datasets-section {
  padding: 20px 0;
  border-bottom: 1px solid #eee;
}

.upload-area {
  border: 2px dashed #ddd;
  padding: 40px;
  text-align: center;
  border-radius: 8px;
  margin: 20px 0;
}

.dataset-list {
  margin: 20px 0;
}

.dataset-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  border: 1px solid #eee;
  border-radius: 8px;
  margin-bottom: 10px;
}

.dataset-info h4 {
  margin: 0 0 5px 0;
  color: #2c3e50;
}

.dataset-info p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.empty-state {
  text-align: center;
  padding: 40px;
  color: #666;
}
</style>