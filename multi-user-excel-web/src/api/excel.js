import request from '@/utils/request'

export function create (createForm) {
  return request({
    url: '/excel/create',
    method: 'post',
    data: createForm

  })
}
export function get (data) {
  return request({
    url: '/excel/get',
    method: 'post',
    data: data

  })
}
export function save (data) {
  return request({
    url: '/excel/save',
    method: 'post',
    data: data

  })
}
export function list (data) {
  return request({
    url: '/excel/list',
    method: 'post',
    data: data

  })
}
