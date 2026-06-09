<template>
  <div class="navbar">
    <div class="navbar-left">
      <div class="breadcrumb">
        <span class="breadcrumb-icon">✦</span>
        <span class="breadcrumb-text">{{ route.meta.title || '首页' }}</span>
      </div>
    </div>

    <div class="navbar-right">
      <el-dropdown trigger="click" @command="handleCommand">
        <span class="user-dropdown">
          <el-avatar :size="32" class="user-avatar">
            {{ userStore.username?.charAt(0)?.toUpperCase() || '命' }}
          </el-avatar>
          <span class="username">{{ userStore.username || '用户' }}</span>
          <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">
              <el-icon><User /></el-icon>
              个人中心
            </el-dropdown-item>
            <el-dropdown-item command="logout" divided>
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

function handleCommand(command) {
  if (command === 'profile') {
    router.push('/profile')
  } else if (command === 'logout') {
    ElMessageBox.confirm('确定要退出登录吗？', '退出确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      userStore.logout()
      router.push('/login')
    }).catch(() => {})
  }
}
</script>

<style scoped>
.navbar {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  height: 100%;
}

.navbar-left {
  display: flex;
  align-items: center;
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: 8px;
}

.breadcrumb-icon {
  color: var(--text-gold);
  font-size: 14px;
}

.breadcrumb-text {
  color: var(--text-primary);
  font-size: 15px;
  font-weight: 500;
  letter-spacing: 1px;
}

.navbar-right {
  display: flex;
  align-items: center;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 12px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.user-dropdown:hover {
  background: rgba(201, 168, 76, 0.1);
}

.user-avatar {
  background: linear-gradient(135deg, var(--gold-dark), var(--gold));
  color: #1a1a2e;
  font-weight: 700;
  font-size: 14px;
}

.username {
  color: var(--text-primary);
  font-size: 14px;
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.dropdown-icon {
  color: var(--text-muted);
  font-size: 12px;
}
</style>
