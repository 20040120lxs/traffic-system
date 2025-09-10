<script setup>
import { ref, reactive, onMounted } from 'vue'
import { adminParamAPI } from '../api/services'

const isLoading = ref(false)
const message = ref('')
const messageType = ref('')

// 参数选项数据
const paramOptions = ref([])
const filteredOptions = ref([])

// 过滤选项
const selectedType = ref('')
const paramTypes = [
  { value: '', label: '全部类型' },
  { value: 'traffic_file', label: '交通文件' },
  { value: 'roadnet_file', label: '路网文件' },
  { value: 'num_intersections', label: '交叉路口数' },
  { value: 'missing_pattern', label: '缺失模式' },
  { value: 'missing_rate', label: '缺失率' }
]

// 编辑状态
const editingId = ref(null)
const editForm = reactive({
  type: '',
  value: '',
  displayName: '',
  sortOrder: 0
})

// 新增表单
const newForm = reactive({
  type: '',
  value: '',
  displayName: '',
  sortOrder: 0
})
const showAddForm = ref(false)

onMounted(() => {
  loadParamOptions()
})

// 加载参数选项
const loadParamOptions = async () => {
  isLoading.value = true
  try {
    const response = await adminParamAPI.getAll()
    if (response.data.success) {
      paramOptions.value = response.data.data
      filterOptions()
    } else {
      showMessage('加载参数选项失败', 'error')
    }
  } catch (error) {
    console.error('加载参数选项失败:', error)
    showMessage('加载参数选项失败', 'error')
  } finally {
    isLoading.value = false
  }
}

// 过滤选项
const filterOptions = () => {
  if (selectedType.value) {
    filteredOptions.value = paramOptions.value.filter(option => option.type === selectedType.value)
  } else {
    filteredOptions.value = [...paramOptions.value]
  }
}

// 开始编辑
const startEdit = (option) => {
  editingId.value = option.id
  editForm.type = option.type
  editForm.value = option.value
  editForm.displayName = option.displayName || ''
  editForm.sortOrder = option.sortOrder || 0
}

// 取消编辑
const cancelEdit = () => {
  editingId.value = null
}

// 保存编辑
const saveEdit = async (id) => {
  if (!editForm.type || !editForm.value) {
    showMessage('类型和值不能为空', 'error')
    return
  }

  try {
    const response = await adminParamAPI.update(id, editForm)
    if (response.data.success) {
      showMessage('更新成功', 'success')
      editingId.value = null
      loadParamOptions()
    } else {
      showMessage(response.data.message || '更新失败', 'error')
    }
  } catch (error) {
    console.error('更新失败:', error)
    showMessage('更新失败', 'error')
  }
}

// 删除选项
const deleteOption = async (id) => {
  if (!confirm('确定要删除这个参数选项吗？')) {
    return
  }

  try {
    const response = await adminParamAPI.delete(id)
    if (response.data.success) {
      showMessage('删除成功', 'success')
      loadParamOptions()
    } else {
      showMessage(response.data.message || '删除失败', 'error')
    }
  } catch (error) {
    console.error('删除失败:', error)
    showMessage('删除失败', 'error')
  }
}

// 显示新增表单
const showAdd = () => {
  showAddForm.value = true
  newForm.type = ''
  newForm.value = ''
  newForm.displayName = ''
  newForm.sortOrder = 0
}

// 取消新增
const cancelAdd = () => {
  showAddForm.value = false
}

// 保存新增
const saveAdd = async () => {
  if (!newForm.type || !newForm.value) {
    showMessage('类型和值不能为空', 'error')
    return
  }

  try {
    const response = await adminParamAPI.create(newForm)
    if (response.data.success) {
      showMessage('创建成功', 'success')
      showAddForm.value = false
      loadParamOptions()
    } else {
      showMessage(response.data.message || '创建失败', 'error')
    }
  } catch (error) {
    console.error('创建失败:', error)
    showMessage('创建失败', 'error')
  }
}

const showMessage = (msg, type) => {
  message.value = msg
  messageType.value = type
  setTimeout(() => {
    message.value = ''
  }, 3000)
}

// 获取类型的中文名称
const getTypeLabel = (type) => {
  const typeObj = paramTypes.find(t => t.value === type)
  return typeObj ? typeObj.label : type
}
</script>

