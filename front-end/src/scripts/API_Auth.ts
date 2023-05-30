import axios from 'axios'
import { store } from '@/store/store'

/**
 * 使得当前用户登出
 */
export async function apiLogout (): Promise<void> {
  await axios.post('/api/auth/logout')
}

/**
 * 向该邮箱发送一个验证码（邮箱可以未登录）
 */
export async function apiSendEmailCode (email: string): Promise<void> {
  await axios.post('api/auth/send-email-code', { email })
}

/**
 * 邮箱验证码登录
 */
export async function apiLoginViaCode (email: string, code: string, rememberMe: boolean): Promise<void> {
  const { data } = await axios.post('/api/auth/login/email-code-login', {
    email,
    verificationCode: code,
    rememberMe
  })
  store.commit('setToken', data)
}

/**
 * 密码登录
 * @param email 邮箱
 * @param password 密码
 * @param rememberMe 记住我
 */
export async function apiLoginViaPassword (email: string, password: string, rememberMe: boolean): Promise<void> {
  const { data } = await axios.post('/api/auth/login/password-login', {
    email,
    password,
    rememberMe
  })
  store.commit('setToken', data)
}

/**
 * 向该邮箱发送一个重置密码用的验证码，若邮箱未注册返回400
 * @param email 邮箱
 */
export async function apiResetPassVerifyEmail (email: string): Promise<void> {
  await axios.post('/api/auth/reset-password/verify-email', { email })
}

/**
 * 验证邮箱，返回一个token用以重置密码
 * @param email 邮箱
 * @param verificationCode 验证码
 * @returns {Promise<string>} token
 */
export async function apiResetPassVerifyCode (email: string, verificationCode: string): Promise<string> {
  const { data } = await axios.post('/api/auth/reset-password/verify-code', {
    email,
    verificationCode
  })
  return data
}

/**
 * 修改token对应的账号的密码
 * @param token 上述接口得到的token
 * @param password
 */
export async function apiResetPassword (token: string, password: string): Promise<void> {
  await axios.post('/api/auth/reset-password/reset', {
    token,
    password
  })
}
