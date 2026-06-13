import request from './request'

// 获取通知列表
export function getNotifications(params) {
  return request({
    url: '/notification/list',
    method: 'get',
    params
  })
}

// 获取未读通知数量
export function getUnreadCount() {
  return request({
    url: '/notification/unread/count',
    method: 'get'
  })
}

// 标记通知为已读
export function markAsRead(id) {
  return request({
    url: `/notification/${id}/read`,
    method: 'put'
  })
}

// 标记所有通知为已读
export function markAllAsRead() {
  return request({
    url: '/notification/read-all',
    method: 'put'
  })
}