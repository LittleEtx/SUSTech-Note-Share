<template>
  <div
    style="text-align: left"
    v-loading="loading"
    element-loading-background="rgba(255, 255, 255, 0.3)"
  >
    <span style="font-size: 20px"><b>总览</b></span>
    <div style="margin-top: 20px"></div>
    <el-space :size="10">
      <!--    笔记本公开性卡片    -->
      <el-card class="info-card" shadow="hover">
        <template #header>
        <span>
          <el-text size="small">
            {{ notebookInfo?.isPublic ? '公开' : '私有' + '笔记本' }}
          </el-text>
          <el-icon style="float: right"><View/></el-icon>
        </span>
        </template>
        <el-text size="small">
          {{
            notebookInfo?.isPublic ?
                '这是一个公开的笔记本，所有人都可以访问' :
                '这是一个私有的笔记本，只有被你分享给的用户和群组成员才能访问'
          }}
        </el-text>
        <div style="position: absolute; bottom: 15px">
          <el-link type="primary" style="font-size: 10px">修改可见度</el-link>
        </div>
      </el-card>
      <el-card
        class="info-card" shadow="hover"
        v-if="shareUsers.length > 0"
      >
        <template #header>
          <el-text size="small">直接访问</el-text>
          <el-icon style="float: right">
            <Share/>
          </el-icon>
        </template>
        <el-text size="small">
          这个笔记本被分享给了 <br/>
          <span style="font-size: 25px">{{ shareUsers.length }}</span>个用户 <br/>
          他们可以直接访问这个笔记本
        </el-text>
        <div style="position: absolute; bottom: 15px">
          <el-link type="primary" style="font-size: 10px">管理</el-link>
        </div>
      </el-card>
      <el-card
        class="info-card" shadow="hover"
        v-if="shareGroups.length > 0"
      >
        <template #header>
          <el-text size="small">通过群组访问</el-text>
          <el-icon style="float: right">
            <User/>
          </el-icon>
        </template>
        <el-text size="small">
          这个笔记本被分享给了 <br/>
          <span style="font-size: 25px">{{ shareGroups.length }}</span>个群组 <br/>
          群组成员可以访问这个笔记本
        </el-text>
        <div style="position: absolute; bottom: 15px">
          <el-link type="primary" style="font-size: 10px">管理</el-link>
        </div>
      </el-card>
    </el-space>
    <div style="margin-top: 30px"></div>
    <span style="font-size: 20px">
    <b>用户分享</b>
    <el-button type="primary" style="float: right" @click="onShowUserShareDialog">
      <el-icon><Share/></el-icon>添加用户
    </el-button>
  </span>
    <el-divider style="margin-bottom: 0"></el-divider>
    <el-table style="width: 100%" :data="shareUsers">
      <el-table-column type="selection" width="50"/>
      <el-table-column label="全选">
        <template #default="scope">
          <div style="display: flex">
            <user-avatar
              :avatar-url="scope.row.avatar"
              :user-id="scope.row.userID"
            ></user-avatar>
            <div style="margin-left: 10px">
              <el-text type="primary"><b>{{ (scope.row as UserInfo).userName }}</b></el-text>
              <br/>
              <el-text> {{ (scope.row as UserInfo).userID }}</el-text>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column>
        <template #header>
          <el-input
            style="float: right"
            placeholder="搜索用户"
            :prefix-icon="Search"
          ></el-input>
        </template>
        <template #default="scope">
          <el-link
            :underline="false"
            style="float: right"
          >
            <el-icon>
              <Delete/>
            </el-icon>
          </el-link>
        </template>
      </el-table-column>
    </el-table>
    <!--  分享给用户时的弹窗  -->
    <el-dialog
      v-model="showUserShareDialog"
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
        v-model="searchUserKey"
        @update:model-value="updateSearchUsers"
      ></el-input>
      <el-scrollbar
        v-show="searchUsers.length > 0"
        style="max-height: 500px; margin-top: 10px"
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
    <div style="margin-top: 20px"></div>

    <span style="font-size: 20px"><b>群组分享</b></span>
    <div style="margin-top: 20px"></div>

  </div>
</template>
<script setup lang="ts">
import { Collection, Delete, Search, Share, User, View } from '@element-plus/icons-vue'
import { GroupInfo, NotebookInfo, UserInfo } from '@/scripts/interfaces'
import { onMounted, ref, watch } from 'vue'
import { apiGetNotebookSharedGroups, apiGetNotebookSharedUsers, apiShareNotebookToUser } from '@/scripts/API_Interact'
import UserAvatar from '@/components/UserAvatar.vue'
import { apiSearchUsers } from '@/scripts/API_Search'
import { useStore } from '@/store/store'

interface Props {
  notebookInfo: NotebookInfo | undefined
}

const props = defineProps<Props>()

const shareUsers = ref<UserInfo[]>([])
const shareGroups = ref<GroupInfo[]>([])
const loading = ref(false)
watch(
  () => props.notebookInfo,
  async () => {
    loading.value = true
    await updateInfo()
    loading.value = false
  }
)
onMounted(async () => {
  loading.value = true
  await updateInfo()
  loading.value = false
})
const updateInfo = async () => {
  if (!props.notebookInfo) {
    return
  }
  shareUsers.value = await apiGetNotebookSharedUsers(props.notebookInfo.notebookID)
  shareGroups.value = await apiGetNotebookSharedGroups(props.notebookInfo.notebookID)
}

const store = useStore()
// ========= 用户分享 =========
const showUserShareDialog = ref(false)
const searchUserKey = ref('')
const searchUsers = ref<UserInfo[]>([])
const selectedUserID = ref(0)

const onShowUserShareDialog = () => {
  showUserShareDialog.value = true
  searchUserKey.value = ''
  searchUsers.value = []
  selectedUserID.value = 0
}

const updateSearchUsers = async () => {
  searchUsers.value = (await apiSearchUsers(searchUserKey.value))
    .filter(user => user.userID !== store.state.userInfo!.userID) // 不能分享给自己
    .filter(user => !shareUsers.value.find(u => u.userID === user.userID)) // 不能分享给已经分享的用户
  if (!searchUsers.value.find(user => user.userID === selectedUserID.value)) {
    // 如果已经选择的用户不在搜索结果中，就取消选择
    selectedUserID.value = 0
  }
  console.log(searchUsers.value)
}
const submitUserShare = async () => {
  showUserShareDialog.value = false
  console.log(selectedUserID.value)
  loading.value = true
  await apiShareNotebookToUser(props.notebookInfo!.notebookID, selectedUserID.value)
  await updateInfo()
  loading.value = false
}

// TODO: filter user, remove user; group related

</script>

<style scoped>
.info-card {
  width: 200px;
  height: 180px;
  position: relative;
}
</style>
