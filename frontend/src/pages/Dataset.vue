<template>
  <div class="dataset-container">
    <el-card class="upload-card">
      <h2>数据集上传</h2>
      
      <el-row :gutter="20">
        <!-- 上传区域 -->
        <el-col :span="12">
          <div class="upload-section">
            <h3>上传新数据集</h3>
            
            <el-upload
              ref="uploadRef"
              class="upload-demo"
              drag
              :auto-upload="false"
              :on-change="handleFileSelect"
              :file-list="fileList"
              :limit="1"
              :on-exceed="handleExceed"
            >
              <el-icon class="el-icon--upload"><upload-filled /></el-icon>
              <div class="el-upload__text">
                将文件拖到此处，或<em>点击上传</em>
              </div>
              <template #tip>
                <div class="el-upload__tip">
                  支持各种格式的数据集文件，文件大小不超过 50MB
                </div>
              </template>
            </el-upload>
            
            <div class="upload-actions" v-if="selectedFile">
              <p>已选择文件：{{ selectedFile.name }}</p>
              <p>文件大小：{{ formatFileSize(selectedFile.size) }}</p>
              <el-button
                type="primary"
                :loading="uploading"
                @click="handleUpload"
                style="margin-right: 10px;"
              >
                确认上传
              </el-button>
              <el-button @click="clearSelection">取消</el-button>
            </div>
          </div>
        </el-col>
        
        <!-- 说明区域 -->
        <el-col :span="12">
          <div class="info-section">
            <h3>上传说明</h3>
            <el-alert
              title="数据集上传功能已可用"
              description="您可以上传各种格式的交通数据集文件，系统会自动保存并在我的数据集中显示。"
              type="success"
              show-icon
              :closable="false"
            />
            
            <div class="upload-tips">
              <h4>使用指南：</h4>
              <ul>
                <li>支持拖拽上传和点击选择文件</li>
                <li>文件大小限制：50MB</li>
                <li>上传的数据集默认为私有，可联系管理员设置为公开</li>
                <li>文件会安全存储在服务器指定目录中</li>
              </ul>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>
    
    <!-- 我的数据集列表 -->
    <el-card class="dataset-list-card" style="margin-top: 20px;">
      <h2>我的数据集</h2>
      
      <el-button @click="loadMyDatasets" style="margin-bottom: 20px;">
        <el-icon><refresh /></el-icon>
        刷新列表
      </el-button>
      
      <el-table :data="myDatasets" style="width: 100%" v-loading="loadingDatasets">
        <el-table-column prop="fileName" label="文件名" />
        <el-table-column prop="fileSize" label="文件大小" :formatter="formatFileSizeInTable" />
        <el-table-column prop="isPublic" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.isPublic ? 'success' : 'info'">
              {{ scope.row.isPublic ? '公开' : '私有' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="上传时间" :formatter="formatDateTime" />
      </el-table>
      
      <div v-if="myDatasets.length === 0 && !loadingDatasets" class="empty-state">
        <p>暂无数据集，请上传您的第一个数据集</p>
      </div>
    </el-card>
    
    <!-- 公开数据集列表 -->
    <el-card class="dataset-list-card" style="margin-top: 20px;">
      <h2>公开数据集</h2>
      
      <el-button @click="loadPublicDatasets" style="margin-bottom: 20px;">
        <el-icon><refresh /></el-icon>
        刷新列表
      </el-button>
      
      <el-table :data="publicDatasets" style="width: 100%" v-loading="loadingPublicDatasets">
        <el-table-column prop="fileName" label="文件名" />
        <el-table-column prop="fileSize" label="文件大小" :formatter="formatFileSizeInTable" />
        <el-table-column prop="ownerType" label="上传者类型" />
        <el-table-column prop="createdAt" label="上传时间" :formatter="formatDateTime" />
      </el-table>
      
      <div v-if="publicDatasets.length === 0 && !loadingPublicDatasets" class="empty-state">
        <p>暂无公开数据集</p>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, genFileId } from 'element-plus'
import { UploadFilled, Refresh } from '@element-plus/icons-vue'
import { datasetAPI } from '../api'

export default {
  name: 'Dataset',
  components: {
    UploadFilled,
    Refresh
  },
  setup() {
    const uploadRef = ref(null)
    const uploading = ref(false)
    const loadingDatasets = ref(false)
    const loadingPublicDatasets = ref(false)
    const selectedFile = ref(null)
    const fileList = ref([])
    const myDatasets = ref([])
    const publicDatasets = ref([])
    
    /**
     * 处理文件选择
     */
    const handleFileSelect = (file, fileList) => {
      selectedFile.value = file.raw
    }
    
    /**
     * 处理文件数量超限
     */
    const handleExceed = (files, fileList) => {
      uploadRef.value.clearFiles()
      const file = files[0]
      file.uid = genFileId()
      uploadRef.value.handleStart(file)
      selectedFile.value = file
    }
    
    /**
     * 清除文件选择
     */
    const clearSelection = () => {
      selectedFile.value = null
      fileList.value = []
      uploadRef.value.clearFiles()
    }
    
    /**
     * 执行文件上传
     */
    const handleUpload = async () => {
      if (!selectedFile.value) {
        ElMessage.warning('请先选择要上传的文件')
        return
      }
      
      uploading.value = true
      
      try {
        const response = await datasetAPI.uploadDataset(selectedFile.value)
        
        if (response.data.success) {
          ElMessage.success('数据集上传成功！')
          clearSelection()
          // 刷新我的数据集列表
          await loadMyDatasets()
        } else {
          ElMessage.error(response.data.message || '上传失败')
        }
      } catch (error) {
        console.error('上传失败:', error)
        ElMessage.error(error.response?.data?.message || '上传失败，请检查网络连接')
      } finally {
        uploading.value = false
      }
    }
    
    /**
     * 加载我的数据集列表
     */
    const loadMyDatasets = async () => {
      loadingDatasets.value = true
      
      try {
        const response = await datasetAPI.getMyDatasets()
        if (response.data.success) {
          myDatasets.value = response.data.datasets
        }
      } catch (error) {
        console.error('加载数据集列表失败:', error)
        ElMessage.error('加载数据集列表失败')
      } finally {
        loadingDatasets.value = false
      }
    }
    
    /**
     * 加载公开数据集列表
     */
    const loadPublicDatasets = async () => {
      loadingPublicDatasets.value = true
      
      try {
        const response = await datasetAPI.getPublicDatasets()
        if (response.data.success) {
          publicDatasets.value = response.data.datasets
        }
      } catch (error) {
        console.error('加载公开数据集列表失败:', error)
        ElMessage.error('加载公开数据集列表失败')
      } finally {
        loadingPublicDatasets.value = false
      }
    }
    
    /**
     * 格式化文件大小
     */
    const formatFileSize = (bytes) => {
      if (bytes === 0) return '0 B'
      const k = 1024
      const sizes = ['B', 'KB', 'MB', 'GB']
      const i = Math.floor(Math.log(bytes) / Math.log(k))
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
    }
    
    /**
     * 表格中格式化文件大小
     */
    const formatFileSizeInTable = (row, column, cellValue) => {
      return formatFileSize(cellValue)
    }
    
    /**
     * 格式化日期时间
     */
    const formatDateTime = (row, column, cellValue) => {
      if (!cellValue) return '-'
      return new Date(cellValue).toLocaleString('zh-CN')
    }
    
    // 组件挂载时加载数据
    onMounted(() => {
      loadMyDatasets()
      loadPublicDatasets()
    })
    
    return {
      uploadRef,
      uploading,
      loadingDatasets,
      loadingPublicDatasets,
      selectedFile,
      fileList,
      myDatasets,
      publicDatasets,
      handleFileSelect,
      handleExceed,
      clearSelection,
      handleUpload,
      loadMyDatasets,
      loadPublicDatasets,
      formatFileSize,
      formatFileSizeInTable,
      formatDateTime
    }
  }
}
</script>

<style scoped>
.dataset-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.upload-section, .info-section {
  padding: 20px;
}

.upload-actions {
  margin-top: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.upload-tips {
  margin-top: 20px;
}

.upload-tips ul {
  padding-left: 20px;
}

.upload-tips li {
  margin-bottom: 8px;
}

.empty-state {
  text-align: center;
  padding: 40px;
  color: #909399;
}

.el-upload__tip {
  margin-top: 10px;
}
</style>