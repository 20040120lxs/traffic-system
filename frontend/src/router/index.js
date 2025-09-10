import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

// 路由组件懒加载
const Home = () => import('../views/Home.vue')
const Login = () => import('../views/Login.vue')
const Register = () => import('../views/Register.vue')
const Profile = () => import('../views/Profile.vue')
const Dataset = () => import('../views/Dataset.vue')
const ParamSettings = () => import('../views/ParamSettings.vue')
const AdminDataset = () => import('../views/AdminDataset.vue')
const AdminUsers = () => import('../views/AdminUsers.vue')
const AdminParamOptions = () => import('../views/AdminParamOptions.vue')

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/profile',
    name: 'Profile',
    component: Profile,
    meta: { requiresAuth: true }
  },
  {
    path: '/dataset',
    name: 'Dataset',
    component: Dataset,
    meta: { requiresAuth: true }
  },
  {
    path: '/param-settings',
    name: 'ParamSettings',
    component: ParamSettings,
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/dataset',
    name: 'AdminDataset',
    component: AdminDataset,
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/users',
    name: 'AdminUsers',
    component: AdminUsers,
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  {
    path: '/admin/param-options',
    name: 'AdminParamOptions',
    component: AdminParamOptions,
    meta: { requiresAuth: true, requiresAdmin: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  // 检查是否需要登录
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next('/login')
    return
  }

  // 检查是否需要管理员权限
  if (to.meta.requiresAdmin && authStore.user?.role !== 'ADMIN') {
    next('/') // 重定向到首页
    return
  }

  next()
})

export default router