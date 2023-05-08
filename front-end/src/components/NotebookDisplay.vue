<template>
<el-card shadow="hover" class="card-size"
         :body-style="{ padding: '0px' }">
  <img :src="notebook.cover" alt="" class="cover">
  <div style="padding: 8px 10px 0 10px; text-align: left;">
    <el-text truncated>
      <b> {{ notebook.title }} </b>
    </el-text>
    <div style="margin-top: 5px; font-size: 10px">
      <el-text style="vertical-align: center" size="small">
          <el-tag size="small" v-if="notebook.isPublic">Public</el-tag>
          <el-tag size="small" type="info" v-else>Private</el-tag>
          {{ notebook.tag }}
      </el-text>
      <br />
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
interface Props {
  notebook: NotebookInfo
}

const props = defineProps<Props>()

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
