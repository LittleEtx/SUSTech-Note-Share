export const downloadFile = (url: string, name: string): void => {
  const a = document.createElement('a') // 生成一个a元素
  const event = new MouseEvent('click') // 创建一个单击事件
  a.download = name // 设置图片名称
  a.href = url // 将生成的URL设置为a.href属性
  a.dispatchEvent(event) // 触发a的单击事件
}
