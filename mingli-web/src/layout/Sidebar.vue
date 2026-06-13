<template>
  <div class="sidebar-wrapper">
    <!-- Logo 区域 -->
    <div class="sidebar-logo">
      <div class="logo-icon">☯</div>
      <div class="logo-text">
        <span class="logo-title">命理阁</span>
        <span class="logo-subtitle">MINGLI STUDIO</span>
      </div>
    </div>

    <el-divider class="sidebar-divider" />

    <!-- 菜单 -->
    <el-menu
      :default-active="activeMenu"
      :router="true"
      background-color="transparent"
      text-color="#a89080"
      active-text-color="#c9a84c"
      class="sidebar-menu"
    >
      <el-menu-item index="/dashboard">
        <el-icon><HomeFilled /></el-icon>
        <span>首页</span>
      </el-menu-item>
      <el-menu-item index="/bazi">
        <el-icon><MagicStick /></el-icon>
        <span>八字算命</span>
      </el-menu-item>
      <el-menu-item index="/fortune">
        <el-icon><Reading /></el-icon>
        <span>命理解读</span>
      </el-menu-item>
      <el-menu-item index="/daily">
        <el-icon><Sunny /></el-icon>
        <span>每日运势</span>
      </el-menu-item>
      <el-menu-item index="/divination">
        <el-icon><Coin /></el-icon>
        <span>占卜问卦</span>
      </el-menu-item>
      <el-menu-item index="/zodiac">
        <el-icon><Star /></el-icon>
        <span>十二星座</span>
      </el-menu-item>
      
      <el-divider class="sidebar-divider" />
      
      <el-menu-item index="/articles">
        <el-icon><ChatDotRound /></el-icon>
        <span>命理社区</span>
      </el-menu-item>
      <el-menu-item index="/consultants">
        <el-icon><UserFilled /></el-icon>
        <span>命理大师</span>
      </el-menu-item>
      
      <el-divider class="sidebar-divider" />
      
      <el-menu-item index="/profile">
        <el-icon><User /></el-icon>
        <span>个人中心</span>
      </el-menu-item>
    </el-menu>

    <!-- 底部装饰 -->
    <div class="sidebar-footer">
      <el-divider class="sidebar-divider" />
      <div class="sidebar-footer-text">
        <span>洞悉天机</span>
        <span>·</span>
        <span>指点迷津</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const activeMenu = computed(() => route.path)
</script>

<style scoped>
.sidebar-wrapper {
  height: 100vh;
  display: flex;
  flex-direction: column;
  padding: 0;
}

.sidebar-logo {
  display: flex;
  align-items: center;
  padding: 28px 20px 16px;
  gap: 12px;
}

.logo-icon {
  width: 44px;
  height: 44px;
  background: linear-gradient(135deg, var(--gold-dark), var(--gold));
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: #1a1a2e;
  flex-shrink: 0;
}

.logo-text {
  display: flex;
  flex-direction: column;
}

.logo-title {
  color: var(--text-gold);
  font-size: 20px;
  font-weight: 700;
  letter-spacing: 4px;
  line-height: 1.2;
}

.logo-subtitle {
  color: var(--text-muted);
  font-size: 10px;
  letter-spacing: 2px;
  margin-top: 2px;
}

.sidebar-divider {
  margin: 4px 16px !important;
  border-color: rgba(201, 168, 76, 0.1) !important;
}

.sidebar-menu {
  flex: 1;
  padding: 8px 0;
  border-right: none !important;
}

.sidebar-menu .el-menu-item {
  height: 48px;
  line-height: 48px;
  margin: 2px 8px;
  border-radius: 8px;
  font-size: 14px;
  letter-spacing: 1px;
}

.sidebar-menu .el-menu-item .el-icon {
  font-size: 18px;
  margin-right: 10px;
}

.sidebar-footer {
  padding: 16px 20px;
}

.sidebar-footer-text {
  display: flex;
  justify-content: center;
  gap: 8px;
  color: var(--text-muted);
  font-size: 11px;
  letter-spacing: 3px;
  padding: 8px 0;
}
</style>