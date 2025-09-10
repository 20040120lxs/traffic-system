<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useAuthStore } from '../stores/auth'
import { paramAPI } from '../api/services'

const authStore = useAuthStore()

const isLoading = ref(false)
const message = ref('')
const messageType = ref('')

// 参数选项数据
const paramOptions = reactive({
  traffic_file: [],
  roadnet_file: [],
  num_intersections: [],
  missing_pattern: [],
  missing_rate: []
})

// 选择的参数
const selectedParams = reactive({
  trafficFile: '',
  roadnetFile: '',
  numIntersections: null,
  missingPattern: '',
  missingRate: ''
})

// 有效的缺失选项
const validMissingOptions = ref({})

// 结果数据
const resultData = ref(null)
const isAdmin = computed(() => authStore.isAdmin)

onMounted(() => {
  loadParamOptions()
})

// 加载参数选项
const loadParamOptions = async () => {
  try {
    const response = await paramAPI.getOptions()
    if (response.data.success) {
      Object.assign(paramOptions, response.data.data)
    }
  } catch (error) {
    console.error('加载参数选项失败:', error)
    showMessage('加载参数选项失败', 'error')
  }
}

// 当前三个基础参数改变时，更新有效的缺失选项
const updateValidMissingOptions = async () => {
  if (!selectedParams.trafficFile || !selectedParams.roadnetFile || !selectedParams.numIntersections) {
    validMissingOptions.value = {}
    return
  }

  try {
    const response = await paramAPI.getValidMissing({
      trafficFile: selectedParams.trafficFile,
      roadnetFile: selectedParams.roadnetFile,
      numIntersections: selectedParams.numIntersections
    })

    if (response.data.success) {
      validMissingOptions.value = response.data.data
      // 清空之前的选择
      selectedParams.missingPattern = ''
      selectedParams.missingRate = ''
    }
  } catch (error) {
    console.error('获取有效缺失选项失败:', error)
    showMessage('获取有效缺失选项失败', 'error')
  }
}

// 提交参数选择
const submitParams = async () => {
  if (!selectedParams.trafficFile || !selectedParams.roadnetFile || 
      !selectedParams.numIntersections || !selectedParams.missingPattern || 
      !selectedParams.missingRate) {
    showMessage('请选择完整的参数', 'error')
    return
  }

  isLoading.value = true
  try {
    const response = await paramAPI.selectParams(selectedParams)
    if (response.data.success) {
      resultData.value = response.data.data
      showMessage('参数提交成功', 'success')
    } else {
      showMessage(response.data.message || '参数提交失败', 'error')
    }
  } catch (error) {
    console.error('参数提交失败:', error)
    showMessage('参数提交失败', 'error')
  } finally {
    isLoading.value = false
  }
}

// 管理员上传图片
const uploadImages = ref({
  fillImage: null,
  noiseImage: null,
  denoiseImage: null,
  customTitle: ''
})

const handleFileUpload = (event, imageType) => {
  uploadImages.value[imageType] = event.target.files[0]
}

