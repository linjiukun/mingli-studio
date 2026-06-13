<template>
  <div class="user-profile-page">
    <!-- 用户信息卡片 -->
    <div class="profile-card">
      <div class="profile-header">
        <el-avatar :size="80" :src="userInfo.avatar" class="profile-avatar">
          {{ userInfo.nickname?.charAt(0) || '用' }}
        </el-avatar>
        <div class="profile-info">
          <h2 class="profile-name">{{ userInfo.nickname || userInfo.username }}</h2>
          <p class="profile-bio">{{ userInfo.bio || '这个人很懒，什么都没写...' }}</p>
          <div class="profile-meta">
            <span v-if="userInfo.birthDate">
              🎂 {{ userInfo.birthDate }}
            </span>
            <span v-if="userInfo.gender === '0'">♂ 男</span>
            <span v-else-if="userInfo.gender === '1'">♀ 女</span>
          </div>
        </div>
        <div class="profile-actions">
          <el-button v-if="!isSelf" :type="isFollowed ? 'default' : 'primary'" @click="toggleFollow">
            {{ isFollowed ? '已关注' : '关注' }}
          </el-button>
          <el-button v-if="isSelf" @click="goToSettings">编辑资料</el-button>
        </div>
      </div>

      <!-- 统计数据 -->
      <div class="profile-stats">
        <div class="stat-item" @click="activeTab = 'articles'">
          <div class="stat-value">{{ stats.articleCount || 0 }}</div>
          <div class="stat-label">文章</div>
        </div>
        <div class="stat-item" @click="activeTab = 'followers'">
          <div class="stat-value">{{ stats.followerCount || 0 }}</div>
          <div class="stat-label">粉丝</div>
        </div>
        <div class="stat-item" @click="activeTab = 'following'">
          <div class="stat-value">{{ stats.followingCount || 0 }}</div>
          <div class="stat-label">关注</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ stats.likeReceived || 0 }}</div>
          <div class="stat-label">获赞</div>
        </div>
      </div>
    </div>

    <!-- 内容标签页 -->
    <div class="profile-tabs">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="文章" name="articles">
          <div class="articles-list">
            <div v-for="article in articles" :key="article.id" class="article-item" @click="goToArticle(article.id)">
              <div class="article-info">
                <h3 class="article-title">{{ article.title }}</h3>
                <p class="article-summary">{{ article.summary }}</p>
                <div class="article-meta">
                  <span>{{ formatDate(article.createTime) }}</span>
                  <span>👁 {{ article.viewCount || 0 }}</span>
                  <span>💬 {{ article.commentCount || 0 }}</span>
                  <span>⭐ {{ article.favoriteCount || 0 }}</span>
                </div>
              </div>
            </div>
            <el-empty v-if="articles.length === 0" description="暂无文章" />
          </div>
        </el-tab-pane>

        <el-tab-pane label="收藏" name="favorites" v-if="isSelf">
          <div class="favorites-list">
            <div v-for="fav in favorites" :key="fav.id" class="favorite-item" @click="goToArticle(fav.articleId)">
              <div class="fav-info">
                <h4>{{ fav.articleTitle }}</h4>
                <p>{{ fav.articleSummary }}</p>
              </div>
            </div>
            <el-empty v-if="favorites.length === 0" description="暂无收藏" />
          </div>
        </el-tab-pane>

        <el-tab-pane label="粉丝" name="followers">
          <div class="users-list">
            <div v-for="user in followers" :key="user.id" class="user-item">
              <el-avatar :size="48" :src="user.followUserAvatar" @click="goToUser(user.userId)">
                {{ user.followUserName?.charAt(0) || '用' }}
              </el-avatar>
              <div class="user-info">
                <span class="user-name">{{ user.followUserName || '匿名用户' }}</span>
              </div>
            </div>
            <el-empty v-if="followers.length === 0" description="暂无粉丝" />
          </div>
        </el-tab-pane>

        <el-tab-pane label="关注" name="following">
          <div class="users-list">
            <div v-for="user in following" :key="user.id" class="user-item">
              <el-avatar :size="48" :src="user.followUserAvatar" @click="goToUser(user.followUserId)">
                {{ user.followUserName?.charAt(0) || '用' }}
              </el-avatar>
              <div class="user-info">
                <span class="user-name">{{ user.followUserName || '匿名用户' }}</span>
              </div>
            </div>
            <el-empty v-if="following.length === 0" description="暂无关注" />
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getArticles } from '@/api/community'
import { getFavorites } from '@/api/community'
import { followUser, unfollowUser, getFollowing, getFollowers, getFollowCounts } from '@/api/social'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const userInfo = ref({})
const stats = ref({})
const articles = ref([])
const favorites = ref([])
const followers = ref([])
const following = ref([])
const activeTab = ref('articles')
const isFollowed = ref(false)

const isSelf = computed(() => {
  const userId = route.params.id || userStore.userInfo?.id
  return userId === userStore.userInfo?.id
})

