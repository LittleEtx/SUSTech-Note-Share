import axios from 'axios'
import type { UserInfo } from './interfaces'
import type { File } from 'buffer'

/**
 * 返回用户ID
 * @returns {Promise<string>} 用户ID
 */
export async function apiGetUserID (): Promise<number> {
  const { data } = await axios.get('/api/user/get-id')
  return data
}

/**
 * 返回用户信息
 * @param id 用户的ID
 * @returns {Promise<UserInfo>} 用户信息
 */
export async function apiGetUserInfo (id: number): Promise<UserInfo> {
  const { data } = await axios.get('/api/user/get-info', { params: { userID: id } })
  return data
}

/**
 * 更新用户信息
 */
export async function apiUpdateInfo (
  userName: string, description: string, gender?: number, birth?: string
): Promise<void> {
  await axios.post('/api/user/update-info', {
    userName,
    description,
    gender,
    birth
  })
}

/**
 * 上传用户头像
 */
export async function apiUploadAvatar (file: Blob): Promise<void> {
  const params = new FormData()
  params.append('avatar', file)
  await axios.post('/api/user/upload-avatar', params)
}
