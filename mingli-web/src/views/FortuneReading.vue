<template>
  <div class="fortune-reading">
    <el-card class="section" shadow="never">
      <template #header><span>📜 命理解读</span></template>
      <el-row :gutter="16">
        <el-col :span="8">
          <el-select v-model="readingType" placeholder="选择解读类型" style="width:100%">
            <el-option label="总体运势" value="general" />
            <el-option label="财运分析" value="wealth" />
            <el-option label="婚姻情感" value="marriage" />
            <el-option label="事业前程" value="career" />
          </el-select>
        </el-col>
        <el-col :span="8">
          <el-button type="primary" @click="handleGenerate" :loading="generating" class="gen-btn">生成解读</el-button>
        </el-col>
      </el-row>

      <div v-if="currentReading" class="reading-result">
        <el-divider content-position="center">解读结果</el-divider>
        <el-tag :type="readingTagType" size="small" class="reading-tag">{{ readingTypeLabel }}</el-tag>
        <div class="reading-content">{{ currentReading.content }}</div>
        <div class="reading-score">
          运势评分：<span :class="scoreClass(currentReading.score)">{{ currentReading.score }}</span> 分
        </div>
      </div>
    </el-card>

    <el-card class="section history-section" shadow="never">
      <template #header><span>📋 历史解读</span></template>
      <div v-if="history.length === 0" class="empty">暂无解读记录</div>
      <div v-for="item in history" :key="item.id" class="history-item">
        <div class="history-header">
          <el-tag size="small" :type="typeTag(item.readingType)">{{ typeLabel(item.readingType) }}</el-tag>
          <span class="history-time">{{ item.createTime }}</span>
          <span class="history-score" :class="scoreClass(item.score)">{{ item.score }}分</span>
        </div>
        <div class="history-preview">{{ item.content?.substring(0, 120) }}...</div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { requestReading, getReadings } from '@/api/fortune'

const readingType = ref('general')
const currentReading = ref(null)
const history = ref([])
const generating = ref(false)

const readingTypeLabel = computed(() => {
  const map = { general: '总体运势', wealth: '财运分析', marriage: '婚姻情感', career: '事业前程' }
  return map[readingType.value] || '总体运势'
})
const readingTagType = computed(() => {
  const map = { general: 'info', wealth: 'warning', marriage: 'danger', career: 'primary' }
  return map[readingType.value] || 'info'
})

const typeTag = (t) => ({ general: 'info', wealth: 'warning', marriage: 'danger', career: 'primary' }[t] || 'info')
const typeLabel = (t) => ({ general: '总体', wealth: '财运', marriage: '婚姻', career: '事业' }[t] || t)
const scoreClass = (s) => s >= 70 ? 'score-high' : s >= 40 ? 'score-mid' : 'score-low'

const handleGenerate = async () => {
  generating.value = true
  try {
    const res = await requestReading({ readingType: readingType.value })
    if (res.code === 200) {
      currentReading.value = res.data
      ElMessage.success('解读完成')
      // 刷新历史
      loadHistory()
    } else ElMessage.error(res.msg)
  } catch (e) { ElMessage.error('生成失败') }
  finally { generating.value = false }
}

const loadHistory = async () => {
  try {
    const res = await getReadings()
    if (res.code === 200) history.value = res.data || []
  } catch { /* ignore */ }
}


onMounted(() => loadHistory())
</script>

<style scoped>
.gen-btn { height: 40px; width: 100%; background: linear-gradient(135deg, #8b6914, #c9a84c); border: none; }
.reading-result { margin-top: 20px; }
.reading-tag { margin-bottom: 12px; display: inline-block; }
.reading-content {
  color: #c4b5a0; line-height: 1.8; white-space: pre-wrap;
  background: rgba(255,255,255,0.03); padding: 16px; border-radius: 8px;
  font-size: 14px;
}
.reading-score { margin-top: 12px; color: #8a7a6a; font-size: 14px; }
.score-high { color: #2ecc71; font-weight: bold; }
.score-mid { color: #f4d03f; font-weight: bold; }
.score-low { color: #e74c3c; font-weight: bold; }
.history-section { margin-top: 20px; }
.history-item {
  padding: 12px 0;
  border-bottom: 1px solid rgba(255,255,255,0.05);
}
.history-item:last-child { border-bottom: none; }
.history-header { display: flex; align-items: center; gap: 12px; margin-bottom: 8px; }
.history-time { color: #6a5a4a; font-size: 12px; flex: 1; }
.history-score { font-size: 14px; }
.history-preview { color: #5a4a3a; font-size: 13px; }
:deep(.el-divider__text) { color: #c9a84c; }
</style>
