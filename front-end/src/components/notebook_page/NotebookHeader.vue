<template>
  <el-space
    direction="vertical"
    :fill="true"
    alignment="start"
    :size="5"
    style="width: 100%; text-align: left"
    v-loading="loading"
    element-loading-background="rgba(255, 255, 255, 0.3)"
  >
  <span>
    <el-text
      style="font-size: 20px"
      v-if="!modifyTitle"
      @click="onModifyTitle"
    >
      <b> {{ notebook?.title }} </b>
    </el-text>
    <el-input
      v-else
      v-model="title"
      ref="titleInputRef"
      placeholder="请输入标题"
      @blur="updateTitle"
      @keyup.enter="updateTitle"
      @keyup.esc="modifyTitle = false"
      :maxlength="40"
      show-word-limit
      style="width: 60%"
    >
    </el-input>
  </span>
    <span>
    <el-tag type="success" effect="dark" v-if="notebook?.isPublic">公开</el-tag>
    <el-tag type="info" effect="dark" v-else>私有</el-tag>
    <el-text size="small" style="font-size: 10px; margin-left: 10px">
      <el-icon> <Clock/> </el-icon>
      上次修改于：{{ notebook?.updateTime }}
    </el-text>
  </span>
    <notebook-tags
      :modify="canModify"
      :tags="getTags"
      @update="tags => updateTags(tags)"
      style="margin-top: 5px"
    ></notebook-tags>
    <div style="margin-top: 5px"></div>
    <div style="font-size: 10px">
      <el-text
        v-if="!modifyDescription"
        size="small" type="info"
        @click="onModifyDescription"
      >
        {{ notebook?.description === '' ? '这个笔记本还没有写简介哦~' : notebook?.description }}
      </el-text>
      <div v-else>
        <el-input
          v-model="description"
          size="small"
          type="textarea"
          maxlength="200"
          placeholder="这个笔记本还没有写简介哦~"
          :autosize="{ minRows: 3 }"
          show-word-limit
        ></el-input>
        <div style="margin-top: 5px"></div>
        <span>
        <el-button
          type="primary" size="small" text
          @click="updateDescription"
        >保存</el-button>
        <el-button
          type="info" size="small" text
          @click="modifyDescription = false"
        >取消</el-button>
      </span>
      </div>

    </div>
  </el-space>
</template>

<script setup lang="ts">
import { Clock } from '@element-plus/icons-vue'
import NotebookTags from '@/components/NotebookTags.vue'
import { apiUpdateBasicInfo } from '@/scripts/API_Notebook'
import { computed, nextTick, ref } from 'vue'
import type { NotebookInfo } from '@/scripts/interfaces'
import { ElInput, ElMessage } from 'element-plus'

interface Props {
  notebook?: NotebookInfo
  canModify: boolean
}

const props = defineProps<Props>()
const emit = defineEmits(['update'])

const loading = ref(false)

// ------------ 更新标签 ----------------
const getTags = computed(() => {
  return props.notebook?.tags ?? []
})

const updateTags = async (newTags: string[]) => {
  if (props.notebook?.tags !== newTags) {
    loading.value = true
    await apiUpdateBasicInfo(
      props.notebook!.notebookID,
      { tags: newTags })
    await emit('update')
    loading.value = false
  }
}

// ------------ 更新标题 ----------------
const titleInputRef = ref<InstanceType<typeof ElInput>>()
const modifyTitle = ref(false)
const title = ref('')
const onModifyTitle = () => {
  if (!props.canModify) {
    return
  }
  modifyTitle.value = true
  nextTick(() => {
    // 注意到 nextTick input 组件才会被渲染出来
    titleInputRef.value!.input!.focus()
  })
  title.value = props.notebook!.title
}
const updateTitle = async () => {
  if (title.value === '') {
    ElMessage.warning('标题不能为空')
  } else {
    loading.value = true
    await apiUpdateBasicInfo(
      props.notebook!.notebookID,
      { title: title.value }
    )
    await emit('update')
    loading.value = false
  }
  modifyTitle.value = false
}

// ------------ 更新简介 ----------------
const modifyDescription = ref(false)
const description = ref('')
const onModifyDescription = () => {
  if (!props.canModify) {
    return
  }
  description.value = props.notebook!.description
  modifyDescription.value = true
}
const updateDescription = async () => {
  loading.value = true
  await apiUpdateBasicInfo(
    props.notebook!.notebookID,
    { description: description.value }
  )
  await emit('update')
  loading.value = false
  modifyDescription.value = false
}
</script>

<style scoped>

</style>
