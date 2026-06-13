<template>
  <div class="article-detail" v-loading="loading">
    <div v-if="article" class="article-container">
      <!-- 返回按钮 -->
      <div class="back-button">
        <el-button @click="goBack" :icon="ArrowLeft">返回列表</el-button>
      </div>

      <!-- 文章头部 -->
      <div class="article-header">
        <div class="article-category">
          <el-tag effect="dark">{{ article.categoryName || '未分类' }}</el-tag>
        </div>
        <h1 class="article-title">{{ article.title }}</h1>
        <div class="article-meta">
          <span class="meta-item">
            <el-icon><User /></el-icon>
            {{ article.authorName || '匿名' }}
          </span>
          <span class="meta-item">
            <el-icon><Clock /></el-icon>
            {{ formatDate(article.createTime) }}
          </span>
          <span class="meta-item">
            <el-icon><View /></el-icon>
            {{ article.viewCount || 0 }} 阅读
          </span>
        </div>
      </div>

      <!-- 文章内容 -->
      <div class="article-body">
        <div class="content-text" v-html="article.content"></div>
      </div>

      <!-- 文章操作 -->
      <div class="article-actions">
        <el-button :type="isFavorited ? 'warning' : 'default'" @click="toggleFavorite">
          <el-icon><Star /></el-icon>
          {{ isFavorited ? '已收藏' : '收藏' }} ({{ article.favoriteCount || 0 }})
        </el-button>
        <el-button @click="scrollToComments">
          <el-icon><ChatDotRound /></el-icon>
          评论 ({{ article.commentCount || 0 }})
        </el-button>
      </div>

      <!-- 评论区域 -->
      <div class="comments-section" id="comments">
        <div class="section-title">
          <h3>评论区</h3>
        </div>

        <!-- 发表评论 -->
        <div class="comment-input">
          <el-input v-model="commentContent" type="textarea" :rows="3" placeholder="写下你的评论..." />
          <el-button type="primary" @click="submitComment" :loading="commentLoading" style="margin-top: 10px;">
            发表评论
          </el-button>
        </div>

        <!-- 评论列表 -->
        <div class="comments-list">
          <div v-for="comment in comments" :key="comment.id" class="comment-item">
            <div class="comment-avatar">
              <el-avatar :size="40" :src="comment.userAvatar">
                {{ comment.userName?.charAt(0) || '用' }}
              </el-avatar>
            </div>
            <div class="comment-content">
              <div class="comment-header">
                <span class="comment-author">{{ comment.userName || '匿名用户' }}</span>
                <span class="comment-time">{{ formatDate(comment.createTime) }}</span>
              </div>
              <div class="comment-text">{{ comment.content }}</div>
              <div class="comment-actions">
                <el-button text size="small" @click="likeComment(comment)">
                  <el-icon><Pointer /></el-icon>
                  {{ comment.likeCount || 0 }}
                </el-button>
                <el-button text size="small" @click="replyTo(comment)">
                  回复
                </el-button>
              </div>

              <!-- 子评论 -->
              <div v-if="comment.replies && comment.replies.length > 0" class="replies-list">
                <div v-for="reply in comment.replies" :key="reply.id" class="reply-item">
                  <div class="reply-header">
                    <span class="reply-author">{{ reply.userName || '匿名用户' }}</span>
                    <span class="reply-time">{{ formatDate(reply.createTime) }}</span>
                  </div>
                  <div class="reply-text">{{ reply.content }}</div>
                </div>
              </div>
            </div>
          </div>

          <el-empty v-if="comments.length === 0" description="暂无评论，快来发表第一条评论吧！" />
        </div>
      </div>
    </div>

    <!-- 文章不存在 -->
    <el-empty v-else-if="!loading" description="文章不存在或已被删除">
      <el-button type="primary" @click="goBack">返回列表</el-button>
    </el-empty>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getArticle, favoriteArticle, unfavoriteArticle, getComments, createComment, likeComment as likeCommentApi } from '@/api/community'

const router = useRouter()
const route = useRoute()
const loading = ref(false)
const article = ref(null)
const comments = ref([])
const commentContent = ref('')
const commentLoading = ref(false)
const isFavorited = ref(false)

const loadArticle = async () => {
  const id = route.params.id
  if (!id) return

  loading.value = true
  try {
    const res = await getArticle(id)
    if (res.code === 200) {
      article.value = res.data
      isFavorited.value = res.data.favoriteCount > 0
    } else {
      ElMessage.error(res.msg || '加载失败')
    }
  } catch (e) {
    console.error('加载文章失败', e)
    ElMessage.error('加载文章失败')
  } finally {
    loading.value = false
  }
}

const loadComments = async () => {
  const id = route.params.id
  if (!id) return

  try {
    const res = await getComments(id)
    if (res.code === 200) {
      comments.value = res.data || []
    }
  } catch (e) {
    console.error('加载评论失败', e)
  }
}