const loadUserProfile = async () => {
  const userId = route.params.id || userStore.userInfo?.id
  if (!userId) return

  // 加载用户信息
  userInfo.value = isSelf.value ? userStore.userInfo : { id: userId }

  // 加载统计数据
  try {
    const res = await getFollowCounts(userId)
    if (res.code === 200) {
      stats.value = res.data || {}
    }
  } catch (e) {
    console.error('加载统计失败', e)
  }
}

const loadArticles = async () => {
  const userId = route.params.id || userStore.userInfo?.id
  try {
    const res = await getArticles({ authorId: userId })
    if (res.code === 200) {
      articles.value = res.data || []
    }
  } catch (e) {
    console.error('加载文章失败', e)
  }
}

const loadFavorites = async () => {
  if (!isSelf.value) return
  try {
    const res = await getFavorites()
    if (res.code === 200) {
      favorites.value = res.data || []
    }
  } catch (e) {
    console.error('加载收藏失败', e)
  }
}

const loadFollowers = async () => {
  const userId = route.params.id || userStore.userInfo?.id
  try {
    const res = await getFollowers(userId)
    if (res.code === 200) {
      followers.value = res.data || []
    }
  } catch (e) {
    console.error('加载粉丝失败', e)
  }
}

const loadFollowing = async () => {
  const userId = route.params.id || userStore.userInfo?.id
  try {
    const res = await getFollowing(userId)
    if (res.code === 200) {
      following.value = res.data || []
    }
  } catch (e) {
    console.error('加载关注失败', e)
  }
}

const toggleFollow = async () => {
  const userId = route.params.id
  try {
    if (isFollowed.value) {
      await unfollowUser(userId)
      isFollowed.value = false
      stats.value.followerCount = Math.max(0, (stats.value.followerCount || 1) - 1)
      ElMessage.success('已取消关注')
    } else {
      await followUser(userId)
      isFollowed.value = true
      stats.value.followerCount = (stats.value.followerCount || 0) + 1
      ElMessage.success('关注成功')
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const handleTabClick = () => {
  if (activeTab.value === 'articles') loadArticles()
  else if (activeTab.value === 'favorites') loadFavorites()
  else if (activeTab.value === 'followers') loadFollowers()
  else if (activeTab.value === 'following') loadFollowing()
}

const goToArticle = (id) => router.push(`/articles/${id}`)
const goToUser = (id) => router.push(`/user/${id}`)
const goToSettings = () => router.push('/profile')

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleDateString('zh-CN')
}

onMounted(() => {
  loadUserProfile()
  loadArticles()
})
</script>

<style scoped>
.user-profile-page {
  padding: 0;
}

.profile-card {
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 16px;
  padding: 32px;
  margin-bottom: 24px;
}

.profile-header {
  display: flex;
  gap: 24px;
  margin-bottom: 24px;
}

.profile-avatar {
  background: linear-gradient(135deg, var(--gold-dark), var(--gold));
  color: #1a1a2e;
  font-size: 32px;
  font-weight: bold;
  flex-shrink: 0;
}

.profile-info {
  flex: 1;
}

.profile-name {
  color: #e8d5c4;
  font-size: 24px;
  margin: 0 0 8px;
}

.profile-bio {
  color: #8a7a6a;
  font-size: 14px;
  margin: 0 0 8px;
}

.profile-meta {
  display: flex;
  gap: 16px;
  color: #6a5a4a;
  font-size: 13px;
}

.profile-actions {
  flex-shrink: 0;
}

.profile-stats {
  display: flex;
  justify-content: center;
  gap: 48px;
  padding-top: 24px;
  border-top: 1px solid rgba(255, 255, 255, 0.06);
}

.stat-item {
  text-align: center;
  cursor: pointer;
}

.stat-value {
  color: #e8d5c4;
  font-size: 24px;
  font-weight: bold;
}

.stat-label {
  color: #8a7a6a;
  font-size: 13px;
  margin-top: 4px;
}

.profile-tabs {
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 16px;
  padding: 24px;
}

.article-item, .favorite-item {
  padding: 16px;
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 8px;
  margin-bottom: 12px;
  cursor: pointer;
  transition: all 0.3s;
}

.article-item:hover, .favorite-item:hover {
  background: rgba(201, 168, 76, 0.08);
  border-color: rgba(201, 168, 76, 0.2);
}

.article-title, .fav-info h4 {
  color: #e8d5c4;
  font-size: 16px;
  margin: 0 0 8px;
}

.article-summary, .fav-info p {
  color: #8a7a6a;
  font-size: 13px;
  margin: 0 0 8px;
}

.article-meta {
  display: flex;
  gap: 16px;
  color: #6a5a4a;
  font-size: 12px;
}

.users-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.user-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px;
  background: rgba(255, 255, 255, 0.04);
  border-radius: 8px;
}

.user-item .el-avatar {
  cursor: pointer;
}

.user-name {
  color: #e8d5c4;
  font-size: 14px;
}
</style>