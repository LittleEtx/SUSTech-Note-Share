export const studentEmailPostfix = 'mail.sustech.edu.cn'
export const validEmailPostfixes = [
  'mail.sustech.edu.cn',
  'sustech.edu.cn'
]

export const validateEmailRule = (region, rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入邮箱前缀'))
  } else if (
    // 当选择了学校邮箱后缀时，验证学号是否为8位数字
    region === studentEmailPostfix &&
    !/^[0-9]{8}$/.test(value)
  ) {
    callback(new Error('学号格式错误'))
  } else {
    callback()
  }
}
