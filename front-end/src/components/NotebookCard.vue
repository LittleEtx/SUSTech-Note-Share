<template>
<el-card
  shadow="hover" class="card-size"
  :body-style="{ padding: '0px' }"
  style="cursor: pointer"
  @click="$router.push({ name: 'notebook', params: { notebookID: notebook.notebookID }})"
>
  <el-image :src="notebook.cover === null ? DefaultCover : notebook.cover" class="cover">
    <template #error>
      <img :src="DefaultCover" class="cover" alt=""/>
    </template>
  </el-image>
  <div style="padding: 5px 10px 0 10px; text-align: left;">
    <el-text truncated>
      <b> {{ notebook.title }} </b>
    </el-text>
    <div style="margin-top: 3px"></div>
    <div style="display: flex; align-items: center">
      <el-tag size="small" type="success" v-if="notebook.isPublic">公开</el-tag>
      <el-tag size="small" type="info" v-else>私有</el-tag>
      <span style="margin-left: 5px"></span>
      <el-text v-if="notebook.authorID === store.state.userInfo?.userID" size="small">
        {{ notebook.tags.length > 0 ? notebook.tags[0] : '' }}
      </el-text>
      <el-link
        v-else :underline="false"
        @click.stop="$router.push({ name: 'user', params: { userID: notebook.authorID }})"
      >
        <user-avatar
          :size="15"
          style="min-width: 15px"
          :avatar-url="userInfo?.avatar"
          :user-id="userInfo?.userID"
        ></user-avatar>
        <span class="text-truncated" style="width: 120px">{{ userInfo?.userName }}</span>
      </el-link>
    </div>
    <div style="height: 3px"></div>
    <el-text size="small">
      <el-icon>
        <Clock/>
      </el-icon>
      {{ notebook.updateTime }}
    </el-text>
  </div>
</el-card>
</template>

<script setup lang="ts">

import type { NotebookInfo, UserInfo } from '@/scripts/interfaces'
import { Clock } from '@element-plus/icons-vue'
import DefaultCover from '@/assets/default-file/default-notebook-cover.png'
import { useStore } from '@/store/store'
import UserAvatar from '@/components/UserAvatar.vue'
import { onBeforeMount, ref, watch } from 'vue'
import { apiGetUserInfo } from '@/scripts/API_User'

interface Props {
  notebook: NotebookInfo
}

const props = defineProps<Props>()

const store = useStore()

const userInfo = ref<UserInfo>()

const updateInfo = async () => {
  if (props.notebook.authorID === store.state.userInfo?.userID) {
    userInfo.value = store.state.userInfo
  } else {
    userInfo.value = await apiGetUserInfo(props.notebook.authorID)
  }
}

onBeforeMount(updateInfo)
watch(() => props.notebook, updateInfo)

</script>

<style scoped>
.card-size {
  width: 200px;
  height: 200px;
}

.cover {
  width: 100%;
  height: 120px;
  object-fit: cover;
}
</style>
