<template>
<div v-loading="loading" element-loading-background="rgba(255, 255, 255, 0.3)">
  <div style="text-align: right">
    <el-button type="primary">
      <el-icon><CirclePlus /></el-icon> 新建笔记本
    </el-button>
  </div>
  <el-row style="margin-top: 20px">
    <el-col :span="4">
      <el-menu
        class="el-menu-vertical-demo"
        :default-active="notebookByDirs.keys().next().value"
        @open="selectedDir = $event"
        @select="selectedDir = $event"
      >
        <el-menu-item
          v-for="(dir, index) in notebookByDirs.keys()"
          :key="index"
          :index="dir"
        >
          <el-icon><Folder /></el-icon>
          <el-text truncated> {{ dir }} </el-text>
        </el-menu-item>
      </el-menu>
    </el-col>
    <el-col :span="20">
      <el-space :size="20" wrap style="width: 100%; padding-left: 20px">
        <notebook-display v-for="(notebook, index) in notebookByDirs.get(selectedDir)"
          :key="index" :notebook="notebook"
        ></notebook-display>
      </el-space>
    </el-col>
  </el-row>
</div>
</template>

<script setup lang="ts">
import { NotebookInfo } from '@/scripts/interfaces'
import { onBeforeMount, ref } from 'vue'
import NotebookDisplay from '@/components/NotebookCard.vue'
import { apiGetUserNotebooks } from '@/scripts/API_Center'
import { CirclePlus, Folder } from '@element-plus/icons-vue'

const notebookByDirs = ref<Map<string, NotebookInfo[]>>(new Map())
const selectedDir = ref<string>('')
// loading before data is loaded
const loading = ref<boolean>(true)
onBeforeMount(async () => {
  const notebookInfos = await apiGetUserNotebooks()
  notebookInfos.forEach((notebookInfo) => {
    const dir = notebookInfo.directory
    if (notebookByDirs.value.has(dir)) {
      notebookByDirs.value.get(dir)!.push(notebookInfo)
    } else {
      notebookByDirs.value.set(dir, [notebookInfo])
    }
  })
  loading.value = false
})

</script>

<style scoped>

</style>
