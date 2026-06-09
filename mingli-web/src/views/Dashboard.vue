<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="24">
        <el-card class="welcome-card" shadow="never">
          <div class="welcome-content">
            <div class="avatar-circle">{{ user?.nickname?.charAt(0) || '命' }}</div>
            <div class="welcome-text">
              <h2>欢迎回来，{{ user?.nickname || '缘主' }}</h2>
              <p v-if="dailyFortune">今日综合运势：<span class="score" :class="scoreLevel(dailyFortune.overallScore)">{{ dailyFortune.overallScore }}分</span></p>
              <p v-else>点击下方功能开始你的命理之旅</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="quick-actions">
      <el-col :xs="12" :sm="8" :md="6" v-for="item in actions" :key="item.path">
        <el-card class="action-card" shadow="never" @click="goPage(item.path)">
          <div class="action-icon" :style="{ color: item.color }">{{ item.icon }}</div>
          <div class="action-title">{{ item.title }}</div>
          <div class="action-desc">{{ item.desc }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card v-if="dailyFortune" class="daily-summary" shadow="never">
      <template #header><span>📅 今日速览</span></template>
      <el-row :gutter="16">
        <el-col :span="6" v-for="item in scores" :key="item.label">
          <div class="score-item">
            <div class="score-label">{{ item.label }}</div>
            <el-progress :percentage="item.value" :color="item.color" :show-text="false" />
            <div class="score-value">{{ item.value }}分</div>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getToday } from '@/api/daily'

const router = useRouter()
const userStore = useUserStore()
const user = ref(null)
const dailyFortune = ref(null)

const actions = [
  { path: '/bazi', icon: '☯', title: '八字算命', desc: '生辰八字命盘解读', color: '#c9a84c' },
  { path: '/fortune', icon: '📜', title: '命理解读', desc: '财运·婚姻·事业', color: '#e8a87c' },
  { path: '/daily', icon: '☀', title: '每日运势', desc: '今日运势早知道', color: '#f4d03f' },
  { path: '/divination', icon: '🔮', title: '占卜问卦', desc: '六爻·梅花易数', color: '#9b59b6' },
  { path: '/profile', icon: '👤', title: '个人中心', desc: '管理个人信息', color: '#5dade2' }
]

const scores = ref([])

const scoreLevel = (s) => {
  if (s >= 80) return 'score-high'
  if (s >= 60) return 'score-mid'
  return 'score-low'
}

const goPage = (path) => router.push(path)

onMounted(async () => {
  user.value = userStore.userInfo
  try {
    const res = await getToday()
    if (res.code === 200) {
      dailyFortune.value = res.data
      scores.value = [
        { label: '财运', value: res.data.wealthScore || 0, color: '#f4d03f' },
        { label: '爱情', value: res.data.loveScore || 0, color: '#e74c3c' },
        { label: '事业', value: res.data.careerScore || 0, color: '#5dade2' },
        { label: '健康', value: res.data.healthScore || 0, color: '#2ecc71' }
      ]
    }
  } catch (e) { /* ignore */ }
})
</script>

<style scoped>
.welcome-card {
  background: linear-gradient(135deg, rgba(201,168,76,0.08), rgba(26,26,46,0.5));
  border: 1px solid rgba(201,168,76,0.15);
  margin-bottom: 20px;
}
.welcome-content { display: flex; align-items: center; gap: 20px; }
.avatar-circle {
  width: 56px; height: 56px; border-radius: 50%;
  background: linear-gradient(135deg, #c9a84c, #8b6914);
  display: flex; align-items: center; justify-content: center;
  font-size: 24px; color: #fff; flex-shrink: 0;
}
.welcome-text h2 { margin: 0 0 4px; color: #e8d5c4; font-size: 20px; }
.welcome-text p { margin: 0; color: #8a7a6a; font-size: 14px; }
.score { font-weight: bold; }
.score-high { color: #2ecc71; }
.score-mid { color: #f4d03f; }
.score-low { color: #e74c3c; }

.quick-actions { margin-bottom: 20px; }
.action-card {
  background: rgba(255,255,255,0.04);
  border: 1px solid rgba(255,255,255,0.06);
  cursor: pointer;
  transition: all 0.3s;
  text-align: center;
  margin-bottom: 16px;
}
.action-card:hover {
  background: rgba(201,168,76,0.08);
  border-color: rgba(201,168,76,0.3);
  transform: translateY(-2px);
}
.action-icon { font-size: 36px; margin-bottom: 8px; }
.action-title { color: #e8d5c4; font-size: 15px; font-weight: bold; }
.action-desc { color: #8a7a6a; font-size: 12px; margin-top: 4px; }

.daily-summary {
  background: rgba(255,255,255,0.04);
  border: 1px solid rgba(255,255,255,0.06);
}
:deep(.el-card__header) {
  border-bottom: 1px solid rgba(255,255,255,0.06);
  color: #c9a84c;
}
.score-item { text-align: center; padding: 8px 0; }
.score-label { color: #c4b5a0; margin-bottom: 6px; font-size: 13px; }
.score-value { color: #e8d5c4; margin-top: 4px; font-size: 14px; font-weight: bold; }
</style>
