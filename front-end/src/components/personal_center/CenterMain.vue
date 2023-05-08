<template>
<div style="text-align: left">

  <div style="margin-top: 10px">
    <el-text size="large"> <b> 最近打开 </b> </el-text>
    <el-button type="primary">
      <el-icon><CirclePlus /></el-icon> 新建
    </el-button>
  </div>
  <el-space :size="20" wrap style="margin-top: 20px">
    <notebook-display :notebook="notebook"></notebook-display>
    <notebook-display :notebook="notebook"></notebook-display>
    <notebook-display :notebook="notebook"></notebook-display>
    <notebook-display :notebook="notebook"></notebook-display>
  </el-space>
</div>
</template>
<script setup lang="ts">
import NotebookDisplay from '@/components/NotebookCard.vue'
import type { NotebookInfo } from '@/scripts/interfaces'
import DefaultCover from '@/assets/default-file/default-notebook-cover.png'
import { CirclePlus } from '@element-plus/icons-vue'

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
.notebook-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: center;
  align-content: center;
}
</style>
