<template>
  <div class="login-page">
    <!-- 水印装饰 -->
    <div class="mystic-watermark" style="top: 5%; left: 2%;">命</div>
    <div class="mystic-watermark" style="bottom: 10%; right: 3%;">理</div>
    <div class="mystic-watermark" style="top: 50%; left: 70%; opacity: 0.015;">☯</div>

    <!-- 背景装饰 -->
    <div class="bg-orb bg-orb-1"></div>
    <div class="bg-orb bg-orb-2"></div>

    <div class="login-container">
      <el-card class="login-card" shadow="never">
        <div class="login-header">
          <div class="login-logo">☯</div>
          <h1 class="login-title">命理阁</h1>
          <p class="login-subtitle">洞悉天机 · 指点迷津</p>
        </div>

        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          size="large"
          class="login-form"
          @keyup.enter="handleLogin"
        >
          <el-form-item prop="username">
            <el-input
              v-model="form.username"
              placeholder="用户名"
              :prefix-icon="User"
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="form.password"
              type="password"
              placeholder="密码"
              :prefix-icon="Lock"
              show-password
            />
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              class="login-btn"
              :loading="userStore.loading"
              @click="handleLogin"
            >
              登 录
            </el-button>
          </el-form-item>

          <div class="login-footer">
            <span class="login-footer-text">还没有账号？</span>
            <router-link to="/register" class="register-link">立即注册</router-link>
          </div>
        </el-form>

        <div class="decorative-line"></div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少 6 个字符', trigger: 'blur' }
  ]
}

async function handleLogin() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  const result = await userStore.login({
    username: form.username,
    password: form.password
  })

  if (result.success) {
    ElMessage.success('登录成功')
    router.push('/dashboard')
  } else {
    ElMessage.error(result.msg)
  }
}
</script>

<style scoped>
.login-page {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0a0a14 0%, #1a1a2e 50%, #0f0f1a 100%);
  position: relative;
  overflow: hidden;
}

.bg-orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  pointer-events: none;
}

.bg-orb-1 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(201, 168, 76, 0.08), transparent 70%);
  top: -100px;
  right: -100px;
}

.bg-orb-2 {
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, rgba(139, 0, 0, 0.06), transparent 70%);
  bottom: -50px;
  left: -50px;
}

.login-container {
  width: 420px;
  position: relative;
  z-index: 1;
}

.login-card {
  padding: 10px 0;
  background: rgba(26, 26, 46, 0.85) !important;
  border: 1px solid rgba(201, 168, 76, 0.2) !important;
  backdrop-filter: blur(20px);
}

.login-header {
  text-align: center;
  padding: 30px 0 10px;
}

.login-logo {
  font-size: 56px;
  margin-bottom: 12px;
  display: inline-block;
}

.login-title {
  color: var(--text-gold);
  font-size: 36px;
  font-weight: 700;
  letter-spacing: 8px;
  margin: 0;
  text-shadow: 0 0 30px rgba(201, 168, 76, 0.3);
}

.login-subtitle {
  color: var(--text-muted);
  font-size: 13px;
  letter-spacing: 6px;
  margin-top: 10px;
}

.login-form {
  padding: 20px 30px 0;
}

.login-form .el-form-item {
  margin-bottom: 22px;
}

.login-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  letter-spacing: 6px;
  margin-top: 6px;
}

.login-footer {
  text-align: center;
  padding: 0 0 8px;
}

.login-footer-text {
  color: var(--text-muted);
  font-size: 13px;
}

.register-link {
  color: var(--text-gold);
  font-size: 13px;
  text-decoration: none;
  margin-left: 4px;
  transition: color 0.3s;
}

.register-link:hover {
  color: var(--gold-light);
  text-decoration: underline;
}

.decorative-line {
  height: 1px;
  background: linear-gradient(
    to right,
    transparent,
    rgba(201, 168, 76, 0.3),
    transparent
  );
  margin: 10px 30px 0;
}
</style>
