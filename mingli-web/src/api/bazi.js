import request from './request'

export function calculate(data) {
  return request({
    url: '/bazi/calculate',
    method: 'post',
    data
  })
}

export function getMyChart() {
  return request({
    url: '/bazi/my-chart',
    method: 'get'
  })
}

export function getHistory() {
  return request({
    url: '/bazi/history',
    method: 'get'
  })
}
