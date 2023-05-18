<template>
  <div v-if="!file">
    <el-empty description="选择文件以预览"/>
  </div>
  <div v-else style="width: 100%; height: 100%">
    <iframe
      v-if="fileType === 'pdf'"
      :src="'static/pdf/web/viewer.html?file=' + file.url"
      style="width: 100%; height: 100%"
    ></iframe>
    <monaco-editor
      v-else-if="fileType === 'text'"
      style="width: 100%; height: 100%"
      :can-modify="canModify"
      :file="file!"
    ></monaco-editor>
  </div>
</template>

<script setup lang="ts">
import type { FileInfo } from '@/scripts/interfaces'
import { ref, watch } from 'vue'
import MonacoEditor from '@/components/notebook_page/MonacoEditor.vue'

interface Props {
  file?: FileInfo
  canModify: boolean
}

const props = defineProps<Props>()

type FileType = 'text' | 'image' | 'video' | 'audio' | 'pdf' | 'other'
const fileType = ref<FileType>()

watch(() => props.file, async () => {
  if (!props.file) {
    return
  }
  switch (props.file.type) {
    case 'application/pdf':
      fileType.value = 'pdf'
      break
    case 'image/png':
    case 'image/jpeg':
    case 'image/gif':
      fileType.value = 'image'
      break
    case 'video/mp4':
      fileType.value = 'video'
      break
    case 'audio/mpeg':
      fileType.value = 'audio'
      break
    default:
      // use text file
      fileType.value = 'text'
      break
  }
  console.log('showing file type:' + fileType.value)
})
</script>

<style scoped>

</style>
