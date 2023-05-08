<template>
<el-space>
  <notebook-display :notebook="notebook"></notebook-display>
</el-space>
</template>
<script setup lang="ts">
import NotebookDisplay from '@/components/NotebookDisplay.vue'
import type { NotebookInfo } from '@/scripts/interfaces'
import DefaultCover from '@/assets/default-file/default-notebook-cover.png'
const notebook: NotebookInfo = {
  id: '1211262834',
  title: '这是一个笔记本这是一个笔记本这是一个笔记本这是一个笔记本',
  tag: 'CS304-软件工程',
  updateTime: '2021-07-01 00:00:00',
  authorID: 12112628,
  cover: DefaultCover,
  description: 'description',
  isPublic: true,
  likeCount: 30,
  starCount: 50,
  directory: 'directory'
}

</script>
<style scoped>

</style>
