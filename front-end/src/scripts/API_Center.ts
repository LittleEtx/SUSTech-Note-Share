import type { NotebookInfo } from '@/scripts/interfaces'
import axios from 'axios'

/**
 * 获取本用户的所有笔记本
 */
export async function apiGetUserNotebooks(): Promise<NotebookInfo[]> {
  const {data} = await axios.get('/api/center/notebooks/get')
  return data.map((notebook: any) => {
    notebook.tags = notebook.tag.split(',')
    return notebook
  })
}

export interface NewNotebookInfo {
  title: string // 笔记本标题
  tags: string[] // 笔记本标签
  description: string // 笔记本描述
  directory: string // 笔记本所处文件夹
  isPublic: boolean // 是否公开
}

/**
 * 创建笔记本
 * @param info 笔记本信息
 */
export async function apiCreateNotebook(info: NewNotebookInfo): Promise<string> {
  const {data} = await axios.post('/api/center/notebooks/create', {
    title: info.title,
    directory: info.directory,
    description: info.description,
    tag: info.tags.join(','),
    isPublic: info.isPublic
  })
  return data
}

/**
 * 获取某个用户的所有公开笔记本
 * @param userID
 */
export async function apiGetPublicNotebooks (userID: number): Promise<NotebookInfo[]> {
  const { data } = await axios.get('/api/center/public-notebooks', { params: { userID } })
  return data
}
