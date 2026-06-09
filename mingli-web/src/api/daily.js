import request from './request'

export function getToday() {
  return request({
    url: '/daily/today',
    method: 'get'
  })
}

export function getRange(params) {
  return request({
    url: '/daily/range',
    method: 'get',
    params
  })
}
