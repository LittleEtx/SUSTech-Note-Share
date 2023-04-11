import axios from 'axios'

/**
 * 使得当前用户登出
 * @returns data 若返回1则登出成功
 */
export function apiLogout () { return axios.post('/api/auth/logout') }
