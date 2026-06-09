import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录', noAuth: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { title: '注册', noAuth: true }
  },
  {
    path: '/',
    component: () => import('@/layout/Layout.vue'),
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '首页', icon: 'HomeFilled' }
      },
      {
        path: 'bazi',
        name: 'BaziChart',
        component: () => import('@/views/BaziChart.vue'),
        meta: { title: '八字算命', icon: 'MagicStick' }
      },
      {
        path: 'fortune',
        name: 'FortuneReading',
        component: () => import('@/views/FortuneReading.vue'),
        meta: { title: '命理解读', icon: 'Reading' }
      },
      {
        path: 'daily',
        name: 'DailyFortune',
        component: () => import('@/views/DailyFortune.vue'),
        meta: { title: '每日运势', icon: 'Sunny' }
      },
      {
        path: 'divination',
        name: 'Divination',
        component: () => import('@/views/Divination.vue'),
        meta: { title: '占卜问卦', icon: 'Coin' }
      },
      {
        path: 'zodiac',
        name: 'Zodiac',
        component: () => import('@/views/Zodiac.vue'),
        meta: { title: '十二星座', icon: 'Star' }
      },
      {
        path: 'profile',
        name: 'UserProfile',
        component: () => import('@/views/UserProfile.vue'),
        meta: { title: '个人中心', icon: 'User' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.meta.noAuth && token) {
    next('/dashboard')
  } else {
    document.title = to.meta.title ? `命理阁 - ${to.meta.title}` : '命理阁'
    next()
  }
})

export default router
