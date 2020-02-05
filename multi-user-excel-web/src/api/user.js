import request from '@/utils/request'
// 用户登录
export function login (loginForm) {
return request({
  url: '/user/login',
    method: 'post',
    data: loginForm

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
  return request({
    url: '/user/create',
    method: 'post',
    data: createForm

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
export function update (data) {
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
