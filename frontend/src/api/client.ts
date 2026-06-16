import axios from 'axios'
import type { ApiResult } from '@/types'

const http = axios.create({
  baseURL: '/api',
  timeout: 15000
})

// 统一解包：直接返回 data 字段
http.interceptors.response.use(
  (resp) => {
    const body = resp.data as ApiResult<unknown>
    if (body && body.code === 0) {
      return body.data
    }
    return Promise.reject(new Error(body?.message || '请求失败'))
  },
  (err) => {
    const msg = err?.response?.data?.message || err.message || '网络错误'
    return Promise.reject(new Error(msg))
  }
)

export { http }
