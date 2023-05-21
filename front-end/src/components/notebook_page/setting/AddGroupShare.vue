<script setup lang="ts">

import UserAvatar from '@/components/UserAvatar.vue'
import { Collection } from '@element-plus/icons-vue'
import type {GroupInfo, NotebookInfo} from '@/scripts/interfaces'
import { computed, ref } from 'vue'
import {apiSearchGroups} from '@/scripts/API_Search'
import { useStore } from '@/store/store'

interface Props {
  notebookInfo: NotebookInfo,
  sharedGroups: GroupInfo[]
  modelValue: boolean
}

const props = defineProps<Props>()

interface Emits {
  (e: 'onSubmit', groupID: number): void

  (e: 'update:modelValue', value: boolean): void
}

const emit = defineEmits<Emits>()

const showDialog = computed({
  get () {
    return props.modelValue
  },
  set (value) {
    emit('update:modelValue', value)
  }
})

const searchNewGroupKey = ref('')
const searchGroups = ref<GroupInfo[]>([])
const selectedGroupID = ref(0)
const store = useStore()
const updateSearchNewGroups = async () => {
  const result = await apiSearchGroups(searchNewGroupKey.value)
  if (!result) {
    searchGroups.value = []
    return
  }
  searchGroups.value = result
      .filter(group => !props.sharedGroups.find(g => g.groupID === group.groupID)) // 不能分享给已经分享的群组
  if (!searchGroups.value.find(group => group.groupID === selectedGroupID.value)) {
    // 如果已经选择的群组不在搜索结果中，就取消选择
    selectedGroupID.value = 0
  }
}

const submitGroupShare = () => {
  emit('onSubmit', selectedGroupID.value)
}

</script>

<template>
  <!--    分享给群组时的弹窗    -->
  <el-dialog
      v-model="showDialog"
      destroy-on-close
  >
    <template #header>
      <div style="text-align: center">
        <el-icon size="30">
          <Collection/>
        </el-icon>
        <br/>
        <el-text style="font-size: 15px"> 分享<b>{{ notebookInfo.title }}</b>至群组</el-text>
      </div>
    </template>
    <el-input
        style="width: 100%"
        placeholder="搜索群组名称或群组ID"
        clearable
        v-model="searchNewGroupKey"
        @update:model-value="updateSearchNewGroups"
    ></el-input>
    <div style="margin-top: 10px"></div>
    <el-scrollbar
        v-if="searchGroups.length > 0"
        style="max-height: 500px"
    >
      <el-radio-group v-model="selectedGroupID" style="width: 100%;">
        <el-space :size="10" direction="vertical" fill style="width: 100%">
          <el-radio
              v-for="group in searchGroups"
              :key="group.groupID"
              :label="group.groupID"
              style="height: 60px"
              border
          >
            <div style="display: flex">
              <div style="margin-left: 10px">
                <el-text type="primary"><b>{{ group.groupName }}</b></el-text>
                <br/>
                <el-text> {{ group.groupID }}</el-text>
              </div>
            </div>
          </el-radio>
        </el-space>
      </el-radio-group>
    </el-scrollbar>
    <div
        v-else
        v-show="searchNewGroupKey !== ''"
        style="width: 100%; text-align: center"
    >
      <el-text>找不到相关群组</el-text>
    </div>
    <template #footer>
      <el-button
          type="primary"
          style="width: 100%"
          :disabled="selectedGroupID === 0"
          @click="submitGroupShare"
      >
        {{ selectedGroupID !== 0 ? '确定' : '请选择要分享的群组' }}
      </el-button>
    </template>
  </el-dialog>
</template>
