<script setup lang="ts">
import { Delete, MoreFilled } from '@element-plus/icons-vue'
import { apiGetSharedNotebook } from '@/scripts/API_Center'
import type { NotebookInfo, UserInfo } from '@/scripts/interfaces'
import { onBeforeMount, ref } from 'vue'
import { apiGetUserInfo } from '@/scripts/API_User'
import UserAvatar from '@/components/UserAvatar.vue'
import NotebookCard from '@/components/NotebookCard.vue'
import { ElMessageBox } from 'element-plus'
import { apiCancelUserShare } from '@/scripts/API_Interact'
import { useStore } from '@/store/store'

const notebookByUsers = ref<Map<string, NotebookInfo[]>>(new Map())
const users = ref<Map<string, UserInfo>>(new Map())
const loading = ref<boolean>(false)
const selectedUser = ref<string>('')
const updateNotebook = async () => {
  notebookByUsers.value.clear()
  const notebookInfos = await apiGetSharedNotebook()
  for (const notebookInfo of notebookInfos) {
    const id = notebookInfo.authorID
    const author = id.toString()
    if (notebookByUsers.value.has(author)) {
      notebookByUsers.value.get(author)!.push(notebookInfo)
    } else {
      notebookByUsers.value.set(author, [notebookInfo])
    }
    // 拉取user信息
    if (!users.value.has(author)) {
      users.value.set(author, await apiGetUserInfo(id))
    }
  }
}

onBeforeMount(async () => {
  loading.value = true
  await updateNotebook()
  selectedUser.value = notebookByUsers.value.keys().next().value ?? 0
  loading.value = false
})

const store = useStore()
const deleteShareNotebook = async (notebook: NotebookInfo) => {
  try {
    await ElMessageBox.confirm('确定要删除这个分享给你的笔记本吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
  } catch (e) {
    return
  }
  loading.value = true
  await apiCancelUserShare(notebook.notebookID, store.state.userInfo!.userID)
  await updateNotebook()
  loading.value = false
}

</script>

<template>
  <div v-loading="loading" element-loading-background="rgba(255, 255, 255, 0.3)">
    <el-container v-if="notebookByUsers.size > 0">
      <el-aside width="180px">
        <!--   左侧边栏展示所有文件夹   -->
        <el-menu
          class="el-menu-vertical-demo"
          :default-active="selectedUser"
          @open="selectedUser = $event"
          @select="selectedUser = $event"
        >
          <el-menu-item
            v-for="user in users.values()"
            :key="user.userID"
            :index="user.userID.toString()"
            class="dir-item-menu"
          >
            <div style="max-width: 150px; display: flex; align-items: center">
              <user-avatar
                style="min-width: 40px"
                :avatar-url="user.avatar"
                :user-id="user.userID"
              ></user-avatar>
              <div style="margin-left: 10px; display: block; line-height: normal; text-align: left"
                   class="text-truncated">
                <span style="height: 20px; font-size: 15px"><b>{{ user.userName }}</b></span> <br>
                <el-text style="height: 10px; font-size: 10px"> {{ user.userID }}</el-text>
              </div>
            </div>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main style="text-align: left">
        <!--   右侧展示所有卡片   -->
        <el-space
          v-if="notebookByUsers.get(selectedUser)?.length > 0"
          :size="20" wrap
          style="padding-left: 20px"
        >
          <div
            v-for="(notebook, index) in notebookByUsers.get(selectedUser)"
            :key="index"
            style="position: relative"
          >
            <notebook-card :notebook="notebook"></notebook-card>
            <el-popover trigger="click">
              <template #reference>
                <el-button
                  text type="info"
                  :icon="MoreFilled"
                  style="position: absolute; right: 0; bottom: 0;"
                ></el-button>
              </template>
              <el-button
                style="width: 120px" text
                :icon="Delete" type="danger"
                @click="deleteShareNotebook(notebook)"
              >取消分享
              </el-button>
            </el-popover>
          </div>
        </el-space>
        <el-empty v-else description="没有笔记本可以展示"></el-empty>
      </el-main>
    </el-container>
    <el-empty v-else description="还有没用户分享笔记本给你哟~"></el-empty>
  </div>

</template>

<style scoped>

</style>