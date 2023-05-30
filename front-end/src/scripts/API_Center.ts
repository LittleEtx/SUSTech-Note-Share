import type { NotebookInfo } from '@/scripts/interfaces'
import { getTags } from '@/scripts/interfaces'
import axios from 'axios'

/**
 * 获取本用户的所有笔记本
 */
export async function apiGetUserNotebooks (): Promise<NotebookInfo[]> {
  const { data } = await axios.get('/api/center/get-notebooks')
  return data.map((notebook: any) => {
    notebook.tags = getTags(notebook.tag)
    return notebook
  })
}

/**
 * 获取某个用户的所有公开笔记本
 * @param userID
 */
export async function apiGetPublicNotebooks (userID: number): Promise<NotebookInfo[]> {
  const { data } = await axios.get('/api/center/public-notebooks', {
    params: {
      user: userID
    }
  })
  return data.map((notebook: any) => {
    notebook.tags = getTags(notebook.tag)
    return notebook
  })
}

export async function apiGetStarredNotebooks (): Promise<NotebookInfo[]> {
  const { data } = await axios.get('/api/center/get-starred-notebooks')
  return data.map((notebook: any) => {
    notebook.tags = getTags(notebook.tag)
    return notebook
  })
}

export async function apiGetSharedNotebook (): Promise<NotebookInfo[]> {
  const { data } = await axios.get('/api/center/get-shared-notebooks')
  return data.map((notebook: any) => {
    notebook.tags = getTags(notebook.tag)
    return notebook
  })
}