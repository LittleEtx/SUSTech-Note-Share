<template>
<div>
  <el-affix>
    <div class="header">
      <main-header></main-header>
    </div>
  </el-affix>
  <el-button
    v-show="isOwner"
    style="position: absolute; left: 20px; top: 80px; z-index: 100"
    @click="router.push({ name: 'home' })"
  >视角：其他人
  </el-button>
  <div class="main-container">
    <div style="width: 240px">
      <el-affix :offset="90">
        <user-display :id="userID" :can-modify="false"></user-display>
      </el-affix>
    </div>
    <div style=" margin-left: 30px"></div>
    <div style="text-align: left">
      <div style="margin-top: 10px">
        <el-text size="large"><b> 公开笔记本 </b></el-text>
      </div>
      <div style="margin-top: 20px"></div>
      <el-space :size="20" wrap>
        <notebook-card
          v-for="(notebook, index) in publicNotebooks"
          :key="index" :notebook="notebook"
        ></notebook-card>
      </el-space>
    </div>
  </div>
  <el-backtop :right="30" :bottom="30"/>
</div>
</template>

<script lang="ts" setup>
import MainHeader from '@/components/MainHeader.vue'
import UserDisplay from '@/components/personal_center/UserDisplay.vue'
import { useRoute, useRouter } from 'vue-router'
import NotebookCard from '@/components/NotebookCard.vue'
import type { NotebookInfo } from '@/scripts/interfaces'
import { computed, onBeforeMount, ref, watch } from 'vue'
import { apiGetPublicNotebooks } from '@/scripts/API_Center'
import { useStore } from '@/store/store'

const route = useRoute()
const router = useRouter()
const publicNotebooks = ref<NotebookInfo[]>([])

watch(
  () => route.params.userID,
  async (newID) => {
    console.log(newID)
    await updateInfo(newID as number)
  }
)

const userID = computed(() => Number(route.params.userID))
const store = useStore()
const isOwner = computed(() => {
  return userID.value === store.state.userInfo?.userID
})

const updateInfo = async (id: number) => {
  console.log(id)
  publicNotebooks.value = await apiGetPublicNotebooks(id)
}

onBeforeMount(async () => {
  await updateInfo(userID.value)
})

</script>

<style scoped>
.main-container {
  display: flex;
  flex-direction: row;
  justify-content: left;
  margin-left: 10%;
  margin-right: 10%;
  margin-top: 40px;
}
</style>
