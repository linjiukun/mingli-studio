<template>
  <div class="consultant-list">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>命理大师</h2>
      <p>专业命理师为您提供一对一咨询服务</p>
    </div>

    <!-- 命理师列表 -->
    <div class="consultants-container" v-loading="loading">
      <el-row :gutter="24">
        <el-col :xs="24" :sm="12" :md="8" v-for="consultant in consultants" :key="consultant.id">
          <el-card class="consultant-card" shadow="hover">
            <div class="consultant-header">
              <el-avatar :size="80" :src="consultant.userAvatar" class="consultant-avatar">
                {{ consultant.realName?.charAt(0) || '师' }}
              </el-avatar>
              <div class="consultant-info">
                <h3 class="consultant-name">{{ consultant.realName }}</h3>
                <div class="consultant-rating">
                  <el-rate v-model="consultant.rating" disabled show-score text-color="#c9a84c" />
                </div>
                <div class="consultant-count">
                  已服务 {{ consultant.consultationCount || 0 }} 人
                </div>
              </div>
            </div>

            <div class="consultant-expertise">
              <el-tag v-for="tag in parseExpertise(consultant.expertise)" :key="tag" size="small"
                effect="plain" class="expertise-tag">
                {{ tag }}
              </el-tag>
            </div>

            <div class="consultant-intro">
              {{ consultant.introduction || '暂无简介' }}
            </div>

            <div class="consultant-certification" v-if="consultant.certification">
              <el-icon><Medal /></el-icon>
              <span>{{ consultant.certification }}</span>
            </div>

            <div class="consultant-actions">
              <el-button type="primary" @click="viewDetail(consultant.id)">
                查看详情
              </el-button>
              <el-button @click="consult(consultant)">
                立即咨询
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-empty v-if="!loading && consultants.length === 0" description="暂无认证命理师" />
    </div>

    <!-- 申请成为命理师 -->
    <div class="apply-section">
      <el-card class="apply-card" shadow="hover">
        <div class="apply-content">
          <div class="apply-icon">🔮</div>
          <div class="apply-text">
            <h3>成为命理师</h3>
            <p>如果您精通命理学，欢迎加入我们的命理师团队</p>
          </div>
          <el-button type="warning" @click="showApplyDialog">立即申请</el-button>
        </div>
      </el-card>
    </div>

    <!-- 申请对话框 -->
    <el-dialog v-model="applyDialogVisible" title="申请成为命理师" width="500px">
      <el-form :model="applyForm" label-width="100px">
        <el-form-item label="真实姓名" required>
          <el-input v-model="applyForm.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="擅长领域" required>
          <el-select v-model="applyForm.expertiseList" multiple placeholder="请选择擅长领域">
            <el-option label="八字命理" value="八字命理" />
            <el-option label="风水堪舆" value="风水堪舆" />
            <el-option label="易经占卜" value="易经占卜" />
            <el-option label="星座运势" value="星座运势" />
            <el-option label="姓名学" value="姓名学" />
            <el-option label="紫微斗数" value="紫微斗数" />
            <el-option label="面相手相" value="面相手相" />
          </el-select>
        </el-form-item>
        <el-form-item label="个人简介">
          <el-input v-model="applyForm.introduction" type="textarea" :rows="4" placeholder="请简单介绍一下自己..." />
        </el-form-item>
        <el-form-item label="认证信息">
          <el-input v-model="applyForm.certification" placeholder="如有相关证书请填写" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="applyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitApply" :loading="applyLoading">提交申请</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getConsultants, applyConsultant } from '@/api/consultant'

const router = useRouter()
const loading = ref(false)
const consultants = ref([])
const applyDialogVisible = ref(false)
const applyLoading = ref(false)

const applyForm = ref({
  realName: '',
  expertiseList: [],
  introduction: '',
  certification: ''
})

const loadConsultants = async () => {
  loading.value = true
  try {
    const res = await getConsultants()
    if (res.code === 200) {
      consultants.value = res.data || []
    }
  } catch (e) {
    console.error('加载命理师失败', e)
  } finally {
    loading.value = false
  }
}