const uploadResults = async () => {
  if (!resultData.value) {
    showMessage('请先提交参数选择', 'error')
    return
  }

  const formData = new FormData()
  formData.append('parameterEntryId', resultData.value.parameterEntryId || 1)
  
  if (uploadImages.value.fillImage) {
    formData.append('fillImage', uploadImages.value.fillImage)
  }
  if (uploadImages.value.noiseImage) {
    formData.append('noiseImage', uploadImages.value.noiseImage)
  }
  if (uploadImages.value.denoiseImage) {
    formData.append('denoiseImage', uploadImages.value.denoiseImage)
  }
  if (uploadImages.value.customTitle) {
    formData.append('customTitle', uploadImages.value.customTitle)
  }

  try {
    const response = await paramAPI.uploadResults(formData)
    if (response.data.success) {
      showMessage('图片上传成功', 'success')
      // 重新加载结果
      submitParams()
    } else {
      showMessage(response.data.message || '图片上传失败', 'error')
    }
  } catch (error) {
    console.error('图片上传失败:', error)
    showMessage('图片上传失败', 'error')
  }
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
  <div class="param-settings">
    <div class="card">
      <div class="card-header">
        <h2 class="card-title">参数设置</h2>
      </div>

      <!-- 消息提示 -->
      <div v-if="message" :class="['alert', `alert-${messageType}`]">
        {{ message }}
      </div>

      <!-- 参数选择表单 -->
      <div class="param-form">
        <div class="form-row">
          <div class="form-group">
            <label class="form-label">交通文件</label>
            <select 
              v-model="selectedParams.trafficFile" 
              @change="updateValidMissingOptions"
              class="form-control"
            >
              <option value="">请选择交通文件</option>
              <option 
                v-for="option in paramOptions.traffic_file" 
                :key="option.value" 
                :value="option.value"
              >
                {{ option.displayName }}
              </option>
            </select>
          </div>

          <div class="form-group">
            <label class="form-label">路网文件</label>
            <select 
              v-model="selectedParams.roadnetFile" 
              @change="updateValidMissingOptions"
              class="form-control"
            >
              <option value="">请选择路网文件</option>
              <option 
                v-for="option in paramOptions.roadnet_file" 
                :key="option.value" 
                :value="option.value"
              >
                {{ option.displayName }}
              </option>
            </select>
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label">交叉路口数</label>
            <select 
              v-model="selectedParams.numIntersections" 
              @change="updateValidMissingOptions"
              class="form-control"
            >
              <option :value="null">请选择交叉路口数</option>
              <option 
                v-for="option in paramOptions.num_intersections" 
                :key="option.value" 
                :value="parseInt(option.value)"
              >
                {{ option.displayName }}
              </option>
            </select>
          </div>

          <div class="form-group">
            <label class="form-label">缺失模式</label>
            <select 
              v-model="selectedParams.missingPattern" 
              class="form-control"
              :disabled="Object.keys(validMissingOptions).length === 0"
            >
              <option value="">请选择缺失模式</option>
              <option 
                v-for="(rates, pattern) in validMissingOptions" 
                :key="pattern" 
                :value="pattern"
              >
                {{ pattern }}
              </option>
            </select>
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label">缺失率</label>
            <select 
              v-model="selectedParams.missingRate" 
              class="form-control"
              :disabled="!selectedParams.missingPattern"
            >
              <option value="">请选择缺失率</option>
              <option 
                v-for="rate in validMissingOptions[selectedParams.missingPattern] || []" 
                :key="rate" 
                :value="rate"
              >
                {{ rate }}
              </option>
            </select>
          </div>
        </div>

        <div class="form-actions">
          <button 
            @click="submitParams" 
            class="btn btn-primary"
            :disabled="isLoading"
          >
            {{ isLoading ? '提交中...' : '确定' }}
          </button>
        </div>
      </div>
    </div>

    <!-- 结果展示 -->
    <div v-if="resultData" class="results-section">
      <!-- 参数表格 -->
      <div class="card">
        <div class="card-header">
          <h3 class="card-title">选中参数</h3>
        </div>
        <table class="table">
          <tbody>
            <tr>
              <td><strong>交通文件</strong></td>
              <td>{{ resultData.selectedParams.trafficFile }}</td>
            </tr>
            <tr>
              <td><strong>路网文件</strong></td>
              <td>{{ resultData.selectedParams.roadnetFile }}</td>
            </tr>
            <tr>
              <td><strong>交叉路口数</strong></td>
              <td>{{ resultData.selectedParams.numIntersections }}</td>
            </tr>
            <tr>
              <td><strong>缺失模式</strong></td>
              <td>{{ resultData.selectedParams.missingPattern }}</td>
            </tr>
            <tr>
              <td><strong>缺失率</strong></td>
              <td>{{ resultData.selectedParams.missingRate }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 图片轮播 -->
      <div class="card">
        <div class="card-header">
          <h3 class="card-title">结果展示</h3>
        </div>
        <div v-if="resultData.images && resultData.images.length > 0" class="image-carousel">
          <div 
            v-for="(image, index) in resultData.images" 
            :key="index"
            class="image-item"
          >
            <img :src="image.url" :alt="image.title" />
            <div class="image-title">{{ image.title }}</div>
          </div>
        </div>
        <div v-else class="no-images">
          <p>暂无相关图片，管理员可上传图片供展示。</p>
        </div>
      </div>
    </div>

    <!-- 管理员上传区域 -->
    <div v-if="isAdmin && resultData" class="admin-upload-section">
      <div class="card">
        <div class="card-header">
          <h3 class="card-title">管理员上传区域</h3>
        </div>
        
        <div class="upload-form">
          <div class="form-group">
            <label class="form-label">自定义标题</label>
            <input 
              v-model="uploadImages.customTitle"
              type="text" 
              class="form-control"
              placeholder="输入自定义标题（可选）"
            />
          </div>

          <div class="upload-grid">
            <div class="upload-item">
              <label class="form-label">填补结果图</label>
              <input 
                type="file" 
                @change="handleFileUpload($event, 'fillImage')"
                class="form-control"
                accept="image/*"
              />
            </div>

            <div class="upload-item">
              <label class="form-label">加噪过程图</label>
              <input 
                type="file" 
                @change="handleFileUpload($event, 'noiseImage')"
                class="form-control"
                accept="image/*"
              />
            </div>

            <div class="upload-item">
              <label class="form-label">去噪过程图</label>
              <input 
                type="file" 
                @change="handleFileUpload($event, 'denoiseImage')"
                class="form-control"
                accept="image/*"
              />
            </div>
          </div>

          <div class="form-actions">
            <button @click="uploadResults" class="btn btn-success">
              上传图片
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.param-settings {
  max-width: 800px;
  margin: 0 auto;
}

.param-form {
  padding: 20px 0;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 20px;
}

.form-actions {
  text-align: center;
  margin-top: 30px;
}

.results-section {
  margin-top: 30px;
}

.image-carousel {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  padding: 20px 0;
}

.image-item {
  text-align: center;
}

.image-item img {
  max-width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.image-title {
  margin-top: 10px;
  font-weight: bold;
  color: #333;
}

.no-images {
  text-align: center;
  padding: 40px;
  color: #666;
}

.admin-upload-section {
  margin-top: 30px;
  border: 2px dashed #e74c3c;
  border-radius: 8px;
}

.admin-upload-section .card {
  border: none;
  box-shadow: none;
}

.upload-form {
  padding: 20px 0;
}

.upload-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.upload-item label {
  color: #e74c3c;
  font-weight: bold;
}

@media (max-width: 768px) {
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .upload-grid {
    grid-template-columns: 1fr;
  }
}
</style>