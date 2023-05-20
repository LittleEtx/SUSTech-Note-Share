<template>
  <div
    style="text-align: left"
    v-loading="loading"
    element-loading-background="rgba(255, 255, 255, 0.3)"
  >
    <span style="font-size: 20px"><b>总览</b></span>
    <div style="margin-top: 20px"></div>
    <el-space :size="10" wrap>
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
    <el-button type="primary" style="float: right" @click="showUserShareDialog = true">
      <el-icon><Share/></el-icon>添加用户
    </el-button>
  </span>
    <el-divider style="margin-bottom: 0"></el-divider>
    <!--   展示已经分享给的用户的表格    -->
    <el-table
        style="width: 100%"
        :data="filteredShareUsers"
        @selection-change="onUserSelectionChange"
    >
      <el-table-column type="selection" width="50"/>
      <el-table-column>
        <template #header>
          <span v-if="!showUserMultiDelete">全选</span>
          <el-button v-else type="danger" plain @click="deleteMultiUserShare">
            删除{{ multiSelectUsers.length }}个用户的分享
          </el-button>
        </template>
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
              v-model="searchShareUsers"
              @update:model-value="onUpdateSearchUsers"
          ></el-input>
        </template>
        <template #default="scope">
          <el-link
              :underline="false"
              style="float: right"
              @click="deleteUserShare(scope.row)"
          >
            <el-icon>
              <Delete/>
            </el-icon>
          </el-link>
        </template>
      </el-table-column>
    </el-table>
    <add-user-share
        v-if="showUserShareDialog"
        :shared-users="shareUsers"
        :notebook-info="notebookInfo!"
        v-model="showUserShareDialog"
        @on-submit="id => submitUserShare(id)"
    ></add-user-share>
    <div style="margin-top: 20px"></div>

    <span style="font-size: 20px"><b>群组分享</b></span>
    <div style="margin-top: 20px"></div>

  </div>
</template>
<script setup lang="ts">
import { Delete, Search, Share, User, View } from '@element-plus/icons-vue'
import { GroupInfo, NotebookInfo, UserInfo } from '@/scripts/interfaces'
import { onMounted, ref, watch } from 'vue'
import {
  apiCancelUserShare,
  apiGetNotebookSharedGroups,
  apiGetNotebookSharedUsers,
  apiShareNotebookToUser
} from '@/scripts/API_Interact'
import UserAvatar from '@/components/UserAvatar.vue'
import { ElMessageBox } from 'element-plus'
import AddUserShare from '@/components/notebook_page/AddUserShare.vue'

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

const searchShareUsers = ref('')
const filteredShareUsers = ref<UserInfo[]>([])

const onUpdateSearchUsers = () => {
  if (searchShareUsers.value === '') {
    filteredShareUsers.value = shareUsers.value
  } else {
    filteredShareUsers.value = shareUsers.value.filter(user =>
        user.userID.toString().includes(searchShareUsers.value) ||
        user.userName.includes(searchShareUsers.value)
    )
  }
}

const updateInfo = async () => {
  if (!props.notebookInfo) {
    return
  }
  shareUsers.value = await apiGetNotebookSharedUsers(props.notebookInfo.notebookID)
  shareGroups.value = await apiGetNotebookSharedGroups(props.notebookInfo.notebookID)
  onUpdateSearchUsers()
}

// ========= 用户分享 =========
const showUserShareDialog = ref(false)
const submitUserShare = async (selectedUserID: number) => {
  showUserShareDialog.value = false
  loading.value = true
  await apiShareNotebookToUser(props.notebookInfo!.notebookID, selectedUserID)
  await updateInfo()
  loading.value = false
}

const deleteUserShare = async (user: UserInfo) => {
  try {
    await ElMessageBox.confirm(`确定要取消分享给用户 ${user.userName} 吗？`, '确认', {
      confirmButtonText: '确定',
      confirmButtonClass: 'el-button--danger',
      cancelButtonText: '取消',
      type: 'warning'
    })
  } catch (e) {
    return
  }
  loading.value = true
  await apiCancelUserShare(props.notebookInfo!.notebookID, user.userID)
  await updateInfo()
  loading.value = false
}

const multiSelectUsers = ref<UserInfo[]>([])
const showUserMultiDelete = ref(false)
const onUserSelectionChange = (users: UserInfo[]) => {
  multiSelectUsers.value = users
  showUserMultiDelete.value = users.length > 0
}

const deleteMultiUserShare = async () => {
  try {
    await ElMessageBox.confirm(`确定要取消分享给这 ${multiSelectUsers.value.length} 个用户吗？`, '确认', {
      confirmButtonText: '确定',
      confirmButtonClass: 'el-button--danger',
      cancelButtonText: '取消',
      type: 'warning'
    })
  } catch (e) {
    return
  }
  loading.value = true
  await Promise.all(multiSelectUsers.value
      .map(user => apiCancelUserShare(props.notebookInfo!.notebookID, user.userID)))
  await updateInfo()
  loading.value = false
}

// TODO: group related

</script>

<style scoped>
.info-card {
  width: 200px;
  height: 180px;
  position: relative;
}
</style>
