import request from '@/utils/request'
/**
 * 根据spread对象创建 excel记录
 */
export function create (createForm) {
  return request({
    url: '/excel/create',
    method: 'post',
    data: createForm

  })
}
/**
 * 根据excelId删除excel记录
 */
export function remove (data) {
  return request({
    url: '/excel/delete',
    method: 'post',
    data: data

  })
}
/**
 * 根据excelId获取单个excel对象
 */
export function get (data) {
  return request({
    url: '/excel/get',
    method: 'post',
    data: data

  })
}
/**
 * spread对象保存 redis
 */
export function easySave (data) {
  return request({
    url: '/excel/easySave',
    method: 'post',
    data: data

  })
}
/**
 * spread对象保存 redis+database
 */
export function hardSave (data) {
  return request({
    url: '/excel/hardSave',
    method: 'post',
    data: data

  })
}
/**
 * 查询excel列表
 */
export function list (data) {
  return request({
    url: '/excel/list',
    method: 'post',
    data: data

  })
}
/**
 * 验证码校验excel是否存在和绑定协助关系
 */
export function checkAndBindExcel (data) {
  return request({
    url: '/excel/assist',
    method: 'post',
    data: data

  })
}
/**
 * 删除的excel还原
 */
export function recover (data) {
  return request({
    url: '/excel/recover',
    method: 'post',
    data: data

  })
}
