import axios from 'axios'

/**
 * 返回用户ID
 * @returns data 用户ID
 */
export function apiGetUserID () {
  return axios.get('/api/user/get-id')
}

/**
 * 返回用户信息
 * @param id 用户的ID
 * @returns
 * <li>
 *   data.uid 用户ID
 *   data.userName 用户名
 *   data.email 用户邮箱
 *   data.avatar 用户头像URL
 *   data.description 签名
 *   data.gender 性别, true为女性
 *   data.birth 生日，格式为YYYY-MM-DD
 * </li>
 */
export function apiGetUserInfo (id) {
  return axios.get('/api/user/get-info', { params: { userID: id }
  })
}
