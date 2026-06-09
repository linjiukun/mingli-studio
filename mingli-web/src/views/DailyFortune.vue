<template>
  <div class="daily-fortune">
    <el-row :gutter="16" class="controls">
      <el-col :span="8">
        <el-date-picker v-model="selectedDate" type="date" placeholder="选择日期" style="width:100%"
          @change="loadDate" value-format="YYYY-MM-DD" />
      </el-col>
    </el-row>

    <div v-if="fortune" class="fortune-card">
      <div class="date-title">{{ selectedDate || '今日' }} 运势</div>

      <div class="overall-section">
        <el-progress type="circle" :percentage="fortune.overallScore" :stroke-width="10" :size="140"
          :color="overallColor" class="overall-progress">
          <span class="overall-value">{{ fortune.overallScore }}</span>
        </el-progress>
        <div class="overall-label">综合评分</div>
      </div>

      <el-divider content-position="center">分项运势</el-divider>

      <div class="sub-scores">
        <div v-for="item in subScores" :key="item.key" class="sub-item">
          <div class="sub-label">{{ item.label }}</div>
          <el-progress :percentage="item.value" :color="item.color" :stroke-width="14" />
          <div class="sub-value">{{ item.value }}</div>
        </div>
      </div>

      <el-divider content-position="center">幸运信息</el-divider>

      <el-row :gutter="12" class="lucky-info">
        <el-col :span="6" v-for="item in luckyItems" :key="item.label">
          <div class="lucky-card">
            <div class="lucky-label">{{ item.label }}</div>
            <div class="lucky-value">{{ item.value }}</div>
          </div>
        </el-col>
      </el-row>

      <el-divider content-position="center">每日建议</el-divider>
      <div class="advice-text">{{ fortune.advice }}</div>
    </div>

    <div v-else class="empty">暂无运势数据，请先选择日期</div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getToday } from '@/api/daily'

const selectedDate = ref('')
const fortune = ref(null)

const subScores = computed(() => {
  if (!fortune.value) return []
  return [
    { key: 'wealth', label: '财运', value: fortune.value.wealthScore || 0, color: '#f4d03f' },
    { key: 'love', label: '爱情', value: fortune.value.loveScore || 0, color: '#e74c3c' },
    { key: 'career', label: '事业', value: fortune.value.careerScore || 0, color: '#5dade2' },
    { key: 'health', label: '健康', value: fortune.value.healthScore || 0, color: '#2ecc71' }
  ]
})

const luckyItems = computed(() => {
  if (!fortune.value) return []
  return [
    { label: '幸运色', value: fortune.value.luckyColor || '—' },
    { label: '幸运数字', value: fortune.value.luckyNumber || '—' },
    { label: '吉方', value: fortune.value.luckyDirection || '—' },
    { label: '忌方', value: fortune.value.avoidDirection || '—' }
  ]
})

const overallColor = computed(() => {
  const s = fortune.value?.overallScore || 0
  if (s >= 80) return '#2ecc71'
  if (s >= 60) return '#f4d03f'
  if (s >= 40) return '#e67e22'
  return '#e74c3c'
})

const loadDate = async () => {
  // 当前只支持今日，扩展需要 range API
  if (!selectedDate.value || selectedDate.value === getDateStr()) {
    await loadToday()
  } else {
    fortune.value = null
  }
}

const getDateStr = () => {
  const d = new Date()
  return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')}`
}

const loadToday = async () => {
  try {
    const res = await getToday()
    if (res.code === 200) fortune.value = res.data
  } catch { /* ignore */ }
}

onMounted(() => {
  selectedDate.value = getDateStr()
  loadToday()
})
</script>

<style scoped>
.controls { margin-bottom: 20px; }
.fortune-card {
  background: rgba(255,255,255,0.04);
  border: 1px solid rgba(255,255,255,0.08);
  border-radius: 12px;
  padding: 24px;
}
.date-title {
  text-align: center;
  font-size: 20px;
  color: #c9a84c;
  margin-bottom: 24px;
  font-family: 'STKaiti', serif;
}
.overall-section { text-align: center; margin-bottom: 8px; }
.overall-value { font-size: 32px; font-weight: bold; color: #e8d5c4; }
.overall-label { color: #8a7a6a; margin-top: 8px; font-size: 14px; }

.sub-scores { padding: 8px 0; }
.sub-item { display: flex; align-items: center; gap: 12px; margin-bottom: 14px; }
.sub-label { width: 50px; color: #c4b5a0; font-weight: bold; }
.sub-value { width: 36px; color: #8a7a6a; text-align: right; }
:deep(.el-progress) { flex: 1; }

.lucky-info { margin: 8px 0; }
.lucky-card {
  background: rgba(201,168,76,0.08);
  border: 1px solid rgba(201,168,76,0.15);
  border-radius: 8px;
  padding: 14px 8px;
  text-align: center;
}
.lucky-label { color: #8a7a6a; font-size: 12px; margin-bottom: 6px; }
.lucky-value { color: #c9a84c; font-size: 16px; font-weight: bold; }

.advice-text {
  color: #c4b5a0; font-size: 15px; line-height: 1.8;
  text-align: center; font-style: italic;
  padding: 8px 0;
}

:deep(.el-divider__text) { color: #c9a84c; }
.empty { text-align: center; padding: 60px 0; color: #6a5a4a; }
</style>
