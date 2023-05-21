<template>
<div style="text-align: left" ref="containerRef">
  <div style="margin-top: 10px">
    <el-text size="large"><b> 最近打开 </b></el-text>
  </div>
  <div style="margin-top: 20px"></div>
  <el-space :size="20" wrap>
    <notebook-card
      v-for="(notebook, index) in recentNotebooks"
      :key="index" :notebook="notebook"
    ></notebook-card>
  </el-space>

</div>
</template>
<script setup lang="ts">
import NotebookCard from '@/components/NotebookCard.vue'
import type { NotebookInfo } from '@/scripts/interfaces'
import { onBeforeMount, ref, watch } from 'vue'
import { apiGetHistory } from '@/scripts/API_HIstory'
import { apiGetBasicInfo } from '@/scripts/API_Notebook'

const recentNotebooks = ref<NotebookInfo[]>([])
const containerRef = ref<InstanceType<typeof HTMLElement>>()
watch(
  // set the count of recent notebooks according to the width of the screen
  () => window.screenX,
  async () => {
    const count = window.screenX * 0.8 -
      await updateInfo()
  }
)

const updateInfo = async () => {
  recentNotebooks.value = []
  const histories = await apiGetHistory(0, 10)
  for (const history of histories) {
    const notebook = await apiGetBasicInfo(history.notebookID)
    recentNotebooks.value.push(notebook)
  }
}

onBeforeMount(async () => {
  await updateInfo()
})

</script>
<style scoped>

</style>
