<template>
  <div style="width: 100%; height: 100%">
    <iframe
      v-if="fileType === 'pdf'"
      :src="'/static/pdf/web/viewer.html?file=' + file.url"
      style="width: 100%; height: 100%"
    ></iframe>
    <monaco-editor
      v-else-if="fileType === 'text'"
      style="width: 100%; height: 100%"
      :can-modify="canModify"
      :file="file!"
    ></monaco-editor>
    <el-scrollbar v-else-if="fileType === 'image'">
      <img :src="file?.url" style="width: 100%" alt="">
    </el-scrollbar>
    <video v-else-if="fileType === 'video'" :src="file?.url" style="width: 100%" controls></video>
    <audio v-else-if="fileType === 'audio'" :src="file?.url" style="width: 100%" controls></audio>
    <div v-else>暂不支持浏览该类型文件</div>
  </div>
</template>

<script setup lang="ts">
import type { FileInfo } from '@/scripts/interfaces'
import { onBeforeMount, ref, watch } from 'vue'
import MonacoEditor from '@/components/notebook_page/MonacoEditor.vue'

interface Props {
  file: FileInfo
  canModify: boolean
}

const props = defineProps<Props>()

type FileType = 'text' | 'image' | 'video' | 'audio' | 'pdf' | 'office' | 'other'
const fileType = ref<FileType>()

const initDisplay = () => {
  if (!props.file) {
    console.log('file is null')
    return
  }
  console.log(props.file.type)
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
    case 'application/vnd.openxmlformats-officedocument.wordprocessingml.document':
    case 'application/msword':
    case 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet':
    case 'application/vnd.ms-excel':
    case 'application/vnd.openxmlformats-officedocument.presentationml.presentation':
    case 'application/vnd.ms-powerpoint':
      fileType.value = 'office'
      break
    default:
      // use text file
      fileType.value = 'text'
      break
  }
  console.log('showing file type:' + fileType.value)
}

onBeforeMount(initDisplay)
watch(() => props.file, initDisplay)

</script>

<style scoped>

</style>
