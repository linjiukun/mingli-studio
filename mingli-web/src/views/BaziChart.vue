<template>
  <div class="bazi-page">
    <el-card class="section" shadow="never">
      <template #header>
        <span>☯ 八字命盘</span>
        <el-button v-if="!chart" type="primary" size="small" style="float:right" @click="showCalc = true">开始测算</el-button>
        <el-button v-else type="warning" size="small" plain style="float:right" @click="showCalc = true">重新测算</el-button>
      </template>

      <div v-if="!chart && !showCalc" class="empty-state">
        <div class="empty-icon">☯</div>
        <p>尚未测算八字，点击上方按钮开始</p>
      </div>

      <div v-if="chart" class="chart-display">
        <el-row :gutter="16" class="pillars">
          <el-col :span="6" v-for="(p, i) in pillars" :key="i">
            <div class="pillar-card">
              <div class="pillar-title">{{ p.name }}</div>
              <div class="pillar-stem">{{ p.stem }}</div>
              <div class="pillar-branch">{{ p.branch }}</div>
            </div>
          </el-col>
        </el-row>

        <div class="info-grid">
          <div class="info-item">
            <span class="label">天干</span>
            <span class="value">{{ chart.heavenlyStems }}</span>
          </div>
          <div class="info-item">
            <span class="label">地支</span>
            <span class="value">{{ chart.earthlyBranches }}</span>
          </div>
        </div>

        <el-divider content-position="center">五行分析</el-divider>
        <div class="five-elements">
          <div v-for="(v, k) in parsedElements" :key="k" class="element-bar">
            <div class="elem-label">{{ elementsCN[k] }}</div>
            <el-progress :percentage="v" :color="elementColors[k]" />
            <div class="elem-count">{{ v }}%</div>
          </div>
        </div>

        <el-divider content-position="center">喜用神 · 忌神</el-divider>
        <div class="gods-section">
          <el-tag type="success" size="large">喜用神：{{ chart.luckGods }}</el-tag>
          <el-tag type="danger" size="large">忌　神：{{ chart.unluckGods }}</el-tag>
        </div>

        <el-divider content-position="center">命理总评</el-divider>
        <div class="summary-text">{{ chart.summary }}</div>
      </div>
    </el-card>

    <!-- 测算弹窗 -->
    <el-dialog v-model="showCalc" title="八字测算" width="420px" :close-on-click-modal="false">
      <el-form :model="calcForm" label-position="top">
        <el-form-item label="出生日期">
          <el-date-picker v-model="calcForm.birthDate" type="date" placeholder="选择出生日期" style="width:100%" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="性别">
              <el-select v-model="calcForm.gender" style="width:100%">
                <el-option label="男" value="0" />
                <el-option label="女" value="1" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生时辰">
              <el-select v-model="calcForm.birthHour" placeholder="选择" style="width:100%">
                <el-option v-for="h in hours" :key="h.value" :label="h.label" :value="h.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="showCalc = false">取消</el-button>
        <el-button type="primary" @click="handleCalc" :loading="calcLoading">开始测算</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { calculate, getMyChart } from '@/api/bazi'

const chart = ref(null)
const showCalc = ref(false)
const calcLoading = ref(false)

const calcForm = reactive({ birthDate: '', gender: '0', birthHour: '' })

const hours = [
  { value: '子', label: '子时' }, { value: '丑', label: '丑时' },
  { value: '寅', label: '寅时' }, { value: '卯', label: '卯时' },
  { value: '辰', label: '辰时' }, { value: '巳', label: '巳时' },
  { value: '午', label: '午时' }, { value: '未', label: '未时' },
  { value: '申', label: '申时' }, { value: '酉', label: '酉时' },
  { value: '戌', label: '戌时' }, { value: '亥', label: '亥时' }
]

