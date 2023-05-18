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
        <img-uploader
          style="min-width: 200px"
          :origin-url="notebookCover"
          type="cover" :height="120" :width="200"
          :show-tip="false"
          @change="file => onUploadCover(file as File)"
          v-loading="loadingCover"
          element-loading-background="rgba(255, 255, 255, 0.3)"
        ></img-uploader>
        <div style="margin-left: 20px"></div>
        <div style="display: flex; flex-direction: column; width: 100%">
          <notebook-header
            :can-modify="canModify"
            :notebook="notebook"
            @update="getNotebookInfo(notebookID)"
          ></notebook-header>
          <div style="margin-top: 10px"></div>
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
      <div class="common-layout" v-if="activeSlot === 'note'">
        <el-container style="height: 90vh">
          <el-aside width="200px">
            <el-scrollbar>
              <notebook-file-list
                :can-modify="canModify"
                :notebook-id="notebook?.notebookID"
                @on-select-file="file => onSelectFile(file)"
              ></notebook-file-list>
            </el-scrollbar>
          </el-aside>
          <el-container>
            <el-header>
              <h4 class="text-truncated" style="width: 70%">{{ currentFile?.name }}</h4>
            </el-header>
            <el-main>
              <file-display
                :file="currentFile"
                :can-modify="canModify">
              </file-display>
            </el-main>
          </el-container>
        </el-container>
      </div>
      <div v-else-if="activeSlot === 'comments'">
        <notebook-comment :notebook-id="notebookID"></notebook-comment>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onBeforeMount, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import MainHeader from '@/components/MainHeader.vue'
import DefaultCover from '@/assets/default-file/default-notebook-cover.png'
import type { FileInfo, NotebookInfo } from '@/scripts/interfaces'
import { apiGetBasicInfo, apiUploadNotebookCover } from '@/scripts/API_Notebook'
import NotFoundPage from '@/pages/NotFoundPage.vue'
import { ChatLineSquare, Collection, Setting } from '@element-plus/icons-vue'
import { useStore } from '@/store/store'
import NotebookHeader from '@/components/notebook_page/NotebookHeader.vue'
import ImgUploader from '@/components/ImgUploader.vue'
import NotebookFileList from '@/components/notebook_page/NotebookFileList.vue'
import FileDisplay from '@/components/notebook_page/FileDisplay.vue'
import NotebookComment from '@/components/history_show/NotebookComment.vue'

const route = useRoute()
const store = useStore()

const notebook = ref<NotebookInfo>()
const show404 = ref(false)
const activeSlot = ref('note')
const canModify = ref(false)

const notebookID = computed(() => {
  return route.params.notebookID as string
})

const notebookCover = computed(() => {
  return notebook.value?.cover || DefaultCover
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

watch(
  () => route.params.notebookID,
  async (newID) => {
    await getNotebookInfo(newID as string)
  }
)

onBeforeMount(async () => {
  await getNotebookInfo(notebookID.value)
})

// 上传封面
const loadingCover = ref(false)
const onUploadCover = async (file: File) => {
  loadingCover.value = true
  await apiUploadNotebookCover(notebookID.value, file)
  await getNotebookInfo(notebookID.value)
  loadingCover.value = false
}

const currentFile = ref<FileInfo>()
const onSelectFile = (file: FileInfo) => {
  currentFile.value = file
}
</script>

<style scoped>
.main-container {
  margin-left: 10%;
  margin-right: 10%;
  margin-top: 40px;
}

.text-truncated {
  display: inline-block;
  max-width: 100%;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}
</style>
