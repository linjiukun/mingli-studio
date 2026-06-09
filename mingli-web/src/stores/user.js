import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as loginApi, register as registerApi } from '@/api/auth'
import { getUserProfile, updateUserProfile } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(null)
  const loading = ref(false)

  const isLoggedIn = computed(() => !!token.value)
  const username = computed(() => userInfo.value?.username || '')

  function setToken(newToken) {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  function removeToken() {
    token.value = ''
    localStorage.removeItem('token')
  }

  async function login(credentials) {
    loading.value = true
    try {
      const res = await loginApi(credentials)
      if (res.code === 200) {
        setToken(res.data.token)
        userInfo.value = res.data.user || null
        return { success: true, msg: res.msg || '登录成功' }
      }
      return { success: false, msg: res.msg || '登录失败' }
    } catch (e) {
      return { success: false, msg: e.message || '网络错误' }
    } finally {
      loading.value = false
    }
  }

  async function register(data) {
    loading.value = true
    try {
      const res = await registerApi(data)
      if (res.code === 200) {
        return { success: true, msg: res.msg || '注册成功' }
      }
      return { success: false, msg: res.msg || '注册失败' }
    } catch (e) {
      return { success: false, msg: e.message || '网络错误' }
    } finally {
      loading.value = false
    }
  }

  async function loadUser() {
    if (!token.value) return
    try {
      const res = await getUserProfile()
      if (res.code === 200) {
        userInfo.value = res.data
      }
    } catch (e) {
      if (e.response?.status === 401) {
        logout()
      }
    }
  }

  async function updateProfile(data) {
    loading.value = true
    try {
      const res = await updateUserProfile(data)
      if (res.code === 200) {
        userInfo.value = { ...userInfo.value, ...res.data }
        return { success: true, msg: '更新成功' }
      }
      return { success: false, msg: res.msg || '更新失败' }
    } catch (e) {
      return { success: false, msg: e.message || '网络错误' }
    } finally {
      loading.value = false
    }
  }

  function logout() {
    removeToken()
    userInfo.value = null
  }

  return {
    token,
    userInfo,
    loading,
    isLoggedIn,
    username,
    login,
    register,
    loadUser,
    updateProfile,
    logout
  }
})
