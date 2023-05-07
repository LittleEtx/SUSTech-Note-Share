export interface UserInfo {
  userID: number // 用户ID
  userName: string // 用户名
  email: string // 用户邮箱
  avatar: string // 用户头像URL
  description: string // 签名
  gender?: number // 性别, 1为男性，0为女性
  birth?: string // 生日，格式为YYYY-MM-DD
}
