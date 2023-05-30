<template>
  <div
    ref="monacoEditorRef"
    style="text-align: left"
  >
  </div>
</template>
<script setup lang="ts">

import * as monaco from 'monaco-editor'
import { editor } from 'monaco-editor'
import { onBeforeUnmount, onMounted, ref, watch } from 'vue'
import type { FileInfo } from '@/scripts/interfaces'
import { apiUpdateFile } from '@/scripts/API_Notebook'
import { ElMessage } from 'element-plus'
import IStandaloneCodeEditor = editor.IStandaloneCodeEditor

interface Props {
  file: FileInfo
  canModify: boolean
}

const props = defineProps<Props>()

const monacoEditorRef = ref<any>(null)
let monacoEditor: IStandaloneCodeEditor

onBeforeUnmount(() => {
  monacoEditor.dispose()
})

const saveFlag = ref(false)
onMounted(async () => {
  monacoEditor = monaco.editor.create(monacoEditorRef.value, {
    value: '',
    automaticLayout: true, // 自适应布局
    theme: 'vs', // 官方自带三种主题vs, hc-black, or vs-dark
    scrollbar: {
      verticalScrollbarSize: 6,
      horizontalScrollbarSize: 6
    },
    unicodeHighlight: {
      ambiguousCharacters: false
    },
    renderLineHighlight: 'all', // 行亮
    selectOnLineNumbers: true, // 显示行号
    minimap: {
      enabled: false
    },
    readOnly: !props.canModify, // 只读
    fontSize: 16, // 字体大小
    overviewRulerBorder: false // 不要滚动条的边框
  })
  monacoEditor.onDidChangeModelContent(() => {
    saveFlag.value = true
  })
  await reloadFile(props.file)
})

setInterval(async () => {
  if (saveFlag.value) {
    await uploadContent()
    saveFlag.value = false
  }
}, 3000) // 每隔3秒保存一次

const uploadContent = async () => {
  try {
    await apiUpdateFile(props.file.id, new Blob([monacoEditor.getValue()]))
  } catch (e) {
    ElMessage.warning('网络错误：无法上传文件！')
  }
}

// ============ 切换文件时重新挂载Model ==================
watch(
  () => props.file,
  async (newFile) => {
    await reloadFile(newFile)
  }
)

const reloadFile = async (file: FileInfo) => {
  const uri = file.id + '/' + file.name
  if (monaco.editor.getModel(monaco.Uri.file(uri))) {
    // 如果存在该model了，直接使用
    const model = monaco.editor.getModel(monaco.Uri.file(uri))!
    monacoEditor.setModel(model)
    return
  }
  const response = await fetch(file.url)
  const blob = await response.blob()
  const fileText = await blob.text()
  const model = monaco.editor.createModel(fileText,
    undefined, monaco.Uri.file(uri))// 根据文件名字自动选择语言
  monacoEditor.setModel(model)
}

</script>

<style scoped>

</style>
