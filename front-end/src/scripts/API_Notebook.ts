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
}

/**
 * 更新笔记本的基本信息
 * @param notebookID 需要更新的笔记本id
 * @param tags 笔记本的标签
 * @param description 笔记本的描述
 * @param title 笔记本标题
 */
export async function apiUpdateBasicInfo (
  notebookID: string,
  { tags, description, title }: UpdateNotebookInfo
): Promise<void> {
  const tag = tags?.join(',')
  await axios.post('/api/notebook/update_info', {
    notebookID,
    tag,
    description,
    title
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

export async function apiUploadFile (noteID: string, file: File, fileID?: string): Promise<string> {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('name', file.name)
  const { data } = await axios.post('/api/notebook/upload-file', formData, {
    params: {
      note: noteID,
      origin: fileID
    }
  })
  return data
}

export async function apiDeleteFile (fileID: string): Promise<void> {
  await axios.delete('/api/notebook/delete-file', {
    params: {
      file: fileID
    }
  })
}
