<script setup>
// 首页组件
</script>

<template>
  <div class="home">
    <div class="hero-section">
      <div class="card">
        <div class="card-header">
          <h1 class="card-title">DiffLight 交通信号灯优化平台</h1>
        </div>
        <div class="hero-content">
          <p class="hero-description">
            DiffLight 是一个基于深度学习的交通信号灯优化平台，
            专注于处理交通数据中的缺失模式，提供智能化的信号灯控制方案。
          </p>
          
          <div class="features">
            <div class="feature-item">
              <h3>🚦 智能优化</h3>
              <p>基于深度学习算法，对交通信号灯进行智能优化控制</p>
            </div>
            
            <div class="feature-item">
              <h3>📊 数据分析</h3>
              <p>支持多种交通数据格式，提供专业的数据分析工具</p>
            </div>
            
            <div class="feature-item">
              <h3>🔧 参数调节</h3>
              <p>灵活的参数配置系统，适应不同的交通场景需求</p>
            </div>
            
            <div class="feature-item">
              <h3>📈 结果可视化</h3>
              <p>丰富的图表展示，让优化结果一目了然</p>
            </div>
          </div>
          
          <div class="action-buttons" v-if="!isAuthenticated">
            <router-link to="/register" class="btn btn-primary btn-large">
              立即注册
            </router-link>
            <router-link to="/login" class="btn btn-secondary btn-large">
              用户登录
            </router-link>
          </div>
          
          <div class="welcome-message" v-else>
            <p>欢迎回来，{{ currentUser?.username }}！</p>
            <div class="quick-actions">
              <router-link to="/param-settings" class="btn btn-primary">
                参数设置
              </router-link>
              <router-link to="/dataset" class="btn btn-secondary">
                数据集管理
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="info-section">
      <div class="card">
        <div class="card-header">
          <h2 class="card-title">系统功能</h2>
        </div>
        <div class="info-content">
          <div class="info-grid">
            <div class="info-item">
              <h4>数据集管理</h4>
              <ul>
                <li>支持用户上传自定义数据集</li>
                <li>提供公共数据集下载</li>
                <li>完整的下载记录追踪</li>
              </ul>
            </div>
            
            <div class="info-item">
              <h4>参数配置</h4>
              <ul>
                <li>交通文件和路网文件选择</li>
                <li>交叉路口数量配置</li>
                <li>缺失模式和缺失率设置</li>
              </ul>
            </div>
            
            <div class="info-item">
              <h4>结果展示</h4>
              <ul>
                <li>填补结果图展示</li>
                <li>加噪和去噪过程可视化</li>
                <li>图片轮播和标题自定义</li>
              </ul>
            </div>
            
            <div class="info-item">
              <h4>管理功能</h4>
              <ul>
                <li>参数选项动态管理</li>
                <li>用户权限控制</li>
                <li>数据集审核和发布</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { computed } from 'vue'
import { useAuthStore } from '../stores/auth'

export default {
  setup() {
    const authStore = useAuthStore()
    
    const isAuthenticated = computed(() => authStore.isAuthenticated)
    const currentUser = computed(() => authStore.user)
    
    return {
      isAuthenticated,
      currentUser
    }
  }
}
</script>

<style scoped>
.home {
  max-width: 1000px;
  margin: 0 auto;
}

.hero-section {
  margin-bottom: 40px;
}

.hero-content {
  text-align: center;
}

.hero-description {
  font-size: 18px;
  line-height: 1.6;
  color: #666;
  margin-bottom: 40px;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.features {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
  margin-bottom: 40px;
}

.feature-item {
  text-align: center;
  padding: 20px;
}

.feature-item h3 {
  font-size: 20px;
  margin-bottom: 10px;
  color: #2c3e50;
}

.feature-item p {
  color: #666;
  line-height: 1.5;
}

.action-buttons {
  display: flex;
  gap: 20px;
  justify-content: center;
  margin-top: 30px;
}

.btn-large {
  padding: 12px 30px;
  font-size: 16px;
}

.welcome-message {
  text-align: center;
  margin-top: 30px;
}

.welcome-message p {
  font-size: 18px;
  color: #2c3e50;
  margin-bottom: 20px;
}

.quick-actions {
  display: flex;
  gap: 15px;
  justify-content: center;
}

.info-section {
  margin-top: 40px;
}

.info-content {
  padding: 0;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
}

.info-item h4 {
  color: #2c3e50;
  margin-bottom: 15px;
  font-size: 16px;
}

.info-item ul {
  list-style: none;
  padding: 0;
}

.info-item li {
  padding: 5px 0;
  color: #666;
  position: relative;
  padding-left: 20px;
}

.info-item li:before {
  content: "✓";
  color: #28a745;
  font-weight: bold;
  position: absolute;
  left: 0;
}

@media (max-width: 768px) {
  .action-buttons {
    flex-direction: column;
    align-items: center;
  }
  
  .quick-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .features {
    grid-template-columns: 1fr;
  }
  
  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style>