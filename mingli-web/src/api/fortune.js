import request from './request'

export function requestReading(data) {
  return request({
    url: '/fortune/reading',
    method: 'post',
    data
  })
}

export function getReadings() {
  return request({
    url: '/fortune/readings',
    method: 'get'
  })
}
