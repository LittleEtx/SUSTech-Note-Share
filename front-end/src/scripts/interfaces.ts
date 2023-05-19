export interface UserInfo {
  userID: number // 用户ID
  userName: string // 用户名
  email: string // 用户邮箱
  avatar: string // 用户头像URL
  description: string // 签名
  gender?: number // 性别, 1为男性，0为女性
  birth?: string // 生日，格式为YYYY-MM-DD
}

export interface NotebookInfo {
  notebookID: string // 笔记本ID
  title: string // 笔记本标题
  tags: string[] // 笔记本标签
  updateTime: string // 笔记本更新时间
  authorID: number // 笔记本作者ID
  cover: string | null // 笔记本封面URL
  description: string // 笔记本描述
  isPublic: boolean // 是否公开
  likeCount: number // 点赞数
  starCount: number // 评论数
  directory: string // 所属文件夹
}

export function getTags (tag?: string): string[] {
  return (tag == null) || tag === '' ? [] : tag.split(',')
}

export interface FileInfo {
  id: string // 文件ID
  name: string // 文件名
  url: string // 文件URL
  type: string // 文件类型
}

export interface NoteInfo {
  name: string // 文件夹的名字
  id: string // 文件夹的id
  files: FileInfo[] // 文件夹下的文件
}

export interface GroupInfo {
  groupID: number // 群组ID
  groupName: string // 群组名字
  groupDescription: string // 群组描述
  createTime: string // 群组创建时间
  owner: UserInfo // 群组的创建者
}
