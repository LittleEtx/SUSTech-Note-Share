import axios from 'axios'
import type { HistoryInfo } from '@/scripts/interfaces'

/**
 * 获取历史记录
 * @param start 起始位置
 * @param limit 限制数量
 */
export const apiGetHistory = async (start?: number, limit?: number): Promise<HistoryInfo[]> => {
  const { data } = await axios.get('/api/history/get-notebook-history', {
    params: {
      start,
      limit
    }
  })
  return data
}

/**
 * 添加一条历史记录
 * @param notebookID
 */
export const apiAddHistory = async (notebookID: string): Promise<void> => {
  await axios.post('/api/history/visit', {}, {
    params: {
      notebook: notebookID
    }
  })
}

/**
 * 删除某本笔记本的历史记录
 * @param notebookID
 */
export const apiDeleteHistory = async (notebookID: string): Promise<void> => {
  await axios.delete('/api/history/delete', {
    params: {
      notebook: notebookID
    }
  })
}

/**
 * 删除所有历史记录
 */
export const apiDeleteAllHistory = async (): Promise<void> => {
  await axios.delete('/api/history/delete-all')
}
