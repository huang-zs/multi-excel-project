import axios from 'axios'
import store from '../store'
import { MessageBox } from 'element-ui'
import router from '../router'

const service = axios.create({
  baseURL: process.env.BASE_URL,
  timeout: 15000
})
// 添加请求拦截器
service.interceptors.request.use(
  config => {
    // 如果store里面有token的话就给报文头加
    if (store.state.SecurtToken !== '') {
      config.headers.SecurtToken = store.state.SecurtToken
    }
    return config
  }
)
//响应拦截器
service.interceptors.response.use(
  response => {//200的走这里
    // if (response.data.status && response.data.status === 200 && response.data.data.code !== 200) {
    //   console.log('接口' + response.config.url + ']调用失败')
    // }
    // console.log('正常响应')
    // console.log(response)

    return response
  },(error) =>{ //非200的会走这里
    console.log('响应报错')
    // console.log(error.response)
    if(error.response.status && error.response.status ===401){
      store.commit('deleteUserInfo')
      MessageBox.alert(error.response.data, '用户未登录', {
        confirmButtonText: '去登录',
        showClose:false,
        callback: action => {
          console.log('去登陆了')
          router.push({path:'/user/login'})
        }
      })
    }
  }
)

export default service
