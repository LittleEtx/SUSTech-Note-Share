import axios from 'axios'

/**
 * 使得当前用户登出
 */
export function apiLogout () {
  return axios.post('/api/auth/logout')
}

export function apiSendEmailCode () {
  return axios.post('api/auth/send-email-code')
}

/**
 * 邮箱验证码登录
 */
export function apiLoginViaCode (email, code, rememberMe) {
  return axios.post('/api/auth/login/email-code-login', {
    email: email,
    verificationCode: code,
    rememberMe: rememberMe
  })
}

/**
 * 密码登录
 * @param email 邮箱
 * @param password 密码
 * @param rememberMe 记住我
 */
export function apiLoginViaPassword (email, password, rememberMe) {
  return axios.post('/api/auth/login/password-login', {
    email: email,
    password: password,
    rememberMe: rememberMe
  })
}

/**
 * 向该邮箱发送一个重置密码用的验证码
 * @param email 邮箱
 */
export function apiResetPwdConfirmEmail (email) {
  return axios.post('/api/auth/reset-password/confirm-email', { email: email })
}

/**
 * 验证邮箱，返回一个token用以重置密码
 * @param email 邮箱
 * @param verificationCode 验证码
 */
export function apiResetPwdVerify (email, verificationCode) {
  return axios.post('/api/auth/reset-password/verify-email', {
    email: email,
    verificationCode: verificationCode
  })
}

/**
 * 修改token对应的账号的密码
 * @param token 上述接口得到的token
 * @param password
 */
export function apiResetPwd (token, password) {
  return axios.post('/api/auth/reset-password/reset', {
    token: token,
    password: password
  })
}