const pillars = computed(() => {
  if (!chart.value) return []
  return [
    { name: '年柱', stem: chart.value.yearPillar?.[0] || '—', branch: chart.value.yearPillar?.[1] || '—' },
    { name: '月柱', stem: chart.value.monthPillar?.[0] || '—', branch: chart.value.monthPillar?.[1] || '—' },
    { name: '日柱', stem: chart.value.dayPillar?.[0] || '—', branch: chart.value.dayPillar?.[1] || '—' },
    { name: '时柱', stem: chart.value.hourPillar?.[0] || '—', branch: chart.value.hourPillar?.[1] || '—' }
  ]
})

const elementsCN = { wood: '木', fire: '火', earth: '土', metal: '金', water: '水' }
const elementColors = { wood: '#27ae60', fire: '#e74c3c', earth: '#f39c12', metal: '#f4d03f', water: '#3498db' }

const parsedElements = computed(() => {
  if (!chart.value?.fiveElements) return {}
  try {
    const raw = typeof chart.value.fiveElements === 'string' ? JSON.parse(chart.value.fiveElements) : chart.value.fiveElements
    const keys = { wood: '木', fire: '火', earth: '土', metal: '金', water: '水' }
    const result = {}
    for (const [en, cn] of Object.entries(keys)) {
      result[en] = (raw[cn] || 0) * 25
    }
    return result
  } catch { return {} }
})

const handleCalc = async () => {
  if (!calcForm.birthDate) { ElMessage.warning('请选择出生日期'); return }
  calcLoading.value = true
  try {
    const res = await calculate(calcForm)
    if (res.code === 200) {
      chart.value = res.data
      showCalc.value = false
      ElMessage.success('八字测算完成')
    } else ElMessage.error(res.msg)
  } catch (e) { ElMessage.error('计算失败') }
  finally { calcLoading.value = false }
}

onMounted(async () => {
  try {
    const res = await getMyChart()
    if (res.code === 200 && res.data) chart.value = res.data
  } catch { /* no chart yet */ }
})
</script>

<style scoped>
.chart-display { margin-top: 8px; }
.pillar-card {
  background: rgba(201,168,76,0.08);
  border: 1px solid rgba(201,168,76,0.2);
  border-radius: 12px;
  padding: 20px 12px;
  text-align: center;
  transition: all 0.3s;
}
.pillar-card:hover { background: rgba(201,168,76,0.15); transform: translateY(-2px); }
.pillar-title { color: #8a7a6a; font-size: 12px; margin-bottom: 8px; }
.pillar-stem { font-size: 36px; color: #c9a84c; font-weight: bold; font-family: 'STKaiti', serif; }
.pillar-branch { font-size: 22px; color: #e8d5c4; margin-top: 4px; font-family: 'STKaiti', serif; }

.info-grid { display: flex; gap: 16px; margin: 16px 0; }
.info-item { flex: 1; background: rgba(255,255,255,0.04); padding: 12px; border-radius: 8px; }
.info-item .label { color: #8a7a6a; font-size: 12px; display: block; }
.info-item .value { color: #e8d5c4; font-size: 20px; font-family: 'STKaiti', serif; }

.five-elements { padding: 8px 0; }
.element-bar { display: flex; align-items: center; gap: 12px; margin-bottom: 12px; }
.elem-label { width: 30px; color: #c4b5a0; font-weight: bold; }
.elem-count { width: 40px; color: #8a7a6a; font-size: 12px; }
:deep(.el-progress) { flex: 1; }

.gods-section { display: flex; gap: 12px; justify-content: center; }
.gods-section .el-tag { font-size: 14px; padding: 6px 16px; }

.summary-text {
  color: #c4b5a0;
  line-height: 1.8;
  padding: 12px;
  background: rgba(255,255,255,0.03);
  border-radius: 8px;
  font-size: 14px;
  white-space: pre-wrap;
}

.empty-state { text-align: center; padding: 60px 0; }
.empty-icon { font-size: 48px; }
.empty-state p { color: #6a5a4a; margin-top: 12px; }
</style>
