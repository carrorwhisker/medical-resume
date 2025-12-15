import axios from 'axios'
import { Message } from 'element-ui'
import Cookies from 'js-cookie'
import router from '@/router'

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'

// 创建医简历专用的axios实例
const medicalService = axios.create({
  // 医简历API基础路径
  baseURL: process.env.VUE_APP_MEDICAL_API || 'http://123.56.162.200:8080/Main/action',
  timeout: 10000
})

// 请求拦截器
medicalService.interceptors.request.use(
  config => {
    // 从Cookie中获取token
    const token = Cookies.get('Admin-Token')
    if (token) {
      // 后台管理使用JWT认证
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => {
    console.log(error)
    return Promise.reject(error)
  }
)

// 响应拦截器
medicalService.interceptors.response.use(
  response => {
    const { code, msg } = response.data
    
    // 处理登录过期 (401未授权)
    if (code == 401 || code == 201) {
      Message({
        message: msg || '登录已过期，请重新登录',
        type: 'error'
      })
      Cookies.remove('Admin-Token')
      // 跳转到登录页
      router.push('/login')
      return Promise.reject(new Error(msg || '登录已过期'))
    } 
    // 处理其他错误
    else if (code !== 200) {
      Message({
        message: msg || '请求失败',
        type: 'error'
      })
      return Promise.reject(new Error(msg || '请求失败'))
    }
    
    // 返回数据
    return response.data
  },
  error => {
    console.log('err' + error)
    let { message } = error
    if (message == "Network Error") {
      message = "后端接口连接异常"
    } else if (message.includes("timeout")) {
      message = "系统接口请求超时"
    } else if (message.includes("Request failed with status code")) {
      message = "系统接口" + message.substr(message.length - 3) + "异常"
    }
    Message({
      message: message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default medicalService

