<template>
  <div class="article-list">
    <!-- 搜索和筛选区域 -->
    <div class="search-section">
      <el-row :gutter="16" align="middle">
        <el-col :span="8">
          <el-input v-model="keyword" placeholder="搜索文章标题..." clearable @keyup.enter="loadArticles">
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </el-col>
        <el-col :span="8">
          <el-select v-model="selectedCategory" placeholder="选择分类" clearable @change="loadArticles">
            <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
          </el-select>
        </el-col>
        <el-col :span="8">
          <el-button type="primary" @click="loadArticles">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-col>
      </el-row>
    </div>

    <!-- 分类标签 -->
    <div class="category-tags">
      <el-tag v-for="cat in categories" :key="cat.id" :type="selectedCategory === cat.id ? '' : 'info'"
        @click="selectCategory(cat.id)" class="category-tag" effect="dark">
        {{ cat.name }}
      </el-tag>
    </div>

    <!-- 文章列表 -->
    <div class="articles-container" v-loading="loading">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" v-for="article in articles" :key="article.id">
          <el-card class="article-card" shadow="hover" @click="goToArticle(article.id)">
            <div class="article-cover" v-if="article.coverImage">
              <img :src="article.coverImage" :alt="article.title" />
            </div>
            <div class="article-cover placeholder" v-else>
              <span class="cover-icon">📖</span>
            </div>
            <div class="article-content">
              <div class="article-category">
                <el-tag size="small" effect="plain">{{ article.categoryName || '未分类' }}</el-tag>
              </div>
              <h3 class="article-title">{{ article.title }}</h3>
              <p class="article-summary">{{ article.summary || '暂无摘要' }}</p>
              <div class="article-meta">
                <span class="meta-item">
                  <el-icon><User /></el-icon>
                  {{ article.authorName || '匿名' }}
                </span>
                <span class="meta-item">
                  <el-icon><View /></el-icon>
                  {{ article.viewCount || 0 }}
                </span>
                <span class="meta-item">
                  <el-icon><ChatDotRound /></el-icon>
                  {{ article.commentCount || 0 }}
                </span>
                <span class="meta-item">
                  <el-icon><Star /></el-icon>
                  {{ article.favoriteCount || 0 }}
                </span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 空状态 -->
      <el-empty v-if="!loading && articles.length === 0" description="暂无文章" />
    </div>

    <!-- 分页 -->
    <div class="pagination-section" v-if="total > pageSize">
      <el-pagination v-model:current-page="currentPage" :page-size="pageSize" :total="total"
        layout="prev, pager, next" @current-change="loadArticles" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getArticles, getCategories } from '@/api/community'

const router = useRouter()
const loading = ref(false)
const articles = ref([])
const categories = ref([])
const keyword = ref('')
const selectedCategory = ref('')
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)

const loadArticles = async () => {
  loading.value = true
  try {
    const res = await getArticles({
      categoryId: selectedCategory.value || undefined,
      keyword: keyword.value || undefined,
      pageNum: currentPage.value,
      pageSize: pageSize.value
    })
    if (res.code === 200) {
      articles.value = res.data || []
      total.value = res.data?.length || 0
    }
  } catch (e) {
    console.error('加载文章失败', e)
  } finally {
    loading.value = false
  }
}

const loadCategories = async () => {
  try {
    const res = await getCategories()
    if (res.code === 200) {
      categories.value = res.data || []
    }
  } catch (e) {
    console.error('加载分类失败', e)
  }
}

const selectCategory = (id) => {
  selectedCategory.value = selectedCategory.value === id ? '' : id
  currentPage.value = 1
  loadArticles()
}

const resetSearch = () => {
  keyword.value = ''
  selectedCategory.value = ''
  currentPage.value = 1
  loadArticles()
}

const goToArticle = (id) => {
  router.push(`/articles/${id}`)
}

onMounted(() => {
  loadCategories()
  loadArticles()
})
</script>

<style scoped>
.article-list {
  padding: 0;
}

.search-section {
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
}

.category-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 24px;
}

.category-tag {
  cursor: pointer;
  transition: all 0.3s;
  border-color: rgba(201, 168, 76, 0.3);
  background: rgba(201, 168, 76, 0.1);
  color: var(--text-gold);
}

.category-tag:hover {
  background: rgba(201, 168, 76, 0.2);
  border-color: var(--gold);
}

.articles-container {
  min-height: 400px;
}

.article-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: all 0.3s;
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 12px;
  overflow: hidden;
}

.article-card:hover {
  transform: translateY(-4px);
  border-color: rgba(201, 168, 76, 0.3);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
}

.article-cover {
  height: 160px;
  overflow: hidden;
}

.article-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.article-cover.placeholder {
  background: linear-gradient(135deg, rgba(201, 168, 76, 0.1), rgba(26, 26, 46, 0.8));
  display: flex;
  align-items: center;
  justify-content: center;
}

.cover-icon {
  font-size: 48px;
  opacity: 0.5;
}

.article-content {
  padding: 16px;
}

.article-category {
  margin-bottom: 8px;
}

.article-category .el-tag {
  background: rgba(201, 168, 76, 0.15);
  border-color: rgba(201, 168, 76, 0.3);
  color: var(--text-gold);
  font-size: 11px;
}

.article-title {
  color: #e8d5c4;
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 8px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-summary {
  color: #8a7a6a;
  font-size: 13px;
  margin: 0 0 12px;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-meta {
  display: flex;
  gap: 16px;
  color: #6a5a4a;
  font-size: 12px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.pagination-section {
  display: flex;
  justify-content: center;
  margin-top: 24px;
  padding: 16px 0;
}
</style>