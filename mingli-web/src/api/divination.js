import request from './request'

export function ask(data) {
  return request({
    url: '/divination/ask',
    method: 'post',
    data
  })
}
