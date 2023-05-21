<template>
<div style="text-align: left">
  <div style="margin-top: 10px">
    <el-text size="large"> <b> 最近打开 </b> </el-text>
  </div>
  <el-space :size="20" wrap>
    <notebook-card v-for="(notebook, index) in notebooks"
                   :key="index" :notebook="notebook"
    ></notebook-card>
  </el-space>
</div>
</template>
<script setup lang="ts">
import NotebookCard from '@/components/NotebookCard.vue'
import type { NotebookInfo } from '@/scripts/interfaces'

const notebooks: NotebookInfo[] = []

const updateInfo = async () => {

}

</script>
<style scoped>

</style>
