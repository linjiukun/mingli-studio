<template>
  <div class="zodiac-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">✦ 十二星座</h1>
      <p class="page-subtitle">探索星辰的奥秘，洞悉命运的轨迹</p>
    </div>

    <!-- 我的星座 -->
    <div v-if="mySign" class="my-zodiac-card">
      <div class="my-zodiac-content">
        <div class="zodiac-big-icon">{{ mySign.unicode }}</div>
        <div class="my-zodiac-info">
          <div class="my-zodiac-name">{{ mySign.nameCn }}（{{ mySign.nameEn }}）</div>
          <div class="my-zodiac-meta">
            <span>元素：{{ mySign.element }}</span>
            <span>｜</span>
            <span>主宰星：{{ mySign.rulingPlanet }}</span>
          </div>
          <div class="my-zodiac-date">日期范围：{{ mySign.dateRange }}</div>
        </div>
        <el-button type="warning" plain @click="scrollToSign(mySign.index)" class="my-zodiac-btn">
          查看详情
        </el-button>
      </div>
    </div>

    <!-- Tab 切换 -->
    <el-tabs v-model="activeTab" class="zodiac-tabs" @tab-change="onTabChange">
      <!-- 星座列表 -->
      <el-tab-pane label="♈ 星座大全" name="signs">
        <div class="zodiac-grid">
          <div
            v-for="(sign, idx) in allSigns"
            :key="idx"
            :ref="el => { if (el) signRefs[idx] = el }"
            class="zodiac-card"
            :class="{ active: selectedSign === idx }"
            @click="selectSign(idx)"
          >
            <div class="zodiac-icon">{{ sign.unicode }}</div>
            <div class="zodiac-name">{{ sign.nameCn }}</div>
            <div class="zodiac-en">{{ sign.nameEn }}</div>
            <div class="zodiac-date">{{ sign.dateRange }}</div>
            <div class="zodiac-element" :class="'elem-' + sign.element">{{ sign.element }}元素</div>
            <div class="zodiac-planet">{{ sign.rulingPlanet }}</div>
          </div>
        </div>

        <!-- 选中星座详情 -->
        <div v-if="selectedSign !== null && selectedSignDetails" class="sign-detail-panel">
          <div class="detail-header">
            <div class="detail-icon">{{ selectedSignDetails.unicode }}</div>
            <div class="detail-title">
              <h2>{{ selectedSignDetails.nameCn }}</h2>
              <span class="detail-en">{{ selectedSignDetails.nameEn }}</span>
              <span class="detail-date">{{ selectedSignDetails.dateRange }}</span>
            </div>
          </div>

          <el-row :gutter="24">
            <el-col :span="8">
              <div class="detail-card">
                <div class="detail-card-title">🔮 基本信息</div>
                <div class="detail-item"><label>元素</label><span>{{ selectedSignDetails.element }}</span></div>
                <div class="detail-item"><label>主宰星</label><span>{{ selectedSignDetails.rulingPlanet }}</span></div>
                <div class="detail-item"><label>幸运数字</label><span>{{ selectedSignDetails.luckyNumbers?.join('、') }}</span></div>
                <div class="detail-item"><label>幸运颜色</label><span>{{ selectedSignDetails.luckyColors?.join('、') }}</span></div>
              </div>
            </el-col>
            <el-col :span="16">
              <div class="detail-card">
                <div class="detail-card-title">📖 性格特征</div>
                <div class="detail-traits">
                  <el-tag
                    v-for="(trait, ti) in selectedSignDetails.traits"
                    :key="ti"
                    class="trait-tag"
                    :type="getTagType(ti)"
                    size="medium"
                  >{{ trait }}</el-tag>
                </div>
                <el-divider />
                <p class="detail-desc">{{ selectedSignDetails.description }}</p>
                <el-divider />
                <div class="detail-match">
                  <div class="match-section">
                    <span class="match-label">💞 最佳配对</span>
                    <div class="match-signs">
                      <span
                        v-for="(bm, bi) in selectedSignDetails.bestMatch"
                        :key="bi"
                        class="match-tag best"
                        @click="openCompatibility(selectedSignDetails.index, bm)"
                      >{{ getSignName(bm) }} {{ getSignUnicode(bm) }}</span>
                    </div>
                  </div>
                  <div class="match-section" style="margin-top: 8px;">
                    <span class="match-label">⚠️ 挑战配对</span>
                    <div class="match-signs">
                      <span
                        v-for="(wm, wi) in selectedSignDetails.worstMatch"
                        :key="wi"
                        class="match-tag worst"
                        @click="openCompatibility(selectedSignDetails.index, wm)"
                      >{{ getSignName(wm) }} {{ getSignUnicode(wm) }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>

          <!-- 今日运势速览 -->
          <div v-if="selectedDaily" class="daily-preview">
            <h3>📅 今日运势 · {{ selectedSignDetails.nameCn }}</h3>
            <el-row :gutter="16">
              <el-col :span="6">
                <div class="score-card">
                  <div class="score-value" :style="{ color: getScoreColor(selectedDaily.overallScore) }">{{ selectedDaily.overallScore }}</div>
                  <div class="score-label">综合评分</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="score-card">
                  <div class="score-value" :style="{ color: getScoreColor(selectedDaily.careerScore) }">{{ selectedDaily.careerScore }}</div>
                  <div class="score-label">事业</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="score-card">
                  <div class="score-value" :style="{ color: getScoreColor(selectedDaily.wealthScore) }">{{ selectedDaily.wealthScore }}</div>
                  <div class="score-label">财运</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="score-card">
                  <div class="score-value" :style="{ color: getScoreColor(selectedDaily.loveScore) }">{{ selectedDaily.loveScore }}</div>
                  <div class="score-label">爱情</div>
                </div>
              </el-col>
            </el-row>
            <div class="lucky-info">
              幸运数字：<span class="lucky-tag">{{ selectedDaily.luckyNumber }}</span>
              幸运颜色：<span class="lucky-tag">{{ selectedDaily.luckyColor }}</span>
              幸运方位：<span class="lucky-tag">{{ selectedDaily.luckyDirection }}</span>
              建议：<span class="advice-text">{{ selectedDaily.advice }}</span>
            </div>
          </div>
        </div>
      </el-tab-pane>

      <!-- 每日运势 -->
      <el-tab-pane label="☀️ 每日运势" name="daily">
        <div class="horoscope-panel">
          <div class="panel-header">
            <h2>📅 今日星座运势</h2>
            <p class="panel-date">{{ todayStr }}</p>
          </div>

          <div class="daily-grid">
            <div
              v-for="(sign, idx) in dailyHoroscopes"
              :key="idx"
              class="daily-card"
              @click="showDailyDetail(idx)"
            >
              <div class="daily-unicode">{{ sign.unicode }}</div>
              <div class="daily-name">{{ sign.nameCn }}</div>
              <div class="daily-score" :style="{ color: getScoreColor(sign.overallScore) }">{{ sign.overallScore }}</div>
              <div class="daily-label">综合运势</div>
              <el-progress
                :percentage="sign.overallScore"
                :color="getScoreColor(sign.overallScore)"
                :show-text="false"
                :stroke-width="6"
              />
            </div>
          </div>

          <!-- 运势详情弹窗 -->
          <el-dialog
            v-model="dailyDetailVisible"
            :title="dailyDetail ? dailyDetail.zodiacName + ' 今日运势' : ''"
            width="520px"
            class="daily-dialog"
          >
            <div v-if="dailyDetail" class="daily-detail-content">
              <div class="daily-detail-header">
                <span class="dd-icon">{{ dailyDetail.unicode }}</span>
                <div class="dd-title">
                  <h3>{{ dailyDetail.zodiacName }}</h3>
                  <span>{{ dailyDetail.date }}</span>
                </div>
              </div>

              <div class="dd-scores">
                <div class="ds-item" v-for="item in scoreItems" :key="item.key">
                  <div class="ds-label">{{ item.label }}</div>
                  <el-progress
                    :percentage="dailyDetail[item.key]"
                    :color="getScoreColor(dailyDetail[item.key])"
                    :stroke-width="12"
                    :text-inside="true"
                    :format="() => dailyDetail[item.key] + '分'"
                  />
                </div>
              </div>

              <el-divider />

              <div class="dd-descriptions">
                <div class="dd-desc-item">
                  <div class="dd-desc-label">📝 整体运势</div>
                  <p>{{ dailyDetail.overallDesc }}</p>
                </div>
                <div class="dd-desc-item">
                  <div class="dd-desc-label">💼 事业</div>
                  <p>{{ dailyDetail.careerDesc }}</p>
                </div>
                <div class="dd-desc-item">
                  <div class="dd-desc-label">💰 财运</div>
                  <p>{{ dailyDetail.wealthDesc }}</p>
                </div>
                <div class="dd-desc-item">
                  <div class="dd-desc-label">❤️ 爱情</div>
                  <p>{{ dailyDetail.loveDesc }}</p>
                </div>
                <div class="dd-desc-item">
                  <div class="dd-desc-label">🏃 健康</div>
                  <p>{{ dailyDetail.healthDesc }}</p>
                </div>
              </div>

              <el-divider />

              <div class="dd-lucky">
                <span>🍀 幸运数字：<strong>{{ dailyDetail.luckyNumber }}</strong></span>
                <span>🎨 幸运颜色：<strong>{{ dailyDetail.luckyColor }}</strong></span>
                <span>🧭 幸运方位：<strong>{{ dailyDetail.luckyDirection }}</strong></span>
              </div>

              <div class="dd-advice">
                <span class="advice-label">💡 每日建议</span>
                <p>{{ dailyDetail.advice }}</p>
              </div>
            </div>
          </el-dialog>
        </div>
      </el-tab-pane>

      <!-- 周/月运势 -->
      <el-tab-pane label="📊 周/月运势" name="period">
        <div class="period-panel">
          <el-radio-group v-model="periodType" class="period-toggle">
            <el-radio-button value="weekly">周运势</el-radio-button>
            <el-radio-button value="monthly">月运势</el-radio-button>
          </el-radio-group>

          <div class="period-select">
            <span>选择星座：</span>
            <el-select v-model="periodSign" placeholder="请选择星座" size="large">
              <el-option
                v-for="(sign, idx) in allSigns"
                :key="idx"
                :label="sign.nameCn + ' ' + sign.unicode"
                :value="idx"
              />
            </el-select>
            <el-button type="warning" @click="loadPeriodHoroscope" style="margin-left: 16px;">
              查看运势
            </el-button>
          </div>

          <div v-if="periodResult" class="period-result">
            <div class="period-icon">{{ periodResult.unicode }} {{ periodResult.zodiacName }}</div>
            <div class="period-score">
              <span class="period-label">{{ periodType === 'weekly' ? '本周' : '本月' }}综合运势</span>
              <span class="period-value" :style="{ color: getScoreColor(periodResult.overallScore || (periodType === 'weekly' ? 75 : 70)) }">
                {{ periodType === 'weekly' ? (periodResult.overallStars || '★★★★☆') : (periodResult.overallScore || '85') + '分' }}
              </span>
            </div>
            <div class="period-desc">
              <p>{{ periodResult.description }}</p>
            </div>
            <div class="period-lucky">
              幸运数字：<strong>{{ periodResult.luckyNumber }}</strong>
              幸运颜色：<strong>{{ periodResult.luckyColor }}</strong>
            </div>
          </div>
        </div>
      </el-tab-pane>

      <!-- 星座配对 -->
      <el-tab-pane label="💞 星座配对" name="compatibility">
        <div class="compatibility-panel">
          <h2>💞 星座配对分析</h2>
          <p class="panel-desc">选择两个星座，查看你们的缘分指数</p>

          <div class="compatibility-select">
            <div class="comp-sign-select">
              <span>星座一：</span>
              <el-select v-model="compSign1" placeholder="选择星座" size="large">
                <el-option
                  v-for="(sign, idx) in allSigns"
                  :key="idx"
                  :label="sign.nameCn + ' ' + sign.unicode"
                  :value="idx"
                />
              </el-select>
            </div>
            <div class="comp-vs">💫</div>
            <div class="comp-sign-select">
              <span>星座二：</span>
              <el-select v-model="compSign2" placeholder="选择星座" size="large">
                <el-option
                  v-for="(sign, idx) in allSigns"
                  :key="idx"
                  :label="sign.nameCn + ' ' + sign.unicode"
                  :value="idx"
                />
              </el-select>
            </div>
            <el-button type="warning" size="large" @click="loadCompatibility" :disabled="compSign1 === null || compSign2 === null">
              开始配对
            </el-button>
          </div>

          <div v-if="compResult" class="comp-result">
            <div class="comp-header">
              <div class="comp-sign-box">
                <div class="comp-big-icon">{{ compResult.sign1Unicode }}</div>
                <div class="comp-sign-name">{{ compResult.sign1 }}</div>
                <div class="comp-element">{{ compResult.element1 }}元素</div>
              </div>
              <div class="comp-middle">
                <div class="comp-score-circle" :style="{ borderColor: getScoreColor(compResult.compatibilityScore) }">
                  <span class="comp-big-score">{{ compResult.compatibilityScore }}</span>
                  <span class="comp-score-label">分</span>
                </div>
                <div class="comp-level" :style="{ color: getScoreColor(compResult.compatibilityScore) }">
                  {{ compResult.level }}
                </div>
              </div>
              <div class="comp-sign-box">
                <div class="comp-big-icon">{{ compResult.sign2Unicode }}</div>
                <div class="comp-sign-name">{{ compResult.sign2 }}</div>
                <div class="comp-element">{{ compResult.element2 }}元素</div>
              </div>
            </div>

            <el-divider />

            <div class="comp-desc">
              <p>{{ compResult.description }}</p>
            </div>

            <div class="comp-tip">
              <span class="tip-label">💡 配对建议</span>
              <p>{{ compResult.tip }}</p>
            </div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import {
  getMyZodiac, getAllSigns, getSignDetail,
  getDailyHoroscope, getWeeklyHoroscope, getMonthlyHoroscope,
  getCompatibility
} from '@/api/zodiac'

// ======================== 状态 ========================
const activeTab = ref('signs')
const allSigns = ref([])
const mySign = ref(null)
const selectedSign = ref(null)
const selectedSignDetails = ref(null)
const selectedDaily = ref(null)
const signRefs = ref({})

// 每日运势
const dailyHoroscopes = ref([])
const dailyDetailVisible = ref(false)
const dailyDetail = ref(null)
const todayStr = ref('')

// 周/月运势
const periodType = ref('weekly')
const periodSign = ref(null)
const periodResult = ref(null)

// 配对
const compSign1 = ref(null)
const compSign2 = ref(null)
const compResult = ref(null)

const scoreItems = [
  { key: 'overallScore', label: '综合运势' },
  { key: 'careerScore', label: '事业运势' },
  { key: 'wealthScore', label: '财运运势' },
  { key: 'loveScore', label: '爱情运势' },
  { key: 'healthScore', label: '健康运势' }
]

// ======================== 方法 ========================

function getTagType(index) {
  const types = ['', 'success', 'warning', 'info', 'danger', 'primary']
  return types[index % types.length]
}

function getScoreColor(score) {
  if (score >= 85) return '#67c23a'
  if (score >= 70) return '#c9a84c'
  if (score >= 55) return '#e6a23c'
  return '#f56c6c'
}

function getSignName(index) {
  if (allSigns.value[index]) return allSigns.value[index].nameCn
  return ''
}

function getSignUnicode(index) {
  if (allSigns.value[index]) return allSigns.value[index].unicode
  return ''
}

function selectSign(index) {
  selectedSign.value = index
  selectedSignDetails.value = allSigns.value[index]
  // 加载今日运势
  getDailyHoroscope(index, '').then(res => {
    if (res.code === 200) {
      selectedDaily.value = res.data
    }
  })
}

function scrollToSign(index) {
  selectedSign.value = index
  selectedSignDetails.value = allSigns.value[index]
  getDailyHoroscope(index, '').then(res => {
    if (res.code === 200) {
      selectedDaily.value = res.data
    }
  })
  // 切换到星座大全tab
  activeTab.value = 'signs'
}

function onTabChange(tab) {
  if (tab === 'daily') {
    loadAllDaily()
  }
}

function loadAllDaily() {
  const today = new Date()
  todayStr.value = `${today.getFullYear()}年${today.getMonth() + 1}月${today.getDate()}日`
  const promises = []
  for (let i = 0; i < 12; i++) {
    promises.push(getDailyHoroscope(i, '').then(res => {
      if (res.code === 200) {
        dailyHoroscopes.value[i] = res.data
      }
    }))
  }
  Promise.all(promises)
}

function showDailyDetail(index) {
  dailyDetail.value = dailyHoroscopes.value[index]
  dailyDetailVisible.value = true
}

function loadPeriodHoroscope() {
  if (periodSign.value === null) return
  const fn = periodType.value === 'weekly' ? getWeeklyHoroscope : getMonthlyHoroscope
  fn(periodSign.value, '').then(res => {
    if (res.code === 200) {
      periodResult.value = res.data
    }
  })
}

function loadCompatibility() {
  if (compSign1.value === null || compSign2.value === null) return
  getCompatibility(compSign1.value, compSign2.value).then(res => {
    if (res.code === 200) {
      compResult.value = res.data
    }
  })
}

function openCompatibility(s1, s2) {
  compSign1.value = s1
  compSign2.value = s2
  activeTab.value = 'compatibility'
  loadCompatibility()
}

// ======================== 初始化 ========================

onMounted(() => {
  // 获取所有星座
  getAllSigns().then(res => {
    if (res.code === 200) {
      allSigns.value = res.data
    }
  })

  // 获取我的星座
  getMyZodiac().then(res => {
    if (res.code === 200) {
      mySign.value = res.data
    }
  })

  // 预加载每日运势
  for (let i = 0; i < 12; i++) {
    dailyHoroscopes.value[i] = { unicode: '♈', nameCn: '加载中...', overallScore: 0 }
  }
  loadAllDaily()
})
</script>

<style scoped>
.zodiac-container {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  text-align: center;
  margin-bottom: 28px;
}

.page-title {
  font-size: 28px;
  color: var(--text-gold);
  letter-spacing: 4px;
  margin: 0;
}

.page-subtitle {
  color: var(--text-muted);
  font-size: 14px;
  letter-spacing: 2px;
  margin-top: 8px;
}

/* 我的星座卡片 */
.my-zodiac-card {
  background: linear-gradient(135deg, rgba(201, 168, 76, 0.08), rgba(201, 168, 76, 0.02));
  border: 1px solid rgba(201, 168, 76, 0.2);
  border-radius: 16px;
  padding: 20px 28px;
  margin-bottom: 24px;
}

.my-zodiac-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.zodiac-big-icon {
  font-size: 48px;
  line-height: 1;
}

.my-zodiac-info {
  flex: 1;
}

.my-zodiac-name {
  font-size: 20px;
  color: var(--text-gold);
  font-weight: 600;
}

.my-zodiac-meta {
  color: var(--text-muted);
  font-size: 13px;
  margin-top: 4px;
}

.my-zodiac-date {
  color: var(--text-secondary);
  font-size: 12px;
  margin-top: 2px;
}

/* Tab 样式 */
.zodiac-tabs :deep(.el-tabs__item) {
  color: var(--text-muted);
  font-size: 15px;
  letter-spacing: 1px;
}

.zodiac-tabs :deep(.el-tabs__item.is-active) {
  color: var(--text-gold);
}

.zodiac-tabs :deep(.el-tabs__active-bar) {
  background: var(--gold);
}

/* 星座网格 */
.zodiac-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.zodiac-card {
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 14px;
  padding: 20px 16px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.zodiac-card:hover {
  border-color: rgba(201, 168, 76, 0.3);
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
}

.zodiac-card.active {
  border-color: var(--gold);
  background: rgba(201, 168, 76, 0.06);
}

.zodiac-icon {
  font-size: 36px;
  line-height: 1;
  margin-bottom: 8px;
}

.zodiac-name {
  font-size: 16px;
  color: var(--text-gold);
  font-weight: 600;
}

.zodiac-en {
  font-size: 12px;
  color: var(--text-muted);
  margin-top: 2px;
}

.zodiac-date {
  font-size: 11px;
  color: var(--text-secondary);
  margin-top: 6px;
}

.zodiac-element {
  display: inline-block;
  padding: 2px 10px;
  border-radius: 10px;
  font-size: 11px;
  margin-top: 8px;
}

.elem-火 { background: rgba(245, 108, 108, 0.15); color: #f56c6c; }
.elem-土 { background: rgba(230, 162, 60, 0.15); color: #e6a23c; }
.elem-风 { background: rgba(64, 158, 255, 0.15); color: #409eff; }
.elem-水 { background: rgba(103, 194, 58, 0.15); color: #67c23a; }

.zodiac-planet {
  font-size: 11px;
  color: var(--text-muted);
  margin-top: 4px;
}

/* 详情面板 */
.sign-detail-panel {
  background: rgba(255, 255, 255, 0.02);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 16px;
  padding: 28px;
  margin-top: 8px;
}

.detail-header {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 24px;
}

.detail-icon {
  font-size: 56px;
  line-height: 1;
}

.detail-title h2 {
  color: var(--text-gold);
  font-size: 24px;
  margin: 0;
}

.detail-en {
  color: var(--text-muted);
  font-size: 14px;
  margin-right: 12px;
}

.detail-date {
  color: var(--text-secondary);
  font-size: 13px;
}

.detail-card {
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 16px;
}

.detail-card-title {
  color: var(--text-gold);
  font-size: 16px;
  margin-bottom: 12px;
  font-weight: 600;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  padding: 6px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.04);
  font-size: 14px;
}

.detail-item label {
  color: var(--text-muted);
}

.detail-item span {
  color: var(--text-primary);
}

.detail-traits {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.trait-tag {
  letter-spacing: 1px;
}

.detail-desc {
  color: var(--text-secondary);
  line-height: 1.8;
  font-size: 14px;
}

.match-label {
  color: var(--text-muted);
  font-size: 13px;
}

.match-signs {
  display: flex;
  gap: 8px;
  margin-top: 6px;
}

.match-tag {
  padding: 4px 12px;
  border-radius: 8px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}

.match-tag.best {
  background: rgba(103, 194, 58, 0.12);
  color: #67c23a;
  border: 1px solid rgba(103, 194, 58, 0.2);
}

.match-tag.worst {
  background: rgba(245, 108, 108, 0.12);
  color: #f56c6c;
  border: 1px solid rgba(245, 108, 108, 0.2);
}

.match-tag:hover {
  transform: scale(1.05);
}

/* 每日运势预览 */
.daily-preview {
  margin-top: 24px;
}

.daily-preview h3 {
  color: var(--text-gold);
  margin-bottom: 16px;
}

.score-card {
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 12px;
  padding: 16px;
  text-align: center;
}

.score-value {
  font-size: 36px;
  font-weight: 700;
}

.score-label {
  font-size: 13px;
  color: var(--text-muted);
  margin-top: 4px;
}

.lucky-info {
  margin-top: 16px;
  padding: 12px 16px;
  background: rgba(255, 255, 255, 0.02);
  border-radius: 10px;
  font-size: 13px;
  color: var(--text-secondary);
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
  align-items: center;
}

.lucky-tag {
  color: var(--text-gold);
  font-weight: 600;
  margin-right: 8px;
}

.advice-text {
  color: var(--text-primary);
  font-style: italic;
}

/* ====== 每日运势 Tab ====== */
.horoscope-panel {
  padding: 8px;
}

.panel-header {
  text-align: center;
  margin-bottom: 24px;
  padding: 20px;
  background: rgba(201, 168, 76, 0.04);
  border-radius: 12px;
}

.panel-header h2 {
  margin: 0;
  color: var(--text-gold);
  font-size: 22px;
}

.panel-date {
  color: var(--text-muted);
  font-size: 14px;
  margin-top: 4px;
}

.daily-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 14px;
}

.daily-card {
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 14px;
  padding: 18px 12px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.daily-card:hover {
  border-color: rgba(201, 168, 76, 0.3);
  transform: translateY(-2px);
}

.daily-unicode {
  font-size: 28px;
  margin-bottom: 4px;
}

.daily-name {
  font-size: 14px;
  color: var(--text-primary);
}

.daily-score {
  font-size: 30px;
  font-weight: 700;
  margin-top: 4px;
}

.daily-label {
  font-size: 11px;
  color: var(--text-muted);
  margin-bottom: 6px;
}

/* 运势详情弹窗 */
.daily-dialog :deep(.el-dialog) {
  background: var(--bg-primary) !important;
  border: 1px solid rgba(201, 168, 76, 0.2);
}

.daily-dialog :deep(.el-dialog__title) {
  color: var(--text-gold);
}

.daily-detail-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}

.dd-icon {
  font-size: 42px;
  line-height: 1;
}

.dd-title h3 {
  margin: 0;
  color: var(--text-gold);
  font-size: 20px;
}

.dd-title span {
  color: var(--text-muted);
  font-size: 13px;
}

.dd-scores {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.ds-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.ds-label {
  font-size: 13px;
  color: var(--text-muted);
}

.dd-description {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.dd-desc-item {
  border-left: 3px solid var(--gold);
  padding-left: 12px;
  margin-bottom: 8px;
}

.dd-desc-label {
  font-size: 14px;
  color: var(--text-gold);
  margin-bottom: 4px;
  font-weight: 600;
}

.dd-desc-item p {
  margin: 0;
  color: var(--text-secondary);
  font-size: 13px;
  line-height: 1.6;
}

.dd-lucky {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
  font-size: 14px;
  color: var(--text-secondary);
}

.dd-lucky strong {
  color: var(--text-gold);
}

.dd-advice {
  margin-top: 12px;
  padding: 12px 16px;
  background: rgba(201, 168, 76, 0.06);
  border-radius: 10px;
  border: 1px solid rgba(201, 168, 76, 0.12);
}

.advice-label {
  color: var(--text-gold);
  font-weight: 600;
  font-size: 14px;
}

.dd-advice p {
  margin: 6px 0 0;
  color: var(--text-primary);
  font-style: italic;
  line-height: 1.6;
}

/* ====== 周/月运势 ====== */
.period-panel {
  padding: 8px;
}

.period-toggle {
  display: flex;
  justify-content: center;
  margin-bottom: 24px;
}

.period-select {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  flex-wrap: wrap;
  margin-bottom: 24px;
}

.period-select span {
  color: var(--text-muted);
}

.period-result {
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 16px;
  padding: 28px;
  text-align: center;
}

.period-icon {
  font-size: 36px;
  color: var(--text-gold);
  margin-bottom: 12px;
}

.period-score {
  margin-bottom: 16px;
}

.period-label {
  display: block;
  color: var(--text-muted);
  font-size: 13px;
  margin-bottom: 6px;
}

.period-value {
  font-size: 28px;
  font-weight: 700;
}

.period-desc {
  max-width: 600px;
  margin: 0 auto 16px;
}

.period-desc p {
  color: var(--text-secondary);
  line-height: 1.8;
  font-size: 14px;
}

.period-lucky {
  color: var(--text-muted);
  font-size: 13px;
  display: flex;
  gap: 20px;
  justify-content: center;
}

.period-lucky strong {
  color: var(--text-gold);
}

/* ====== 配对面板 ====== */
.compatibility-panel {
  padding: 8px;
  text-align: center;
}

.compatibility-panel h2 {
  color: var(--text-gold);
  font-size: 22px;
  margin: 0;
}

.panel-desc {
  color: var(--text-muted);
  font-size: 14px;
  margin: 8px 0 24px;
}

.compatibility-select {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  flex-wrap: wrap;
  margin-bottom: 28px;
}

.comp-sign-select {
  display: flex;
  align-items: center;
  gap: 8px;
}

.comp-sign-select span {
  color: var(--text-muted);
  white-space: nowrap;
}

.comp-vs {
  font-size: 28px;
}

.comp-result {
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 16px;
  padding: 28px;
}

.comp-header {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 24px;
}

.comp-sign-box {
  text-align: center;
}

.comp-big-icon {
  font-size: 48px;
  line-height: 1;
  margin-bottom: 8px;
}

.comp-sign-name {
  color: var(--text-gold);
  font-size: 18px;
  font-weight: 600;
}

.comp-element {
  color: var(--text-muted);
  font-size: 12px;
  margin-top: 2px;
}

.comp-middle {
  text-align: center;
}

.comp-score-circle {
  width: 100px;
  height: 100px;
  border: 4px solid var(--gold);
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin: 0 auto 8px;
}

.comp-big-score {
  font-size: 36px;
  font-weight: 700;
  line-height: 1;
}

.comp-score-label {
  font-size: 12px;
  color: var(--text-muted);
}

.comp-level {
  font-size: 16px;
  font-weight: 600;
}

.comp-desc {
  max-width: 600px;
  margin: 0 auto;
}

.comp-desc p {
  color: var(--text-secondary);
  line-height: 1.8;
  font-size: 14px;
}

.comp-tip {
  margin-top: 16px;
  padding: 14px 20px;
  background: rgba(201, 168, 76, 0.06);
  border: 1px solid rgba(201, 168, 76, 0.12);
  border-radius: 12px;
  max-width: 500px;
  margin-left: auto;
  margin-right: auto;
}

.tip-label {
  color: var(--text-gold);
  font-weight: 600;
  font-size: 14px;
}

.comp-tip p {
  margin: 6px 0 0;
  color: var(--text-primary);
  line-height: 1.6;
  font-size: 13px;
}

/* 响应式 */
@media (max-width: 900px) {
  .zodiac-grid, .daily-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 600px) {
  .zodiac-grid, .daily-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  .comp-header {
    flex-direction: column;
  }
}
</style>
