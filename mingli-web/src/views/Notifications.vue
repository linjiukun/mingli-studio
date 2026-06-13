<template>
  <div class="notification-center">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="header-left">
        <h2>消息通知</h2>
        <el-tag v-if="unreadCount > 0" type="danger" effect="dark">{{ unreadCount }} 条未读</el-tag>
      </div>
      <el-button v-if="unreadCount > 0" @click="markAllRead">全部已读</el-button>
    </div>

    <!-- 通知类型筛选 -->
    <div class="filter-section">
      <el-radio-group v-model="currentType" @change="loadNotifications">
        <el-radio-button label="">全部</el-radio-button>
        <el-radio-button label="comment">评论</el-radio-button>
        <el-radio-button label="like">点赞</el-radio-button>
        <el-radio-button label="favorite">收藏</el-radio-button>
        <el-radio-button label="follow">关注</el-radio-button>
        <el-radio-button label="system">系统</el-radio-button>
      </el-radio-group>
    </div>

    <!-- 通知列表 -->
    <div class="notification-list" v-loading="loading">
      <div v-for="notification in notifications" :key="notification.id"
        class="notification-item" :class="{ unread: notification.isRead === '0' }"
        @click="handleClick(notification)">
        <div class="notification-avatar">
          <el-avatar :size="44" :src="notification.senderAvatar">
            {{ getNotificationIcon(notification.type) }}
          </el-avatar>
        </div>
        <div class="notification-content">
          <div class="notification-title">{{ notification.title }}</div>
          <div class="notification-desc">{{ notification.content }}</div>
          <div class="notification-time">{{ formatTime(notification.createTime) }}</div>
        </div>
        <div class="notification-badge" v-if="notification.isRead === '0'">
          <span class="badge-dot"></span>
        </div>
      </div>

      <el-empty v-if="!loading && notifications.length === 0" description="暂无通知" />
    </div>

    <!-- 分页 -->
    <div class="pagination-section" v-if="total > pageSize">
      <el-pagination v-model:current-page="currentPage" :page-size="pageSize" :total="total"
        layout="prev, pager, next" @current-change="loadNotifications" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getNotifications, getUnreadCount, markAsRead, markAllAsRead } from '@/api/notification'

const router = useRouter()
const loading = ref(false)
const notifications = ref([])
const unreadCount = ref(0)
const currentType = ref('')
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)

const loadNotifications = async () => {
  loading.value = true
  try {
    const res = await getNotifications({
      type: currentType.value || undefined,
      pageNum: currentPage.value,
      pageSize: pageSize.value
    })
    if (res.code === 200) {
      notifications.value = res.data?.list || res.data || []
      total.value = res.data?.total || 0
    }
  } catch (e) {
    console.error('加载通知失败', e)
  } finally {
    loading.value = false
  }
}

const loadUnreadCount = async () => {
  try {
    const res = await getUnreadCount()
    if (res.code === 200) {
      unreadCount.value = res.data || 0
    }
  } catch (e) {
    console.error('加载未读数失败', e)
  }
}

const handleClick = async (notification) => {
  // 标记为已读
  if (notification.isRead === '0') {
    try {
      await markAsRead(notification.id)
      notification.isRead = '1'
      unreadCount.value = Math.max(0, unreadCount.value - 1)
    } catch (e) {
      console.error('标记已读失败', e)
    }
  }

  // 跳转到相关页面
  if (notification.targetType === 'article' && notification.targetId) {
    router.push(`/articles/${notification.targetId}`)
  }
}

const markAllRead = async () => {
  try {
    const res = await markAllAsRead()
    if (res.code === 200) {
      ElMessage.success('已全部标记为已读')
      notifications.value.forEach(n => n.isRead = '1')
      unreadCount.value = 0
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const getNotificationIcon = (type) => {
  const icons = {
    comment: '💬',
    like: '👍',
    favorite: '⭐',
    follow: '👥',
    system: '🔔'
  }
  return icons[type] || '📢'
}

const formatTime = (timeStr) => {
  if (!timeStr) return ''
  const date = new Date(timeStr)
  const now = new Date()
  const diff = now - date

  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return `${Math.floor(diff / 60000)} 分钟前`
  if (diff < 86400000) return `${Math.floor(diff / 3600000)} 小时前`
  if (diff < 604800000) return `${Math.floor(diff / 86400000)} 天前`

  return date.toLocaleDateString('zh-CN')
}

onMounted(() => {
  loadNotifications()
  loadUnreadCount()
})
</script>

<style scoped>
.notification-center {
  padding: 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-left h2 {
  color: #e8d5c4;
  font-size: 24px;
  margin: 0;
}

.filter-section {
  margin-bottom: 24px;
}

.filter-section .el-radio-group {
  background: rgba(255, 255, 255, 0.04);
  border-radius: 8px;
  padding: 4px;
}

.notification-list {
  min-height: 400px;
}

.notification-item {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  padding: 16px 20px;
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 12px;
  margin-bottom: 12px;
  cursor: pointer;
  transition: all 0.3s;
}

.notification-item:hover {
  background: rgba(201, 168, 76, 0.08);
  border-color: rgba(201, 168, 76, 0.2);
}

.notification-item.unread {
  background: rgba(201, 168, 76, 0.1);
  border-color: rgba(201, 168, 76, 0.15);
}

.notification-avatar {
  flex-shrink: 0;
}

.notification-avatar .el-avatar {
  background: linear-gradient(135deg, var(--gold-dark), var(--gold));
  color: #1a1a2e;
  font-size: 20px;
}

.notification-content {
  flex: 1;
  min-width: 0;
}

.notification-title {
  color: #e8d5c4;
  font-size: 15px;
  font-weight: 600;
  margin-bottom: 4px;
}

.notification-desc {
  color: #8a7a6a;
  font-size: 13px;
  margin-bottom: 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.notification-time {
  color: #6a5a4a;
  font-size: 12px;
}

.notification-badge {
  flex-shrink: 0;
  display: flex;
  align-items: center;
}

.badge-dot {
  width: 10px;
  height: 10px;
  background: #e74c3c;
  border-radius: 50%;
}

.pagination-section {
  display: flex;
  justify-content: center;
  margin-top: 24px;
  padding: 16px 0;
}
</style>