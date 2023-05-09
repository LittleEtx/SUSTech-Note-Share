<template>
<div style="text-align: left">
  <div style="margin-top: 10px">
    <el-text size="large"> <b> 最近打开 </b> </el-text>
    <el-button type="primary">
      <el-icon><CirclePlus /></el-icon> 新建
    </el-button>
  </div>
  <el-space :size="20" wrap style="margin-top: 20px">
    <notebook-display v-for="(notebook, index) in notebooks"
      :key="index" :notebook="notebook"
    ></notebook-display>
  </el-space>
</div>
</template>
<script setup lang="ts">
import NotebookDisplay from '@/components/NotebookCard.vue'
import type { NotebookInfo } from '@/scripts/interfaces'
import { CirclePlus } from '@element-plus/icons-vue'

const notebooks: NotebookInfo[] = []

</script>
<style scoped>

</style>
