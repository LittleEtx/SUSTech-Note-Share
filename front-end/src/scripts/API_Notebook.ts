import axios from 'axios'

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
