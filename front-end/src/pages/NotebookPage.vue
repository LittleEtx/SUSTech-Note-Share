<template>
  <div v-if="show404">
    <not-found-page></not-found-page>
  </div>
  <div v-else>
    <div class="header">
      <main-header/>
    </div>
    <div class="main-container">
      <div style="display: flex; flex-direction: row; width: 100%">
        <el-image :src="notebook?.cover!" class="cover">
          <template #error>
            <img :src="DefaultCover" class="cover" alt=""/>
          </template>
        </el-image>
        <div style="margin-left: 20px"></div>
        <div style="text-align: left; width: 100%">
          <span>
            <el-text style="font-size: 20px; vertical-align: center">
              <b> {{ notebook?.title }} </b>
            </el-text>
            <el-tag type="success" v-if="notebook?.isPublic">公开</el-tag>
            <el-tag type="info" v-else>私有</el-tag>
          </span>

          <div style="margin-top: 5px; font-size: 10px">
            <el-text size="small">
              <el-icon>
                <Clock/>
              </el-icon>
              上次修改于：{{ notebook?.updateTime }}
            </el-text>
          </div>
          <div style="margin-top: 5px">
            <notebook-tags
              :modify="canModify"
              :tags="getTags"
              @update="tags => updateTags(tags)"
            ></notebook-tags>
          </div>
          <el-text size="small" type="info">
            {{ notebook?.description }}
          </el-text>
          <!--    按钮组    -->
          <el-tabs v-model="activeSlot">
            <el-tab-pane name="note">
              <template #label>
                <span><el-icon><Collection/></el-icon> <b>笔记</b> </span>
              </template>
            </el-tab-pane>
            <el-tab-pane name="comments">
              <template #label>
                <span><el-icon><ChatLineSquare/></el-icon> <b>评论</b> </span>
              </template>
            </el-tab-pane>
            <el-tab-pane name="setting">
              <template #label>
                <span><el-icon><Setting/></el-icon> <b>设置</b> </span>
              </template>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onBeforeMount, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import MainHeader from '@/components/MainHeader.vue'
import DefaultCover from '@/assets/default-file/default-notebook-cover.png'
import type { NotebookInfo } from '@/scripts/interfaces'
import { apiGetBasicInfo, apiUpdateBasicInfo } from '@/scripts/API_Notebook'
import NotFoundPage from '@/pages/NotFoundPage.vue'
import { ChatLineSquare, Clock, Collection, Setting } from '@element-plus/icons-vue'
import { useStore } from '@/store/store'
import NotebookTags from '@/components/NotebookTags.vue'

const route = useRoute()
const store = useStore()

const notebook = ref<NotebookInfo>()
const show404 = ref(false)
const activeSlot = ref('note')
const canModify = ref(false)

const getTags = computed(() => {
  return notebook.value?.tags ?? []
})

const notebookID = computed(() => {
  return route.params.notebookID as string
})

const getNotebookInfo = async (notebookID: string) => {
  try {
    notebook.value = await apiGetBasicInfo(notebookID)
    canModify.value = notebook.value?.authorID === store.state.userInfo?.userID
    show404.value = false
  } catch (e) {
    // 若笔记本不存在或用户无权访问，则显示 404 页面
    if (e.response?.status === 404) {
      show404.value = true
    }
  }
}

// 更新标签
const updateTags = async (newTags: string[]) => {
  if (notebook.value?.tags !== newTags) {
    await apiUpdateBasicInfo(notebookID.value, { tags: newTags })
    await getNotebookInfo(notebookID.value)
  }
}

watch(
  () => route.params.notebookID,
  (newID) => {
    getNotebookInfo(newID as string)
  }
)

onBeforeMount(() => {
  getNotebookInfo(notebookID.value)
})

</script>

<style scoped>
.main-container {
  display: flex;
  flex-direction: row;
  justify-content: left;
  margin-left: 10%;
  margin-right: 10%;
  margin-top: 40px;
}

.cover {
  width: 200px;
  height: 120px;
  min-width: 200px;
  border-radius: 10px;
}
</style>
