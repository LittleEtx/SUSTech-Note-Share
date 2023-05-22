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
        <div style="min-width: 200px">
          <img-uploader
            v-if="canModify"
            style="min-width: 200px"
            :origin-url="notebookCover"
            type="cover" :height="120" :width="200"
            :show-tip="false"
            @change="file => onUploadCover(file as File)"
            v-loading="loadingCover"
            element-loading-background="rgba(255, 255, 255, 0.3)"
          ></img-uploader>
          <el-image v-else style="height: 120px; width: 200px; border-radius: 10%" :src="notebookCover" alt=""/>
        </div>
        <div style="margin-left: 20px"></div>
        <div style="display: flex; flex-direction: column; width: 100%">
          <notebook-header
            :can-modify="canModify"
            :notebook="notebook"
            @update="getNotebookInfo(notebookID)"
          ></notebook-header>
          <div style="margin-top: 10px"></div>
          <!--    按钮组    -->
          <el-affix>
            <el-tabs
              v-model="activeSlot"
              style="background-color: white"
              @update:model-value="slot => updateRenderList(slot as string)"
            >
              <el-tab-pane name="note">
                <template #label>
                  <span><el-icon><Collection/></el-icon> <b>笔记</b> </span>
                </template>
              </el-tab-pane>
              <el-tab-pane name="comments" v-if="notebook?.isPublic">
                <template #label>
                  <span><el-icon><ChatLineSquare/></el-icon> <b>评论</b> </span>
                </template>
              </el-tab-pane>
              <el-tab-pane name="setting" v-if="canModify">
                <template #label>
                  <span><el-icon><Setting/></el-icon> <b>设置</b> </span>
                </template>
              </el-tab-pane>
            </el-tabs>
          </el-affix>
        </div>
      </div>
      <notebook-edit-pane
        v-show="activeSlot === 'note'"
        :notebook-id="notebookID"
        :can-modify="canModify"
      ></notebook-edit-pane>
      <notebook-comment
        v-if="notebook?.isPublic && lazyRenderList.includes('comments')"
        v-show="activeSlot === 'comments'"
        :notebook-id="notebookID"
      ></notebook-comment>
      <notebook-setting
        v-if="canModify && lazyRenderList.includes('setting')"
        v-show="activeSlot === 'setting'"
        :notebook-info="notebook"
        @on-update-notebook-visibility="getNotebookInfo(notebookID)"
      ></notebook-setting>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onBeforeMount, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import MainHeader from '@/components/MainHeader.vue'
import DefaultCover from '@/assets/default-file/default-notebook-cover.png'
import type { NotebookInfo } from '@/scripts/interfaces'
import { apiGetBasicInfo, apiUploadNotebookCover } from '@/scripts/API_Notebook'
import NotFoundPage from '@/pages/NotFoundPage.vue'
import { ChatLineSquare, Collection, Setting } from '@element-plus/icons-vue'
import { useStore } from '@/store/store'
import NotebookHeader from '@/components/notebook_page/NotebookHeader.vue'
import ImgUploader from '@/components/ImgUploader.vue'
import NotebookComment from '@/components/history_show/NotebookComment.vue'
import NotebookEditPane from '@/components/notebook_page/NotebookEditPane.vue'
import NotebookSetting from '@/components/notebook_page/setting/NotebookSetting.vue'
import { apiAddHistory } from '@/scripts/API_HIstory'

const route = useRoute()
const store = useStore()

const notebook = ref<NotebookInfo>()
const show404 = ref(false)
const activeSlot = ref('note')
const canModify = ref(false)
const lazyRenderList = ref<string[]>(['note'])

const updateRenderList = (slot: string) => {
  if (!lazyRenderList.value.includes(slot)) {
    lazyRenderList.value.push(slot)
  }
}

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
    await apiAddHistory(newID as string)
  }
)

onBeforeMount(async () => {
  await getNotebookInfo(notebookID.value)
  await apiAddHistory(notebookID.value)
})

// 上传封面
const loadingCover = ref(false)
const onUploadCover = async (file: File) => {
  loadingCover.value = true
  await apiUploadNotebookCover(notebookID.value, file)
  await getNotebookInfo(notebookID.value)
  loadingCover.value = false
}

</script>

<style scoped>
.main-container {
  margin-left: 10%;
  margin-right: 10%;
  margin-top: 40px;
}

</style>
