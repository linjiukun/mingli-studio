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

// 获取关注列表
export function getFollowing(userId) {
  return request({
    url: '/social/following',
    params: { userId }
  })
}

// 获取粉丝列表
export function getFollowers(userId) {
  return request({
    url: '/social/followers',
    params: { userId }
  })
}

// 获取关注数和粉丝数
export function getFollowCounts(userId) {
  return request({
    url: '/social/counts',
    params: { userId }
  })
}