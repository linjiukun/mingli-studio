import request from './request'

// 获取已认证命理师列表
export function getConsultants() {
  return request({
    url: '/consultants',
    method: 'get'
  })
}

// 获取命理师详情
export function getConsultant(id) {
  return request({
    url: `/consultants/${id}`,
    method: 'get'
  })
}

// 申请成为命理师
export function applyConsultant(data) {
  return request({
    url: '/consultants/apply',
    method: 'post',
    data
  })
}

// 更新命理师信息
export function updateConsultant(id, data) {
  return request({
    url: `/consultants/${id}`,
    method: 'put',
    data
  })
}

// 获取当前用户的命理师信息
export function getMyConsultant() {
  return request({
    url: '/consultants/my',
    method: 'get'
  })
}