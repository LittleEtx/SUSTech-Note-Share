<script setup lang="ts">
import { Delete, MoreFilled, Search } from '@element-plus/icons-vue'
import { onMounted, ref } from 'vue'
import type { NotebookInfo } from '@/scripts/interfaces'
import { apiGetStarredNotebooks } from '@/scripts/API_Center'
import NotebookCard from '@/components/NotebookCard.vue'
import { apiCancelStarNotebook } from '@/scripts/API_Interact'

const searchKey = ref('')
const displayNotebooks = ref<NotebookInfo[]>([])
const starredNotebooks = ref<NotebookInfo[]>([])
const loading = ref(false)
const updateInfo = async () => {
  starredNotebooks.value = await apiGetStarredNotebooks()
  updateKey()
}

const updateKey = () => {
  if (searchKey.value === '') {
    displayNotebooks.value = starredNotebooks.value
  } else {
    displayNotebooks.value = starredNotebooks.value.filter(
      notebook => notebook.title.toLowerCase().includes(searchKey.value.toLocaleLowerCase()) ||
        notebook.description.toLowerCase().includes(searchKey.value.toLocaleLowerCase()) ||
        notebook.tags.some(tag => tag.toLowerCase().includes(searchKey.value.toLocaleLowerCase()))
    )
  }
}

onMounted(async () => {
  loading.value = true
  await updateInfo()
  loading.value = false
})

const deleteStarNotebook = async (notebook: NotebookInfo) => {
  loading.value = true
  await apiCancelStarNotebook(notebook.notebookID)
  await updateInfo()
  loading.value = false
}

</script>

<template>
  <div
    v-loading="loading"
    element-loading-background="rgba(255, 255, 255, 0.3)"
    style="text-align: left"
  >
    <div style="text-align: right">
      <el-text style="float: left; margin-top: 10px">
        共 {{ displayNotebooks.length }} 个{{ searchKey === '' ? '收藏的笔记本' : '搜索结果' }}
      </el-text>
      <el-input
        :prefix-icon="Search"
        style="width: 300px"
        v-model="searchKey"
        @update:modelValue="updateKey"
        clearable
        placeholder="搜索收藏笔记本"
      >
      </el-input>
    </div>

    <div style="margin-top: 20px"></div>
    <el-space
      v-if="displayNotebooks.length > 0"
      :size="20" wrap
    >
      <div
        v-for="(notebook, index) in displayNotebooks"
        :key="index"
        style="position: relative"
      >
        <notebook-card :notebook="notebook"></notebook-card>
        <el-popover trigger="click">
          <template #reference>
            <el-button
              text type="info"
              :icon="MoreFilled"
              style="position: absolute; right: 0; bottom: 0;"
            ></el-button>
          </template>
          <el-button
            style="width: 120px" text
            :icon="Delete" type="danger"
            @click="deleteStarNotebook(notebook)"
          >取消收藏
          </el-button>
        </el-popover>
      </div>
    </el-space>
    <div
      style="text-align: center"
      v-else-if="starredNotebooks.length > 0"
    >
      <el-text>找不到相关的笔记本</el-text>
    </div>
    <el-empty v-else description="你还没有收藏任何笔记哟~"></el-empty>
  </div>

</template>
