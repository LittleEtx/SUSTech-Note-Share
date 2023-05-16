<template>
  <div v-if="!file">
    <el-empty description="选择文件以预览"/>
  </div>
  <div v-else style="width: 100%; height: 100%">
    <div v-if="fileType === 'pdf'" style="width: 100%; height: 100%">
      <iframe
        :src="'static/pdf/web/viewer.html?file=' + file.url"
        style="width: 100%; height: 100%"
      ></iframe>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { FileInfo } from '@/scripts/interfaces'
import { ref, watch } from 'vue'

interface Props {
  file?: FileInfo
}

const props = defineProps<Props>()

type FileType = 'text' | 'image' | 'video' | 'audio' | 'pdf' | 'markdown' | 'other'
const fileType = ref<FileType>()
const fileBlob = ref<File>()
const arrayBuffer = ref<ArrayBuffer>()

watch(() => props.file, async () => {
  if (!props.file) {
    return
  }
  console.log(props.file)
  const response = await fetch(props.file!.url)
  const contentType = response.headers.get('Content-Type')
  console.log(contentType)
  const blob = await response.blob()
  fileBlob.value = new File([blob], props.file!.name)
  arrayBuffer.value = await blob.arrayBuffer()
  console.log(arrayBuffer.value)
  console.log(fileBlob.value)

  switch (props.file.type) {
    case 'application/pdf':
      fileType.value = 'pdf'
      break
    case 'text/markdown':
      fileType.value = 'markdown'
      break
    case 'text/plain':
      fileType.value = 'text'
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
      fileType.value = 'other'
      break
  }
  console.log('showing file type:' + fileType.value)
})
</script>

<style scoped>

</style>
