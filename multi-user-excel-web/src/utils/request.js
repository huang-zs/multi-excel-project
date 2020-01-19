import axios from 'axios'
import store from '../store'
import {Message} from 'element-ui'

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
service.interceptors.response.use(
  response => {
    if (response.data.status && response.data.status === 200 && response.data.data.status === 200) {
      Message.success({message: response.data.data.msg})
    }
    return response
  }
)

export default service
