<template>
  <div class="register-container">
    <div class="register-card">
      <h1 class="title">命理阁</h1>
      <p class="subtitle">注册 · 开启你的命理之旅</p>
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="form.password" show-password placeholder="至少6位" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input type="password" v-model="form.confirmPassword" show-password placeholder="再次输入密码" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="给自己取个好听的名字" />
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="出生日期" prop="birthDate">
              <el-date-picker v-model="form.birthDate" type="date" placeholder="选择日期" style="width:100%" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="form.gender" placeholder="选择性别" style="width:100%">
                <el-option label="男" value="0" />
                <el-option label="女" value="1" />
                <el-option label="保密" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="出生时辰" prop="birthHour">
          <el-select v-model="form.birthHour" placeholder="选择时辰（选填）" style="width:100%">
            <el-option v-for="h in hours" :key="h.value" :label="h.label" :value="h.value" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="register-btn" @click="handleRegister" :loading="loading">注册</el-button>
        </el-form-item>
      </el-form>
      <p class="login-link">已有账号？<router-link to="/login">立即登录</router-link></p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '@/api/auth'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: '',
  birthDate: '',
  gender: '2',
  birthHour: ''
})

const hours = [
  { value: '子', label: '子时 (23:00-00:59)' },
  { value: '丑', label: '丑时 (01:00-02:59)' },
  { value: '寅', label: '寅时 (03:00-04:59)' },
  { value: '卯', label: '卯时 (05:00-06:59)' },
  { value: '辰', label: '辰时 (07:00-08:59)' },
  { value: '巳', label: '巳时 (09:00-10:59)' },
  { value: '午', label: '午时 (11:00-12:59)' },
  { value: '未', label: '未时 (13:00-14:59)' },
  { value: '申', label: '申时 (15:00-16:59)' },
  { value: '酉', label: '酉时 (17:00-18:59)' },
  { value: '戌', label: '戌时 (19:00-20:59)' },
  { value: '亥', label: '亥时 (21:00-22:59)' }
]

const validatePass2 = (rule, value, callback) => {
  if (value !== form.password) callback(new Error('两次密码不一致'))
  else callback()
}

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, min: 6, message: '密码至少6位', trigger: 'blur' }],
  confirmPassword: [{ required: true, validator: validatePass2, trigger: 'blur' }],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }]
}

const handleRegister = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    const res = await register(form)
    if (res.code === 200) {
      ElMessage.success('注册成功！请登录')
      router.push('/login')
    } else {
      ElMessage.error(res.msg || '注册失败')
    }
  } catch (e) {
    ElMessage.error('网络错误')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0a0a1a 0%, #1a1a3e 50%, #0d0d1a 100%);
}
.register-card {
  width: 480px;
  padding: 40px;
  background: rgba(255,255,255,0.06);
  backdrop-filter: blur(16px);
  border-radius: 16px;
  border: 1px solid rgba(201,168,76,0.2);
}
.title {
  text-align: center;
  font-size: 36px;
  color: #c9a84c;
  margin-bottom: 4px;
  font-family: 'STKaiti', 'KaiTi', serif;
}
.subtitle {
  text-align: center;
  color: #8a7a6a;
  margin-bottom: 32px;
  font-size: 14px;
}
:deep(.el-form-item__label) { color: #c4b5a0; }
:deep(.el-input__wrapper) {
  background: rgba(255,255,255,0.08);
  border: 1px solid rgba(201,168,76,0.15);
  box-shadow: none;
}
:deep(.el-input__inner) { color: #e8d5c4; }
:deep(.el-input__inner::placeholder) { color: #6a5a4a; }
.register-btn {
  width: 100%;
  background: linear-gradient(135deg, #8b6914, #c9a84c);
  border: none;
  height: 44px;
  font-size: 16px;
}
.login-link {
  text-align: center;
  color: #8a7a6a;
  margin-top: 16px;
  font-size: 13px;
}
.login-link a { color: #c9a84c; text-decoration: none; }
</style>