const parseExpertise = (expertise) => {
  if (!expertise) return []
  return expertise.split(',').map(s => s.trim()).filter(Boolean)
}

const viewDetail = (id) => {
  router.push(`/consultants/${id}`)
}

const consult = (consultant) => {
  ElMessage.info('咨询功能开发中，敬请期待...')
}

const showApplyDialog = () => {
  applyDialogVisible.value = true
}

const submitApply = async () => {
  if (!applyForm.value.realName) {
    ElMessage.warning('请输入真实姓名')
    return
  }
  if (applyForm.value.expertiseList.length === 0) {
    ElMessage.warning('请选择擅长领域')
    return
  }

  applyLoading.value = true
  try {
    const data = {
      realName: applyForm.value.realName,
      expertise: applyForm.value.expertiseList.join(','),
      introduction: applyForm.value.introduction,
      certification: applyForm.value.certification
    }
    const res = await applyConsultant(data)
    if (res.code === 200) {
      ElMessage.success('申请成功，等待审核')
      applyDialogVisible.value = false
      applyForm.value = { realName: '', expertiseList: [], introduction: '', certification: '' }
    } else {
      ElMessage.error(res.msg || '申请失败')
    }
  } catch (e) {
    ElMessage.error('申请失败')
  } finally {
    applyLoading.value = false
  }
}

onMounted(() => {
  loadConsultants()
})
</script>

<style scoped>
.consultant-list {
  padding: 0;
}

.page-header {
  text-align: center;
  margin-bottom: 32px;
}

.page-header h2 {
  color: #e8d5c4;
  font-size: 28px;
  margin: 0 0 8px;
}

.page-header p {
  color: #8a7a6a;
  font-size: 14px;
  margin: 0;
}

.consultants-container {
  min-height: 400px;
}

.consultant-card {
  margin-bottom: 24px;
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 12px;
  transition: all 0.3s;
}

.consultant-card:hover {
  transform: translateY(-4px);
  border-color: rgba(201, 168, 76, 0.3);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
}

.consultant-header {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
}

.consultant-avatar {
  background: linear-gradient(135deg, var(--gold-dark), var(--gold));
  color: #1a1a2e;
  font-size: 32px;
  font-weight: bold;
}

.consultant-info {
  flex: 1;
}

.consultant-name {
  color: #e8d5c4;
  font-size: 18px;
  margin: 0 0 8px;
}

.consultant-rating {
  margin-bottom: 4px;
}

.consultant-rating :deep(.el-rate) {
  --el-rate-icon-size: 16px;
}

.consultant-count {
  color: #8a7a6a;
  font-size: 13px;
}

.consultant-expertise {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 16px;
}

.expertise-tag {
  background: rgba(201, 168, 76, 0.1);
  border-color: rgba(201, 168, 76, 0.3);
  color: var(--text-gold);
}

.consultant-intro {
  color: #c4b5a0;
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 16px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.consultant-certification {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #c9a84c;
  font-size: 13px;
  margin-bottom: 16px;
  padding: 8px 12px;
  background: rgba(201, 168, 76, 0.1);
  border-radius: 6px;
}

.consultant-actions {
  display: flex;
  gap: 12px;
}

.consultant-actions .el-button {
  flex: 1;
}

.apply-section {
  margin-top: 32px;
}

.apply-card {
  background: linear-gradient(135deg, rgba(201, 168, 76, 0.1), rgba(26, 26, 46, 0.8));
  border: 1px solid rgba(201, 168, 76, 0.2);
  border-radius: 12px;
}

.apply-content {
  display: flex;
  align-items: center;
  gap: 24px;
}

.apply-icon {
  font-size: 48px;
}

.apply-text {
  flex: 1;
}

.apply-text h3 {
  color: #e8d5c4;
  font-size: 20px;
  margin: 0 0 8px;
}

.apply-text p {
  color: #8a7a6a;
  font-size: 14px;
  margin: 0;
}
</style>