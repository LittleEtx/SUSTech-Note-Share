<template>
  <div
    v-infinite-scroll="load"
    class="infinite-list"
    style="overflow: auto; text-align: left"
  >
    <template v-if="historyList.length > 0">
      <el-timeline>
        <el-timeline-item
          v-for="history in historyList"
          :key="history.notebook.notebookID"
          :timestamp="history.visitTime"
          placement="top"
        >
          <el-card style="height: 120px" :body-style="{ padding: '0px' }" shadow="hover">
            <div style="display: flex; align-items: center; ">
              <div
                @click="$router.push({ name: 'notebook', params: { notebookID: history.notebook.notebookID }})"
                style="min-width: 200px; max-width: 200px; height: 120px; cursor: pointer"
              >
                <el-image
                  style="height: 120px; width: 200px"
                  :src="history.notebook.cover === null ? DefaultCover : history.notebook.cover"
                >
                  <template #error>
                    <img
                      style="height: 120px; width: 200px"
                      :src="DefaultCover" class="cover" alt=""
                    />
                  </template>
                </el-image>
              </div>
              <div style="margin-left: 20px"></div>
              <div style="display: grid">
                <el-text truncated>
                  <b> {{ history.notebook.title }} </b>
                </el-text>
                <span class="text-truncated" style="font-size: 10px; margin-top: 5px">
                  {{ history.notebook.description }}
                </span>
                <span class="text-truncated" style="margin-top: 3px">
                  <el-tag size="small" type="success" v-if="history.notebook.isPublic">公开</el-tag>
                  <el-tag size="small" type="info" v-else>私有</el-tag>
                  <el-tag
                    size="small"
                    v-for="tag in history.notebook.tags" style="margin-left: 5px"
                    :key="tag"
                  >{{ tag }}</el-tag>
                </span>
                <div style="display: flex; align-items: center; margin-top: 10px">
                  <el-link
                    :underline="false"
                    @click="$router.push({ name: 'user', params: { userID: history.author.userID }})"
                  >
                    <user-avatar
                      :avatar-url="history.author.avatar"
                      :user-id="history.author.userID"
                      :size="20"
                    ></user-avatar>
                    <span style="margin-left: 5px"><b> {{ history.author.userName }} </b></span>
                  </el-link>

                  <el-text size="small" style="margin-left: 15px">
                    <el-icon>
                      <Clock/>
                    </el-icon>
                    {{ history.notebook.updateTime }}
                  </el-text>
                </div>
              </div>
              <!--     占位符     -->
              <div style="min-width: 80px"></div>
              <el-link
                style="position: absolute; right: 30px;"
                type="danger" :underline="false"
                @click="deleteHistory(history.notebook.notebookID)"
              >
                <el-icon>
                  <Delete/>
                </el-icon>
              </el-link>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
      <div v-if="reachEnd" style="text-align: center">
        <el-text>已经到底了~</el-text>
      </div>

    </template>
    <el-empty v-else description="你还没有任何历史记录"></el-empty>
  </div>

</template>

<script setup lang="ts">

import { onBeforeMount, ref } from 'vue'
import type { NotebookInfo, UserInfo } from '@/scripts/interfaces'
import { apiDeleteAllHistory, apiDeleteHistory, apiGetHistory } from '@/scripts/API_HIstory'
import { apiGetBasicInfo } from '@/scripts/API_Notebook'
import { apiGetUserInfo } from '@/scripts/API_User'
import DefaultCover from '@/assets/default-file/default-notebook-cover.png'
import { Clock, Delete } from '@element-plus/icons-vue'
import UserAvatar from '@/components/UserAvatar.vue'

interface Item {
  visitTime: string
  notebook: NotebookInfo
  author: UserInfo
}

const historyList = ref<Item[]>([])
const user = ref<Map<number, UserInfo>>(new Map())
const reachEnd = ref(false)
const load = () => {
  updateList(20)
}

const updateLock = ref(false) // 加锁以保证列表的更新不会重复进行
const updateList = async (count: number) => {
  if (reachEnd.value || updateLock.value) return
  updateLock.value = true
  const newItems = await apiGetHistory(historyList.value.length, count)
  if (newItems.length === 0) {
    reachEnd.value = true
    updateLock.value = false
    return
  }
  for (const item of newItems) {
    try {
      const notebook = await apiGetBasicInfo(item.notebookID)
      const authorID = notebook.authorID
      // 更新用户信息
      if (!user.value.has(authorID)) {
        const author = await apiGetUserInfo(authorID)
        user.value.set(authorID, author)
      }
      historyList.value.push({
        visitTime: item.visitTime,
        notebook,
        author: user.value.get(authorID)!
      })
    } catch (e) {
    }
  }
  updateLock.value = false
}

onBeforeMount(async () => {
  await updateList(20)
})

const deleteHistory = async (notebookID: string) => {
  await apiDeleteHistory(notebookID)
  historyList.value = historyList.value.filter(item => item.notebook.notebookID !== notebookID)
}

const clearAll = async () => {
  historyList.value = []
  await apiDeleteAllHistory()
}

defineExpose({
  clearAll
})
</script>
