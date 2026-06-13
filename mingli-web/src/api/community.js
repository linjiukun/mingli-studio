import request from './request'

// ==================== 文章相关 ====================

// 获取文章列表
export function getArticles(params) {
  return request({
    url: '/community/articles',
    method: 'get',
    params
  })
}

// 获取文章详情
export function getArticle(id) {
  return request({
    url: `/community/articles/${id}`,
    method: 'get'
  })
}

// 发布文章
export function createArticle(data) {
  return request({
    url: '/community/articles',
    method: 'post',
    data
  })
}

// 修改文章
export function updateArticle(id, data) {
  return request({
    url: `/community/articles/${id}`,
    method: 'put',
    data
  })
}

// 删除文章
export function deleteArticle(id) {
  return request({
    url: `/community/articles/${id}`,
    method: 'delete'
  })
}

// ==================== 收藏相关 ====================

// 收藏文章
export function favoriteArticle(id) {
  return request({
    url: `/community/articles/${id}/favorite`,
    method: 'post'
  })
}

// 取消收藏
export function unfavoriteArticle(id) {
  return request({
    url: `/community/articles/${id}/favorite`,
    method: 'delete'
  })
}

// 获取收藏列表
export function getFavorites() {
  return request({
    url: '/community/favorites',
    method: 'get'
  })
}

// ==================== 评论相关 ====================

// 获取文章评论
export function getComments(articleId) {
  return request({
    url: `/community/articles/${articleId}/comments`,
    method: 'get'
  })
}

// 发表评论
export function createComment(articleId, data) {
  return request({
    url: `/community/articles/${articleId}/comments`,
    method: 'post',
    data
  })
}

// 删除评论
export function deleteComment(id) {
  return request({
    url: `/community/comments/${id}`,
    method: 'delete'
  })
}

// 点赞评论
export function likeComment(id) {
  return request({
    url: `/community/comments/${id}/like`,
    method: 'post'
  })
}

// ==================== 分类相关 ====================

// 获取所有分类
export function getCategories() {
  return request({
    url: '/community/categories',
    method: 'get'
  })
}