const toggleFavorite = async () => {
  if (!article.value) return

  try {
    if (isFavorited.value) {
      const res = await unfavoriteArticle(article.value.id)
      if (res.code === 200) {
        isFavorited.value = false
        article.value.favoriteCount = Math.max(0, (article.value.favoriteCount || 1) - 1)
        ElMessage.success('已取消收藏')
      }
    } else {
      const res = await favoriteArticle(article.value.id)
      if (res.code === 200) {
        isFavorited.value = true
        article.value.favoriteCount = (article.value.favoriteCount || 0) + 1
        ElMessage.success('收藏成功')
      }
    }
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const submitComment = async () => {
  if (!commentContent.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }

  commentLoading.value = true
  try {
    const res = await createComment(article.value.id, { content: commentContent.value })
    if (res.code === 200) {
      ElMessage.success('评论成功')
      commentContent.value = ''
      loadComments()
      article.value.commentCount = (article.value.commentCount || 0) + 1
    } else {
      ElMessage.error(res.msg || '评论失败')
    }
  } catch (e) {
    ElMessage.error('评论失败')
  } finally {
    commentLoading.value = false
  }
}

const likeComment = async (comment) => {
  try {
    const res = await likeCommentApi(comment.id)
    if (res.code === 200) {
      comment.likeCount = (comment.likeCount || 0) + 1
    }
  } catch (e) {
    console.error('点赞失败', e)
  }
}

const replyTo = (comment) => {
  commentContent.value = `@${comment.userName} `
}

const scrollToComments = () => {
  document.getElementById('comments')?.scrollIntoView({ behavior: 'smooth' })
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric' })
}

const goBack = () => {
  router.push('/articles')
}

onMounted(() => {
  loadArticle()
  loadComments()
})
</script>

<style scoped>
.article-detail {
  padding: 0;
}

.back-button {
  margin-bottom: 20px;
}

.article-container {
  max-width: 900px;
  margin: 0 auto;
}

.article-header {
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 12px;
  padding: 32px;
  margin-bottom: 24px;
}

.article-category {
  margin-bottom: 16px;
}

.article-category .el-tag {
  background: rgba(201, 168, 76, 0.15);
  border-color: rgba(201, 168, 76, 0.3);
  color: var(--text-gold);
}

.article-title {
  color: #e8d5c4;
  font-size: 28px;
  font-weight: 700;
  margin: 0 0 16px;
  line-height: 1.4;
}

.article-meta {
  display: flex;
  gap: 24px;
  color: #8a7a6a;
  font-size: 14px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

.article-body {
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 12px;
  padding: 32px;
  margin-bottom: 24px;
}

.content-text {
  color: #c4b5a0;
  font-size: 16px;
  line-height: 1.8;
}

.content-text :deep(p) {
  margin-bottom: 16px;
}

.content-text :deep(h2),
.content-text :deep(h3) {
  color: #e8d5c4;
  margin: 24px 0 12px;
}

.article-actions {
  display: flex;
  gap: 16px;
  margin-bottom: 32px;
}

.article-actions .el-button {
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.1);
  color: #c4b5a0;
}

.article-actions .el-button:hover {
  border-color: rgba(201, 168, 76, 0.3);
  color: var(--text-gold);
}

.comments-section {
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 12px;
  padding: 32px;
}

.section-title h3 {
  color: #e8d5c4;
  font-size: 18px;
  margin: 0 0 24px;
  padding-bottom: 12px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
}

.comment-input {
  margin-bottom: 32px;
}

.comment-input .el-textarea :deep(textarea) {
  background: rgba(255, 255, 255, 0.06);
  border-color: rgba(255, 255, 255, 0.1);
  color: #c4b5a0;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.comment-item {
  display: flex;
  gap: 16px;
}

.comment-avatar {
  flex-shrink: 0;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.comment-author {
  color: #e8d5c4;
  font-weight: 600;
  font-size: 14px;
}

.comment-time {
  color: #6a5a4a;
  font-size: 12px;
}

.comment-text {
  color: #c4b5a0;
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 8px;
}

.comment-actions {
  display: flex;
  gap: 8px;
}

.comment-actions .el-button {
  color: #6a5a4a;
}

.comment-actions .el-button:hover {
  color: var(--text-gold);
}

.replies-list {
  margin-top: 16px;
  padding-left: 20px;
  border-left: 2px solid rgba(201, 168, 76, 0.2);
}

.reply-item {
  padding: 12px 0;
}

.reply-item:not(:last-child) {
  border-bottom: 1px solid rgba(255, 255, 255, 0.04);
}

.reply-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 6px;
}

.reply-author {
  color: #e8d5c4;
  font-size: 13px;
  font-weight: 600;
}

.reply-time {
  color: #6a5a4a;
  font-size: 11px;
}

.reply-text {
  color: #c4b5a0;
  font-size: 13px;
  line-height: 1.6;
}
</style>