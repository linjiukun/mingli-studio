import request from '@/api/request'

/**
 * 获取我的星座（根据用户出生日期）
 */
export function getMyZodiac() {
  return request({
    url: '/zodiac/my-sign',
    method: 'get'
  })
}

/**
 * 获取所有星座列表
 */
export function getAllSigns() {
  return request({
    url: '/zodiac/all-signs',
    method: 'get'
  })
}

/**
 * 获取星座详情
 */
export function getSignDetail(index) {
  return request({
    url: `/zodiac/sign/${index}`,
    method: 'get'
  })
}

/**
 * 根据生日计算星座
 */
export function calculateZodiac(month, day) {
  return request({
    url: '/zodiac/calculate',
    method: 'get',
    params: { month, day }
  })
}

/**
 * 获取星座每日运势
 */
export function getDailyHoroscope(sign, date) {
  return request({
    url: '/zodiac/daily',
    method: 'get',
    params: { sign, date }
  })
}

/**
 * 获取星座周运势
 */
export function getWeeklyHoroscope(sign, week) {
  return request({
    url: '/zodiac/weekly',
    method: 'get',
    params: { sign, week }
  })
}

/**
 * 获取星座月运势
 */
export function getMonthlyHoroscope(sign, month) {
  return request({
    url: '/zodiac/monthly',
    method: 'get',
    params: { sign, month }
  })
}

/**
 * 星座配对分析
 */
export function getCompatibility(sign1, sign2) {
  return request({
    url: '/zodiac/compatibility',
    method: 'get',
    params: { sign1, sign2 }
  })
}
