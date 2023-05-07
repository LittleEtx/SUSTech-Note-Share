import axios from 'axios'

/**
 * 返回用户ID
 * @returns {Promise<string>} 用户ID
 */
export async function apiGetUserID (): Promise<string> {
  const { data } = await axios.get('/api/user/get-id')
  return data.toString()
}

interface UserInfo {
  userID: number // 用户ID
  userName: string // 用户名
  email: string // 用户邮箱
  avatar: string // 用户头像URL
  description: string // 签名
  gender?: number // 性别, 1为男性，0为女性
  birth?: string // 生日，格式为YYYY-MM-DD
}

/**
 * 返回用户信息
 * @param id 用户的ID
 * @returns {Promise<UserInfo>} 用户信息
 */
export async function apiGetUserInfo (id: string): Promise<UserInfo> {
  const { data } = await axios.get('/api/user/get-info', { params: { userID: id } })
  return data
}

/**
 * 更新用户信息
 */
export async function apiUpdateInfo (
    userName: string, description: string, gender?:number, birth?: string
): Promise<void> {
  await axios.post('/api/user/update-info', {
    userName,
    description,
    gender,
    birth
  })
}
