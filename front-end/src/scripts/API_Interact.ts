import type { GroupInfo, UserInfo } from '@/scripts/interfaces'
import axios from 'axios'

/**
 * 获取笔记本被分享给的所有用户
 * @param notebookID
 */
export const apiGetNotebookSharedUsers = async (notebookID: string): Promise<UserInfo[]> => {
  const { data } = await axios.get('/api/interact/share/get-shared-users', {
    params: {
      notebook: notebookID
    }
  })
  return data
}

/**
 * 获取笔记本被分享给的所有群组
 * @param notebookID
 */
export const apiGetNotebookSharedGroups = async (notebookID: string): Promise<GroupInfo[]> => {
  const { data } = await axios.get('/api/interact/share/get-shared-groups', {
    params: {
      notebook: notebookID
    }
  })
  return data
}

/**
 * 将笔记本分享给某个用户
 * @param notebookID
 * @param targetUserID
 */
export const apiShareNotebookToUser = async (notebookID: string, targetUserID: number): Promise<void> => {
  await axios.post('/api/interact/share/share-to-user', {}, {
    params: {
      notebook: notebookID,
      target: targetUserID
    }
  })
}

/**
 * 取消分享给某个用户
 * @param notebookID
 * @param targetUserID
 */
export const apiCancelUserShare = async (notebookID: string, targetUserID: number): Promise<void> => {
  await axios.post('/api/interact/share/cancel-user-share', {}, {
    params: {
      notebook: notebookID,
      target: targetUserID
    }
  })
}

/**
 * 将笔记本分享给某个群组
 * @param notebookID
 * @param targetGroupID
 */
export const apiShareNotebookToGroup = async (notebookID: string, targetGroupID: number): Promise<void> => {
  await axios.post('/api/interact/share/share-to-group', {}, {
    params: {
      notebook: notebookID,
      group: targetGroupID
    }
  })
}

/**
 * 取消分享给某个群组
 * @param notebookID
 * @param targetGroupID
 */
export const apiCancelGroupShare = async (notebookID: string, targetGroupID: number): Promise<void> => {
  await axios.post('/api/interact/share/cancel-group-share', {}, {
    params: {
      notebook: notebookID,
      group: targetGroupID
    }
  })
}
