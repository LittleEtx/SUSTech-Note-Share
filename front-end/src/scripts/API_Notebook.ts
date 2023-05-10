import axios from 'axios'

/**
 * 上传笔记本封面
 * @param notebookID 笔记本id
 * @param file 笔记本文件
 */
export async function apiUploadNotebookCover(notebookID: string, file: File): Promise<void> {
  const formData = new FormData()
  formData.append('cover', file)
  formData.append('notebookID', notebookID)
  await axios.post('/api/notebooks//upload_cover', formData)
}
