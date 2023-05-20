<template>
  <div class="common-layout">
    <el-container>
      <el-aside width="200px">Aside</el-aside>
      <el-main style="text-align: left">
        <share-setting :notebook-info="notebookInfo"></share-setting>

      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import type { NotebookInfo } from '@/scripts/interfaces'
import ShareSetting from '@/components/notebook_page/ShareSetting.vue'

interface Props {
  notebookInfo: NotebookInfo | undefined
}

defineProps<Props>()

</script>

<style scoped>

</style>