<template>
  <div class="admin-param-options">
    <div class="card">
      <div class="card-header">
        <h2 class="card-title">参数选项管理</h2>
      </div>

      <!-- 消息提示 -->
      <div v-if="message" :class="['alert', `alert-${messageType}`]">
        {{ message }}
      </div>

      <!-- 操作栏 -->
      <div class="toolbar">
        <div class="filter-section">
          <label class="form-label">类型过滤：</label>
          <select v-model="selectedType" @change="filterOptions" class="form-control filter-select">
            <option v-for="type in paramTypes" :key="type.value" :value="type.value">
              {{ type.label }}
            </option>
          </select>
        </div>

        <div class="action-section">
          <button @click="showAdd" class="btn btn-primary" :disabled="isLoading">
            新增选项
          </button>
          <button @click="loadParamOptions" class="btn btn-secondary" :disabled="isLoading">
            {{ isLoading ? '加载中...' : '刷新' }}
          </button>
        </div>
      </div>

      <!-- 新增表单 -->
      <div v-if="showAddForm" class="add-form">
        <div class="card">
          <div class="card-header">
            <h3 class="card-title">新增参数选项</h3>
          </div>
          <div class="form-grid">
            <div class="form-group">
              <label class="form-label">类型 *</label>
              <select v-model="newForm.type" class="form-control">
                <option value="">请选择类型</option>
                <option v-for="type in paramTypes.slice(1)" :key="type.value" :value="type.value">
                  {{ type.label }}
                </option>
              </select>
            </div>

            <div class="form-group">
              <label class="form-label">值 *</label>
              <input v-model="newForm.value" type="text" class="form-control" placeholder="输入参数值" />
            </div>

            <div class="form-group">
              <label class="form-label">显示名称</label>
              <input v-model="newForm.displayName" type="text" class="form-control" placeholder="输入显示名称" />
            </div>

            <div class="form-group">
              <label class="form-label">排序顺序</label>
              <input v-model.number="newForm.sortOrder" type="number" class="form-control" min="0" />
            </div>
          </div>

          <div class="form-actions">
            <button @click="saveAdd" class="btn btn-success">保存</button>
            <button @click="cancelAdd" class="btn btn-secondary">取消</button>
          </div>
        </div>
      </div>

      <!-- 参数选项列表 -->
      <div class="options-table">
        <table class="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>类型</th>
              <th>值</th>
              <th>显示名称</th>
              <th>排序顺序</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="option in filteredOptions" :key="option.id">
              <!-- 普通显示模式 -->
              <template v-if="editingId !== option.id">
                <td>{{ option.id }}</td>
                <td>
                  <span class="type-badge" :class="`type-${option.type}`">
                    {{ getTypeLabel(option.type) }}
                  </span>
                </td>
                <td>{{ option.value }}</td>
                <td>{{ option.displayName || '-' }}</td>
                <td>{{ option.sortOrder || 0 }}</td>
                <td>
                  <button @click="startEdit(option)" class="btn btn-sm btn-primary">
                    编辑
                  </button>
                  <button @click="deleteOption(option.id)" class="btn btn-sm btn-danger">
                    删除
                  </button>
                </td>
              </template>

              <!-- 编辑模式 -->
              <template v-else>
                <td>{{ option.id }}</td>
                <td>
                  <select v-model="editForm.type" class="form-control form-control-sm">
                    <option v-for="type in paramTypes.slice(1)" :key="type.value" :value="type.value">
                      {{ type.label }}
                    </option>
                  </select>
                </td>
                <td>
                  <input v-model="editForm.value" type="text" class="form-control form-control-sm" />
                </td>
                <td>
                  <input v-model="editForm.displayName" type="text" class="form-control form-control-sm" />
                </td>
                <td>
                  <input v-model.number="editForm.sortOrder" type="number" class="form-control form-control-sm" min="0" />
                </td>
                <td>
                  <button @click="saveEdit(option.id)" class="btn btn-sm btn-success">
                    保存
                  </button>
                  <button @click="cancelEdit" class="btn btn-sm btn-secondary">
                    取消
                  </button>
                </td>
              </template>
            </tr>
          </tbody>
        </table>

        <div v-if="filteredOptions.length === 0" class="empty-state">
          <p>{{ isLoading ? '加载中...' : '暂无数据' }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.admin-param-options {
  max-width: 1000px;
  margin: 0 auto;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}

.filter-section {
  display: flex;
  align-items: center;
  gap: 10px;
}

.filter-select {
  width: 150px;
}

.action-section {
  display: flex;
  gap: 10px;
}

.add-form {
  margin-bottom: 30px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
  margin-bottom: 20px;
}

.form-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.options-table {
  overflow-x: auto;
}

.table th {
  white-space: nowrap;
}

.type-badge {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: bold;
  color: white;
}

.type-traffic_file {
  background-color: #007bff;
}

.type-roadnet_file {
  background-color: #28a745;
}

.type-num_intersections {
  background-color: #ffc107;
  color: #333;
}

.type-missing_pattern {
  background-color: #dc3545;
}

.type-missing_rate {
  background-color: #6f42c1;
}

.btn-sm {
  padding: 4px 8px;
  font-size: 12px;
  margin-right: 5px;
}

.form-control-sm {
  padding: 4px 8px;
  font-size: 12px;
}

.empty-state {
  text-align: center;
  padding: 40px;
  color: #666;
}

@media (max-width: 768px) {
  .toolbar {
    flex-direction: column;
    gap: 15px;
    align-items: stretch;
  }

  .filter-section {
    justify-content: center;
  }

  .action-section {
    justify-content: center;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }
}
</style>