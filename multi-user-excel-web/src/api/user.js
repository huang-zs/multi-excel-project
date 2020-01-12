import request from '@/utils/request'

export function login (loginForm) {
  return request({
    url: '/user/login',
    method: 'post',
    data: loginForm

  })
}
export function create (createForm) {
  return request({
    url: '/user/create',
    method: 'post',
    data: createForm

  })
}
export function getCode (data) {
  return request({
    url: '/user/mail/code',
    method: 'post',
    data: data

  })
}
