import type { NotebookInfo } from '@/scripts/interfaces'
import axios from 'axios'

/**
 * 获取本用户的所有笔记本
 */
export async function apiGetUserNotebooks (): Promise<NotebookInfo[]> {
  const { data } = await axios.get('/api/center/notebooks/get')
  return data
}

/**
 * 创建笔记本
 * @param title 笔记本标题
 * @param directory 笔记本所属分类
 * @param description 笔记本描述
 * @param tag 笔记本标签
 * @param isPublic 是否公开
 */
export async function apiCreateNotebook (
  title: string,
  directory: string,
  description: string,
  tag: string,
  isPublic: boolean
): Promise<void> {
  await axios.post('/api/center/notebooks/create', {
    title,
    directory,
    description,
    tag,
    isPublic
  })
}

/**
 * 获取某个用户的所有公开笔记本
 * @param userID
 */
export async function apiGetPublicNotebooks (userID: number): Promise<NotebookInfo[]> {
  const { data } = await axios.get('/api/center/public-notebooks', { params: { userID } })
  return data
}
