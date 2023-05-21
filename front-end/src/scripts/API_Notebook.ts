import axios from 'axios'
import type { NotebookInfo, NoteInfo } from '@/scripts/interfaces'
import { getTags } from '@/scripts/interfaces'

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
export async function apiCreateNotebook (info: NewNotebookInfo): Promise<string> {
  const { data } = await axios.post('/api/notebook/create', {
    title: info.title,
    directory: info.directory,
    description: info.description,
    tag: info.tags.join(','),
    isPublic: info.isPublic
  })
  return data
}

/**
 * 上传笔记本封面
 * @param notebookID 笔记本id
 * @param file 笔记本文件
 */
export async function apiUploadNotebookCover (notebookID: string, file: File): Promise<void> {
  const formData = new FormData()
  formData.append('cover', file)
  formData.append('notebookID', notebookID)
  await axios.post('/api/notebook/upload_cover', formData)
}

/**
 * 获取笔记本的基本信息
 * @param notebookID 笔记本id
 */
export async function apiGetBasicInfo (notebookID: string): Promise<NotebookInfo> {
  const { data } = await axios.get('/api/notebook/basic', { params: { notebookID } })
  data.tags = getTags(data.tag)
  return data
}

interface UpdateNotebookInfo {
  tags?: string[]
  description?: string
  title?: string
  directory?: string
}

/**
 * 更新笔记本的基本信息
 * @param notebookID 需要更新的笔记本id
 * @param tags 笔记本的标签
 * @param description 笔记本的描述
 * @param title 笔记本标题
 * @param directory 笔记本所处文件夹
 */
export async function apiUpdateBasicInfo (
  notebookID: string,
  { tags, description, title, directory }: UpdateNotebookInfo
): Promise<void> {
  const tag = tags?.join(',')
  await axios.post('/api/notebook/update_info', {
    notebookID,
    tag,
    description,
    title,
    directory
  })
}

/**
 * 获取笔记本下的所有笔记和笔记中的文件
 * @param notebookID
 */
export async function apiGetNoteInfos (notebookID: string): Promise<NoteInfo[]> {
  const { data } = await axios.get('/api/notebook/directory',
    { params: { notebook: notebookID } })
  return data
}

/**
 * 创建笔记
 * @param notebookID 笔记本id
 * @param name 笔记名字
 * @param isPublic 是否公开
 * @returns 笔记id
 */
export async function apiCreateNote (notebookID: string, name: string, isPublic: boolean): Promise<string> {
  const { data } = await axios.post('/api/notebook/create_note', {}, {
    params: {
      notebook: notebookID,
      name,
      public: isPublic
    }
  })
  return data
}

/**
 * 上传文件至笔记
 * @param noteID 笔记id
 * @param file 文件
 */
export async function apiUploadFile (noteID: string, file: File): Promise<string> {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('name', file.name)
  const { data } = await axios.post('/api/notebook/upload-file', formData, {
    params: {
      note: noteID
    }
  })
  return data
}

/**
 * 更新文件
 * @param fileID 需要覆盖的文件id
 * @param file 文件
 */
export async function apiUpdateFile (fileID: string, file: Blob): Promise<void> {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('id', fileID)
  await axios.post('/api/notebook/update-file', formData)
}

/**
 * 删除文件
 * @param fileID
 */
export async function apiDeleteFile (fileID: string): Promise<void> {
  await axios.delete('/api/notebook/delete-file', {
    params: {
      file: fileID
    }
  })
}

/**
 * 重命名文件
 * @param fileID 文件id
 * @param newName 新的文件名
 */
export async function apiRenameFile (fileID: string, newName: string): Promise<void> {
  await axios.post('/api/notebook/rename-file', {}, {
    params: {
      file: fileID,
      name: newName
    }
  })
}

/**
 * 移动文件
 * @param fileID 文件ID
 * @param newNoteID 新的笔记ID
 */
export async function apiMoveFile (fileID: string, newNoteID: string): Promise<void> {
  await axios.post('/api/notebook/move-file', {}, {
    params: {
      file: fileID,
      note: newNoteID
    }
  })
}

/**
 * 重命名笔记
 * @param noteID
 * @param newName
 */
export async function apiRenameNote (noteID: string, newName: string): Promise<void> {
  await axios.post('/api/notebook/rename-note', {}, {
    params: {
      note: noteID,
      name: newName
    }
  })
}

/**
 * 删除笔记
 * @param noteID
 * @param newNote
 */
export async function apiDeleteNote (noteID: string, newNote?: string): Promise<void> {
  await axios.delete('/api/notebook/delete-note', {
    params: {
      note: noteID,
      target: newNote
    }
  })
}

/**
 * 删除笔记本
 * @param notebookID
 */
export const apiDeleteNotebook = async (notebookID: string): Promise<void> => {
  await axios.delete('/api/notebook/delete', {
    params: {
      notebook: notebookID
    }
  })
}
