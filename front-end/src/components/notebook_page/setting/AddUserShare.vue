<script setup lang="ts">

import UserAvatar from '@/components/UserAvatar.vue'
import { Collection } from '@element-plus/icons-vue'
import type { NotebookInfo } from '@/scripts/interfaces'
import { UserInfo } from '@/scripts/interfaces'
import { computed, ref } from 'vue'
import { apiSearchUsers } from '@/scripts/API_Search'
import { useStore } from '@/store/store'

interface Props {
  notebookInfo: NotebookInfo,
  sharedUsers: UserInfo[]
  modelValue: boolean
}

const props = defineProps<Props>()

interface Emits {
  (e: 'onSubmit', userID: number): void

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

const searchNewUserKey = ref('')
const searchUsers = ref<UserInfo[]>([])
const selectedUserID = ref(0)
const store = useStore()
const updateSearchNewUsers = async () => {
  const result = await apiSearchUsers(searchNewUserKey.value)
  console.log(result)
  if (!result) {
    searchUsers.value = []
    return
  }
  searchUsers.value = result
    .filter(user => user.userID !== store.state.userInfo!.userID) // 不能分享给自己
    .filter(user => !props.sharedUsers.find(u => u.userID === user.userID)) // 不能分享给已经分享的用户
  if (!searchUsers.value.find(user => user.userID === selectedUserID.value)) {
    // 如果已经选择的用户不在搜索结果中，就取消选择
    selectedUserID.value = 0
  }
}

const submitUserShare = () => {
  emit('onSubmit', selectedUserID.value)
}

</script>

<template>
  <!--    分享给用户时的弹窗    -->
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
        <el-text style="font-size: 15px"> 分享<b>{{ notebookInfo.title }}</b>至用户</el-text>
      </div>
    </template>
    <el-input
        style="width: 100%"
        placeholder="搜索用户名、学号或邮箱"
        clearable
        v-model="searchNewUserKey"
        @update:model-value="updateSearchNewUsers"
    ></el-input>
    <div style="margin-top: 10px"></div>
    <el-scrollbar
        v-if="searchUsers.length > 0"
        style="max-height: 500px"
    >
      <el-radio-group v-model="selectedUserID" style="width: 100%;">
        <el-space :size="10" direction="vertical" fill style="width: 100%">
          <el-radio
              v-for="user in searchUsers"
              :key="user.userID"
              :label="user.userID"
              style="height: 60px"
              border
          >
            <div style="display: flex">
              <user-avatar
                  :avatar-url="user.avatar"
                  :user-id="user.userID"
              ></user-avatar>
              <div style="margin-left: 10px">
                <el-text type="primary"><b>{{ user.userName }}</b></el-text>
                <br/>
                <el-text> {{ user.userID }}</el-text>
              </div>
            </div>
          </el-radio>
        </el-space>
      </el-radio-group>
    </el-scrollbar>
    <div
        v-else
        v-show="searchNewUserKey !== ''"
        style="width: 100%; text-align: center"
    >
      <el-text>找不到相关用户</el-text>
    </div>
    <template #footer>
      <el-button
          type="primary"
          style="width: 100%"
          :disabled="selectedUserID === 0"
          @click="submitUserShare"
      >
        {{ selectedUserID !== 0 ? '确定' : '请选择要分享的用户' }}
      </el-button>
    </template>
  </el-dialog>
</template>
