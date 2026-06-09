<template>
  <div class="profile-page">
    <el-card class="section" shadow="never">
      <template #header><span>👤 个人信息</span></template>
      <el-form :model="form" label-position="left" label-width="100px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" disabled />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="form.realName" />
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="出生日期">
              <el-date-picker v-model="form.birthDate" type="date" placeholder="选择日期" style="width:100%" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别">
              <el-select v-model="form.gender" style="width:100%">
                <el-option label="男" value="0" />
                <el-option label="女" value="1" />
                <el-option label="保密" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="出生时辰">
          <el-select v-model="form.birthHour" style="width:100%">
            <el-option label="未填写" value="" />
            <el-option v-for="h in hours" :key="h.value" :label="h.label" :value="h.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSave" :loading="saving" class="save-btn">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getUserProfile, updateUserProfile } from '@/api/auth'

const userStore = useUserStore()
const saving = ref(false)

const form = reactive({
  username: '',
  nickname: '',
  realName: '',
  birthDate: '',
  gender: '2',
  birthHour: '',
  email: ''
})

const hours = [
  { value: '子', label: '子时' }, { value: '丑', label: '丑时' },
  { value: '寅', label: '寅时' }, { value: '卯', label: '卯时' },
  { value: '辰', label: '辰时' }, { value: '巳', label: '巳时' },
  { value: '午', label: '午时' }, { value: '未', label: '未时' },
  { value: '申', label: '申时' }, { value: '酉', label: '酉时' },
  { value: '戌', label: '戌时' }, { value: '亥', label: '亥时' }
]

const loadProfile = async () => {
    try {
      const res = await getUserProfile()
      if (res.code === 200) {
        const u = res.data
        Object.assign(form, {
          username: u.username || '',
          nickname: u.nickname || '',
          realName: u.realName || '',
          birthDate: u.birthDate || '',
          gender: u.gender || '2',
          birthHour: u.birthHour || '',
          email: u.email || ''
        })
      }
    } catch { /* ignore */ }
  }

  const handleSave = async () => {
    saving.value = true
    try {
      const res = await updateUserProfile(form)
      if (res.code === 200) {
        ElMessage.success('保存成功')
        userStore.loadUser()
      } else ElMessage.error(res.msg)
    } catch { ElMessage.error('保存失败') }
    finally { saving.value = false }
  }

onMounted(() => loadProfile())
</script>

<style scoped>
.section {
  background: rgba(255,255,255,0.04);
  border: 1px solid rgba(255,255,255,0.06);
}
:deep(.el-form-item__label) { color: #c4b5a0; }
:deep(.el-input__wrapper) {
  background: rgba(255,255,255,0.06);
  border: 1px solid rgba(201,168,76,0.12);
  box-shadow: none;
}
:deep(.el-input__inner) { color: #e8d5c4; }
:deep(.el-input.is-disabled .el-input__wrapper) { background: rgba(255,255,255,0.02); }
:deep(.el-input.is-disabled .el-input__inner) { color: #6a5a4a; }
.save-btn { width: 200px; background: linear-gradient(135deg, #8b6914, #c9a84c); border: none; }
</style>
