<script setup lang="ts">
import MainHeader from '@/components/MainHeader.vue'
import { onBeforeMount, ref, watch } from 'vue'
import NotebookCard from '@/components/NotebookCard.vue'
import type { NotebookInfo } from '@/scripts/interfaces'
import { apiSearchNotebooks } from '@/scripts/API_Search'
import { Search } from '@element-plus/icons-vue'

interface Props {
  query: string
}

const props = defineProps<Props>()
const searchKey = ref('')
const relatedNotebooks = ref<NotebookInfo[]>([])
watch(() => props.query, async (newVal) => {
  searchKey.value = newVal
  relatedNotebooks.value = await apiSearchNotebooks(searchKey.value, 0, 30)
})
onBeforeMount(async () => {
  searchKey.value = props.query
  relatedNotebooks.value = await apiSearchNotebooks(searchKey.value, 0, 30)
})

</script>

<template>
  <el-affix>
    <div class="header">
      <main-header :show-search-bar="false"></main-header>
    </div>
  </el-affix>
  <div
    style="margin-left: 10%; margin-right: 10%; margin-top: 60px;
    display: flex; flex-direction: column; align-items: center;"
  >
    <el-text style="font-size: 20px"><b>搜索公开笔记库</b></el-text>
    <div style="margin-top: 10px"></div>
    <el-input
      v-model="searchKey"
      style="width: 500px"
      @change="$router.push('/search?q=' + searchKey)"
      :prefix-icon="Search"
    ></el-input>
    <div style="margin-top: 40px"></div>
    <el-space :size="20" wrap v-if="relatedNotebooks.length > 0">
      <notebook-card
        v-for="(notebook, index) in relatedNotebooks"
        :key="index" :notebook="notebook"
      ></notebook-card>
    </el-space>
    <el-empty v-else description="找不到相关的笔记"></el-empty>
  </div>
</template>

<style scoped>

</style>
