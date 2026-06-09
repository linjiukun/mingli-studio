<template>
  <el-container class="layout-container">
    <!-- 水印背景 -->
    <div class="mystic-watermark" style="top: 10%; left: -5%;">命</div>
    <div class="mystic-watermark" style="bottom: 5%; right: -5%;">理</div>
    <div class="mystic-watermark" style="top: 40%; left: 60%; opacity: 0.02;">☯</div>

    <el-aside width="220px" class="layout-aside">
      <Sidebar />
    </el-aside>
    <el-container>
      <el-header class="layout-header">
        <Navbar />
      </el-header>
      <el-main class="layout-main">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import Sidebar from './Sidebar.vue'
import Navbar from './Navbar.vue'
</script>

<style scoped>
.layout-container {
  height: 100vh;
  overflow: hidden;
  position: relative;
}

.layout-aside {
  background: linear-gradient(180deg, #0f0f1a 0%, #1a1a2e 100%);
  border-right: 1px solid rgba(201, 168, 76, 0.15);
  overflow-y: auto;
  overflow-x: hidden;
  height: 100vh;
  position: relative;
  z-index: 10;
}

.layout-header {
  height: 60px !important;
  padding: 0 !important;
  background: linear-gradient(90deg, #0f0f1a 0%, #1a1a2e 100%);
  border-bottom: 1px solid rgba(201, 168, 76, 0.15);
  position: relative;
  z-index: 9;
  display: flex;
  align-items: center;
}

.layout-main {
  background: var(--bg-primary);
  padding: 24px;
  overflow-y: auto;
  overflow-x: hidden;
  height: calc(100vh - 60px);
  position: relative;
  z-index: 1;
}
</style>
