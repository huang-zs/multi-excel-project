import request from '@/utils/request'
//获取门户图片
export function getDoorImages() {
  return request({
  url: '/door/image/list',
    method: 'post'

  })
}