<template>
  <div class="divination-page">
    <el-card class="section" shadow="never">
      <template #header><span>🔮 占卜问卦</span></template>

      <div class="form-section">
        <el-input
          v-model="question"
          type="textarea"
          :rows="4"
          placeholder="请输入您要占问的问题……"
          maxlength="200"
          show-word-limit
          class="question-input"
        />

        <div class="method-section">
          <span class="method-label">占卜方式：</span>
          <el-radio-group v-model="method">
            <el-radio value="六爻">六爻占卜</el-radio>
            <el-radio value="梅花易数">梅花易数</el-radio>
          </el-radio-group>
        </div>

        <el-button type="primary" class="ask-btn" @click="handleAsk" :loading="asking">
          <span style="margin-right:4px">⚡</span> 起卦
        </el-button>
      </div>
    </el-card>

    <el-card v-if="result" class="section result-card" shadow="never">
      <template #header><span>🔮 卦象解读</span></template>

      <div class="hexagram-header">
        <div class="hexagram-symbol">{{ result.hexagramName }}</div>
        <div class="hexagram-code">{{ result.hexagramSymbol }}</div>
      </div>

      <el-divider content-position="center">卦象解读</el-divider>
      <div class="interpretation">{{ result.interpretation }}</div>

      <el-divider content-position="center">建议</el-divider>
      <div class="advice">{{ result.advice }}</div>
    </el-card>

    <el-card class="section history-card" shadow="never">
      <template #header><span>📋 占卜历史</span></template>
      <div v-if="history.length === 0" class="empty">暂无占卜记录</div>
      <div v-for="item in history" :key="item.id" class="history-item" @click="viewHistory(item)">
        <div class="history-q">{{ item.question?.substring(0, 50) }}{{ item.question?.length > 50 ? '...' : '' }}</div>
        <div class="history-meta">
          <span class="hex-name">{{ item.hexagramName }}</span>
          <span class="history-time">{{ item.createTime }}</span>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElDrawer } from 'element-plus'
import { ask as askDivination } from '@/api/divination'

const question = ref('')
const method = ref('六爻')
const asking = ref(false)
const result = ref(null)

const history = ref([
  { id: 0, question: '今日运势如何', hexagramName: '地天泰', createTime: '—', interpretation: '暂无历史', advice: '' }
])

const handleAsk = async () => {
  if (!question.value.trim()) { ElMessage.warning('请输入您要占问的问题'); return }
  asking.value = true
  try {
    const res = await askDivination({ question: question.value, method: method.value })
    if (res.code === 200) {
      result.value = res.data
      ElMessage.success('起卦完成')
    } else ElMessage.error(res.msg)
  } catch (e) { ElMessage.error('起卦失败') }
  finally { asking.value = false }
}

const viewHistory = (item) => {
  if (item.interpretation && item.interpretation !== '暂无历史') {
    result.value = item
  }
}

const loadHistory = async () => {
  try {
    const { getReadings } = await import('@/api/fortune')
    const res = await getReadings()
    if (res.code === 200 && res.data) {
      // 占卜历史走 divination 接口 — 暂不单独实现
    }
  } catch { /* ignore */ }
}

onMounted(() => loadHistory())
</script>

<style scoped>
.form-section { padding: 8px 0; }
.question-input { margin-bottom: 16px; }
:deep(.el-textarea__inner) {
  background: rgba(255,255,255,0.06);
  border: 1px solid rgba(201,168,76,0.15);
  color: #e8d5c4;
  font-size: 15px;
}
.method-section {
  display: flex; align-items: center; gap: 16px;
  margin-bottom: 20px;
}
.method-label { color: #c4b5a0; white-space: nowrap; }
:deep(.el-radio) { color: #c4b5a0; }
:deep(.el-radio.is-checked) { color: #c9a84c; }
.ask-btn {
  width: 100%; height: 46px;
  background: linear-gradient(135deg, #6c3483, #9b59b6);
  border: none;
  font-size: 17px;
}

.result-card { margin-top: 20px; }
.hexagram-header { text-align: center; padding: 16px 0; }
.hexagram-symbol { font-size: 28px; color: #c9a84c; font-weight: bold; font-family: 'STKaiti', serif; }
.hexagram-code { font-size: 36px; color: #8a7a6a; margin-top: 8px; letter-spacing: 4px; }

.interpretation {
  color: #c4b5a0; line-height: 1.9; white-space: pre-wrap;
  background: rgba(255,255,255,0.03); padding: 16px; border-radius: 8px;
}
.advice {
  color: #c9a84c; font-size: 15px; line-height: 1.8;
  text-align: center; font-style: italic;
}

.history-card { margin-top: 20px; }
.history-item {
  padding: 12px 0;
  border-bottom: 1px solid rgba(255,255,255,0.05);
  cursor: pointer;
  transition: background 0.2s;
}
.history-item:hover { background: rgba(255,255,255,0.03); }
.history-q { color: #c4b5a0; font-size: 14px; margin-bottom: 4px; }
.history-meta { display: flex; gap: 12px; font-size: 12px; }
.hex-name { color: #9b59b6; }
.history-time { color: #6a5a4a; }
:deep(.el-divider__text) { color: #c9a84c; }
.empty { text-align: center; padding: 40px 0; color: #6a5a4a; }
</style>
