import type { GroupInfo, NotebookInfo, UserInfo } from '@/scripts/interfaces'
import axios from 'axios'

/**
 * 搜索用户
 * @param keyword
 */
export const apiSearchUsers = async (keyword: string): Promise<UserInfo[]> => {
  const { data } = await axios.get('/api/search/user', {
    params: {
      key: keyword
    }
  })
  return data
}

/**
 * 搜索公开笔记本
 * @param keyword
 * @param page
 * @param numberPerPage
 */
export const apiSearchNotebooks = async (
  keyword: string, page: number, numberPerPage: number
): Promise<NotebookInfo[]> => {
  const { data } = await axios.get('/api/search/notebook', {
    params: {
      key: keyword,
      pageSize: numberPerPage,
      pageNumber: page
    }
  })
  return data
}

/**
 * 搜索群组
 * @param keyword
 */
export const apiSearchGroups = async (keyword: string): Promise<GroupInfo[]> => {
  const { data } = await axios.get('/api/search/group', {
    params: {
      key: keyword
    }
  })
  return data
}
