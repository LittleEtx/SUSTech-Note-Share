export const downloadFile = (url: string, name: string): void => {
  const a = document.createElement('a') // 生成一个a元素
  const event = new MouseEvent('click') // 创建一个单击事件
  a.download = name // 设置图片名称
  a.href = url // 将生成的URL设置为a.href属性
  a.dispatchEvent(event) // 触发a的单击事件
}

export const getDateStr = (dateMs: number): string => {
  // 获取js 时间戳
  let time = new Date().getTime()
  // 去掉 js 时间戳后三位，与php 时间戳保持一致
  time = (time - dateMs) / 1000
  // 存储转换值
  if (time < 60 * 10) {
    // 十分钟内
    return '刚刚'
  } else if ((time < 60 * 60) && (time >= 60 * 10)) {
    // 超过十分钟少于1小时
    return `${Math.floor(time / 60)}分钟前`
  } else if ((time < 60 * 60 * 24) && (time >= 60 * 60)) {
    // 超过1小时少于24小时
    return `${Math.floor(time / 60 / 60)}小时前`
  } else if ((time < 60 * 60 * 24 * 30) && (time >= 60 * 60 * 24)) {
    // 超过1天少于30天内
    return `${Math.floor(time / 60 / 60 / 24)}天前`
  } else {
    // 超过30天ddd
    const date = new Date(dateMs)
    return `${date.getFullYear()}/${(date.getMonth() + 1)}/${date.getDate()}`
  }
}
