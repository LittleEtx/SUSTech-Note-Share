<template>
  <div
    ref="monacoEditorRef"
    style="text-align: left"
  >
  </div>
</template>
<script setup lang="ts">

import * as monaco from 'monaco-editor'
import { onBeforeUnmount, onMounted, ref, watch } from 'vue'
import type { FileInfo } from '@/scripts/interfaces'

interface Props {
  file: FileInfo
  canModify: boolean
}

const props = defineProps<Props>()

const monacoEditorRef = ref<any>(null)
let editor: monaco.editor.IStandaloneCodeEditor
onBeforeUnmount(() => {
  editor.dispose()
})

onMounted(async () => {
  editor = monaco.editor.create(monacoEditorRef.value, {
    value: '',
    language: 'markdown',
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
  editor.onDidChangeModelContent(() => {
    console.log(editor.getValue())
  })
  await reloadFile(props.file)
})

// ============ 文本编辑 ==================
watch(
  () => props.file,
  async (newFile) => {
    await reloadFile(newFile)
  }
)

const reloadFile = async (file: FileInfo) => {
  console.log('reloading file ' + file.id)
  const response = await fetch(file.url)
  const blob = await response.blob()
  const fileText = await blob.text()
  editor.setModel(monaco.editor.createModel(fileText, 'markdown'))
}

</script>

<style scoped>

</style>
