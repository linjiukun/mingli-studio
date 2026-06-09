import { defineStore } from 'pinia'
import { ref } from 'vue'
import { calculate as calculateBazi, getMyChart, getHistory } from '@/api/bazi'
import { requestReading, getReadings } from '@/api/fortune'
import { getToday, getRange } from '@/api/daily'
import { ask as askDivination } from '@/api/divination'

export const useFortuneStore = defineStore('fortune', () => {
  // 八字相关
  const baziChart = ref(null)
  const baziHistory = ref([])
  const baziLoading = ref(false)

  // 解读相关
  const readings = ref([])
  const readingLoading = ref(false)

  // 运势相关
  const todayFortune = ref(null)
  const rangeFortune = ref([])
  const dailyLoading = ref(false)

  // 占卜相关
  const divinationResult = ref(null)
  const divinationLoading = ref(false)

  // ======== 八字 ========
  async function calcBazi(data) {
    baziLoading.value = true
    try {
      const res = await calculateBazi(data)
      if (res.code === 200) {
        baziChart.value = res.data
        return { success: true, data: res.data }
      }
      return { success: false, msg: res.msg || '计算失败' }
    } catch (e) {
      return { success: false, msg: e.message || '网络错误' }
    } finally {
      baziLoading.value = false
    }
  }

  async function fetchMyChart() {
    baziLoading.value = true
    try {
      const res = await getMyChart()
      if (res.code === 200) {
        baziChart.value = res.data
        return { success: true, data: res.data }
      }
      return { success: false, msg: res.msg || '获取失败' }
    } catch (e) {
      return { success: false, msg: e.message || '网络错误' }
    } finally {
      baziLoading.value = false
    }
  }

  async function fetchBaziHistory() {
    baziLoading.value = true
    try {
      const res = await getHistory()
      if (res.code === 200) {
        baziHistory.value = res.data || []
        return { success: true, data: res.data }
      }
      return { success: false, msg: res.msg || '获取失败' }
    } catch (e) {
      return { success: false, msg: e.message || '网络错误' }
    } finally {
      baziLoading.value = false
    }
  }

  // ======== 解读 ========
  async function fetchReading(data) {
    readingLoading.value = true
    try {
      const res = await requestReading(data)
      if (res.code === 200) {
        readings.value.unshift(res.data)
        return { success: true, data: res.data }
      }
      return { success: false, msg: res.msg || '解读失败' }
    } catch (e) {
      return { success: false, msg: e.message || '网络错误' }
    } finally {
      readingLoading.value = false
    }
  }

  async function fetchReadings() {
    readingLoading.value = true
    try {
      const res = await getReadings()
      if (res.code === 200) {
        readings.value = res.data || []
        return { success: true, data: res.data }
      }
      return { success: false, msg: res.msg || '获取失败' }
    } catch (e) {
      return { success: false, msg: e.message || '网络错误' }
    } finally {
      readingLoading.value = false
    }
  }

  // ======== 运势 ========
  async function fetchTodayFortune() {
    dailyLoading.value = true
    try {
      const res = await getToday()
      if (res.code === 200) {
        todayFortune.value = res.data
        return { success: true, data: res.data }
      }
      return { success: false, msg: res.msg || '获取失败' }
    } catch (e) {
      return { success: false, msg: e.message || '网络错误' }
    } finally {
      dailyLoading.value = false
    }
  }

  async function fetchRangeFortune(params) {
    dailyLoading.value = true
    try {
      const res = await getRange(params)
      if (res.code === 200) {
        rangeFortune.value = res.data || []
        return { success: true, data: res.data }
      }
      return { success: false, msg: res.msg || '获取失败' }
    } catch (e) {
      return { success: false, msg: e.message || '网络错误' }
    } finally {
      dailyLoading.value = false
    }
  }

  // ======== 占卜 ========
  async function fetchDivination(data) {
    divinationLoading.value = true
    try {
      const res = await askDivination(data)
      if (res.code === 200) {
        divinationResult.value = res.data
        return { success: true, data: res.data }
      }
      return { success: false, msg: res.msg || '占卜失败' }
    } catch (e) {
      return { success: false, msg: e.message || '网络错误' }
    } finally {
      divinationLoading.value = false
    }
  }

  function clearDivination() {
    divinationResult.value = null
  }

  return {
    baziChart,
    baziHistory,
    baziLoading,
    readings,
    readingLoading,
    todayFortune,
    rangeFortune,
    dailyLoading,
    divinationResult,
    divinationLoading,
    calcBazi,
    fetchMyChart,
    fetchBaziHistory,
    fetchReading,
    fetchReadings,
    fetchTodayFortune,
    fetchRangeFortune,
    fetchDivination,
    clearDivination
  }
})
