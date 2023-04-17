import axios from 'axios'

/**
 * 返回用户ID
 * @returns {Promise<string>} 用户ID
 */
export async function apiGetUserID () {
  const { data } = await axios.get('/api/user/get-id')
  return data
}

/**
 * @typedef {Object} UserInfo
 * @property {int} uid 用户ID
 * @property {string} userName 用户名
 * @property {string} email 用户邮箱
 * @property {string} avatar 用户头像URL
 * @property {string} description 签名
 * @property {boolean} gender 性别, true为女性
 * @property {string} birth 生日，格式为YYYY-MM-DD
 */
/**
 * 返回用户信息
 * @param id 用户的ID
 * @returns {Promise<UserInfo>} 用户信息
 */
export async function apiGetUserInfo (id) {
  const { data } = await axios.get('/api/user/get-info', { params: { userID: id }
  })
  return data
}
