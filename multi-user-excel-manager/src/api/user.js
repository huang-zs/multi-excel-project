import request from '@/utils/request'
import {encrypt} from '@/api/utils'
// 用户登录
export function login (loginForm) {
  let data={}
  Object.assign(data,loginForm)
  data.password=encrypt(data.password)
  return request({
  url: '/user/login',
    method: 'post',
    data: data

  })
}
// 用户登出
export function logout () {
return request({
  url: '/user/logout',
    method: 'post'
  })
}

// 新建用户
export function create (createForm) {
  let data={}
  Object.assign(data,createForm)
  data.password=encrypt(data.password)
  return request({
    url: '/user/create',
    method: 'post',
    data: data

  })
}
//获取邮箱验证码
export function getCode (data) {
  return request({
    url: '/user/mail/code',
    method: 'post',
    data: data

  })
}
//重置密码
export function resetPassword (data) {
  return request({
    url: '/user/resetPassword',
    method: 'post',
    data: data

  })
}
//修改信息
export function update (updateForm) {
  let data={}
  Object.assign(data,updateForm)
  if(data.password)
  data.password=encrypt(data.password)
  return request({
    url: '/user/update',
    method: 'post',
    data: data

  })
}
//意见反馈
export function talkToAuthor (data) {
  return request({
    url: '/user/messageAdvice',
    method: 'post',
    data: data

  })
}
