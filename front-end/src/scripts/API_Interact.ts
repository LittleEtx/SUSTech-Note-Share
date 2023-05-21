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
      notebookID: notebookID,
      groupID: targetGroupID
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
      notebookID: notebookID,
      groupID: targetGroupID
    }
  })
}

/**
 * 将笔记本公开
 * @param notebookID
 */
export const apiSetNotebookPublic = async (notebookID: string): Promise<void> => {
  await axios.post('/api/interact/set-notebook-public', {}, {
    params: {
      notebook: notebookID
    }
  })
}

/**
 * 将笔记本设为私有
 * @param notebookID
 */
export const apiSetNotebookPrivate = async (notebookID: string): Promise<void> => {
  await axios.post('/api/interact/set-notebook-private', {}, {
    params: {
      notebook: notebookID
    }
  })
}

export const apiIfLikeNotebook = async (notebookID: string): Promise<boolean> => {
  const { data } = await axios.get('/api/interact/if-like', {
    params: {
      notebook: notebookID
    }
  })
  return data
}

export const apiLikeNotebook = async (notebookID: string): Promise<void> => {
  await axios.post('/api/interact/like-notebook', {}, {
    params: {
      notebook: notebookID
    }
  })
}

export const apiCancelLikeNotebook = async (notebookID: string): Promise<void> => {
  await axios.post('/api/interact/cancel-like-notebook', {}, {
    params: {
      notebook: notebookID
    }
  })
}

export const apiIfStarNotebook = async (notebookID: string): Promise<boolean> => {
  const { data } = await axios.get('/api/interact/if-star', {
    params: {
      notebook: notebookID
    }
  })
  return data
}

export const apiStarNotebook = async (notebookID: string): Promise<void> => {
  await axios.post('/api/interact/star-notebook', {}, {
    params: {
      notebook: notebookID
    }
  })
}

export const apiCancelStarNotebook = async (notebookID: string): Promise<void> => {
  await axios.post('/api/interact/cancel-star-notebook', {}, {
    params: {
      notebook: notebookID
    }
  })
}
