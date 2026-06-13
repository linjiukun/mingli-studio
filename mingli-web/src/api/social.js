import request from './request'

// ==================== 关注相关 ====================

// 关注用户
export function followUser(userId) {
  return request({
    url: `/social/follow/${userId}`,
    method: 'post'
  })
}

// 取消关注
export function unfollowUser(userId) {
  return request({
    url: `/social/follow/${userId}`,
    method: 'delete'
  })
}

// 检查是否已关注
export function checkFollow(userId) {
  return request({
    url: `/social/follow/check/${userId}`,
    method: 'get'
  })
}

// 获取当前用户的关注列表
export function getMyFollowing() {
  return request({
    url: '/social/following',
    method: 'get'
  })
}

// 获取指定用户的关注列表
export function getFollowing(userId) {
  return request({
    url: `/social/following/${userId}`,
    method: 'get'
  })
}

// 获取当前用户的粉丝列表
export function getMyFollowers() {
  return request({
    url: '/social/followers',
    method: 'get'
  })
}

// 获取指定用户的粉丝列表
export function getFollowers(userId) {
  return request({
    url: `/social/followers/${userId}`,
    method: 'get'
  })
}

// 获取当前用户的关注数
export function getMyFollowingCount() {
  return request({
    url: '/social/following/count',
    method: 'get'
  })
}

// 获取当前用户的粉丝数
export function getMyFollowerCount() {
  return request({
    url: '/social/followers/count',
    method: 'get'
  })
}

// 获取指定用户的关注数和粉丝数
export function getFollowCounts(userId) {
  return request({
    url: `/social/counts/${userId}`,
    method: 'get'
  })
}