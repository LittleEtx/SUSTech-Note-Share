<template>
<div
  style="text-align: left"
  v-loading="loading"
  element-loading-background="rgba(255, 255, 255, 0.3)"
>
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
    :shared-users="sharedUsers"
    :notebook-info="notebookInfo!"
    v-model="showUserShareDialog"
    @on-submit="id => submitUserShare(id)"
  ></add-user-share>
</div>
</template>
<script setup lang="ts">
import { Delete, Search } from '@element-plus/icons-vue'
import { NotebookInfo, UserInfo } from '@/scripts/interfaces'
import { computed, nextTick, onMounted, ref, watch } from 'vue'
import { apiCancelUserShare, apiGetNotebookSharedUsers, apiShareNotebookToUser } from '@/scripts/API_Interact'
import UserAvatar from '@/components/UserAvatar.vue'
import { ElMessageBox } from 'element-plus'
import AddUserShare from '@/components/notebook_page/AddUserShare.vue'

interface Props {
  notebookInfo: NotebookInfo | undefined
  modelValue: UserInfo[]
}

const props = defineProps<Props>()

interface Emit {
  (e: 'update:modelValue', value: UserInfo[]): void
}

const emit = defineEmits<Emit>()
const sharedUsers = computed({
  get () {
    return props.modelValue
  },
  set (value) {
    emit('update:modelValue', value)
  }
})

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
    filteredShareUsers.value = sharedUsers.value
  } else {
    filteredShareUsers.value = sharedUsers.value.filter(user =>
      user.userID.toString().includes(searchShareUsers.value) ||
      user.userName.includes(searchShareUsers.value)
    )
  }
}

const updateInfo = async () => {
  if (!props.notebookInfo) {
    return
  }
  sharedUsers.value = await apiGetNotebookSharedUsers(props.notebookInfo.notebookID)
  await nextTick()
  onUpdateSearchUsers()
}

// ========= 用户分享 =========
const showUserShareDialog = ref(false)
const addUser = () => {
  showUserShareDialog.value = true
}
defineExpose({
  addUser
})

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
</script>

<style scoped>

</style>
