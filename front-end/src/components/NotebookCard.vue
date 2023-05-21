<template>
<el-card
  shadow="hover" class="card-size"
  :body-style="{ padding: '0px' }"
  style="cursor: pointer"
  @click="$router.push({ name: 'notebook', params: { notebookID: notebook.notebookID }})"
>
  <el-image :src="notebook.cover === null ? DefaultCover : notebook.cover" class="cover">
    <template #error>
      <img :src="DefaultCover" class="cover" alt=""/>
    </template>
  </el-image>
  <div style="padding: 8px 10px 0 10px; text-align: left;">
    <el-text truncated>
      <b> {{ notebook.title }} </b>
    </el-text>
    <div style="margin-top: 5px; font-size: 10px">
      <el-text style="vertical-align: center" size="small">
        <el-tag size="small" type="success" v-if="notebook.isPublic">公开</el-tag>
        <el-tag size="small" type="info" v-else>私有</el-tag>
        {{ notebook.tags.length > 0 ? notebook.tags[0] : '' }}
      </el-text>
      <div style="height: 3px"></div>
      <el-text size="small">
        <el-icon><Clock /></el-icon>
        {{ notebook.updateTime }}
      </el-text>
    </div>
  </div>
</el-card>
</template>

<script setup lang="ts">

import type { NotebookInfo } from '@/scripts/interfaces'
import { Clock } from '@element-plus/icons-vue'
import DefaultCover from '@/assets/default-file/default-notebook-cover.png'

interface Props {
  notebook: NotebookInfo
}
defineProps<Props>()
</script>

<style scoped>
.card-size {
  width: 200px;
  height: 200px;
}

.cover {
  width: 100%;
  height: 120px;
  object-fit: cover;
}
</style>
