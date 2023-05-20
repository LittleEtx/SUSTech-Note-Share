<template>
  <div class="container-layout">
    <div class="affix-container">
      <el-affix :offset="100" target=".container-layout">
        <el-tabs
            tab-position="right"
            style="text-align: left"
            v-model="activeTab"
            @update:model-value="updateActiveTab"
        >
          <el-tab-pane name="overview">
            <template #label>
              <span style="width: 160px">
                <el-icon><Collection/></el-icon>
                <b style="margin-left: 10px">总览</b>
              </span>
            </template>
          </el-tab-pane>
          <el-tab-pane name="userSharing">
            <template #label>
              <span style="width: 160px">
                <el-icon><Share/></el-icon>
                <b style="margin-left: 10px">用户分享</b>
              </span>
            </template>
          </el-tab-pane>
          <el-tab-pane name="groupSharing">
            <template #label>
              <span style="width: 160px">
                <el-icon><User/></el-icon>
                <b style="margin-left: 10px">群组分享</b>
              </span>
            </template>
          </el-tab-pane>
          <el-tab-pane name="critical">
            <template #label>
              <span style="width: 160px">
                <el-icon><Delete/></el-icon>
                <b style="margin-left: 10px">关键设置</b>
              </span>
            </template>
          </el-tab-pane>
        </el-tabs>
      </el-affix>
    </div>
    <div class="main-container" ref="scrollRef">
      <span style="font-size: 20px" id="overview"><b>总览</b></span>
      <div style="margin-top: 20px"></div>
      <setting-overview
          :notebook-info="notebookInfo"
          :shared-users="sharedUsers"
          :shared-groups="sharedGroups"
      ></setting-overview>
      <div style="margin-top: 30px"></div>
      <span style="font-size: 20px" id="userSharing">
        <b>用户分享</b>
        <el-button type="primary" style="float: right" @click="userShareSettingRef!.addUser()">
          <el-icon style="margin-right: 10px"><Share/></el-icon>添加用户
        </el-button>
      </span>
      <user-share-setting
          ref="userShareSettingRef"
          :notebook-info="notebookInfo"
          v-model="sharedUsers"
      ></user-share-setting>
      <div style="margin-top: 20px"></div>
      <span style="font-size: 20px" id="groupSharing">
        <b>群组分享</b>
      </span>
      <div style="margin-top: 20px"></div>
      <span style="font-size: 20px" id="critical"><b>关键设置</b></span>
      <el-divider/>
      <div>
        <el-button :type="notebookInfo!.isPublic ? 'danger' : 'primary'" plain style="float: right">
          将笔记本的可见性改为 {{ notebookInfo.isPublic ? '私有' : '公开' }}
        </el-button>
        <el-text size="large"><b> 改变笔记本的可见性 </b></el-text>
        <br>
        <el-text size="small">目前笔记本为{{ notebookInfo.isPublic ? '公开' : '私有' }}</el-text>
      </div>
      <el-divider/>
      <div>
        <el-button type="danger" plain style="float: right">
          <el-icon style="margin-right: 10px">
            <Delete/>
          </el-icon>
          删除笔记本
        </el-button>
        <el-text size="large"><b>删除笔记本</b></el-text>
        <br>
        <el-text size="small">永久删除该笔记本</el-text>
      </div>

      <div style="margin-top: 40px"></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { GroupInfo, NotebookInfo, UserInfo } from '@/scripts/interfaces'
import UserShareSetting from '@/components/notebook_page/setting/UserShareSetting.vue'
import { Collection, Delete, Share, User } from '@element-plus/icons-vue'
import { nextTick, onBeforeUnmount, onMounted, ref } from 'vue'
import SettingOverview from '@/components/notebook_page/setting/SettingOverview.vue'

const activeTab = ref('share')

interface Props {
  notebookInfo: NotebookInfo | undefined
}

defineProps<Props>()

const sharedUsers = ref<UserInfo[]>([])
const sharedGroups = ref<GroupInfo[]>([])
const userShareSettingRef = ref<InstanceType<typeof UserShareSetting>>()

// =========== 以下为滚动导航栏相关 ===========
const scrollRef = ref<InstanceType<typeof HTMLElement>>()
const scrollList = ref([]) // 每个盒子的滚动距离列表

const handleScroll = (e) => { // 滚动事件
  scrollList.value.forEach((item) => {
    if (e.target.scrollTop >= item.scrollTopNum) {
      activeTab.value = item
    }
  })
}

const items = [
  'overview',
  'userSharing',
  'groupSharing',
  'critical'
]

const updateActiveTab = (item) => { // 单击单个导航执行事件
  console.log(item)
  nextTick(() => {
    scrollRef.value!.scrollTop = document.getElementById(item)!.offsetTop + 2
  })
}

onMounted(() => {
  items.forEach((item) => {
    scrollList.value.push({ scrollTopNum: document.getElementById(item)!.offsetTop })
  })
  window.addEventListener('scroll', handleScroll, true)
})
onBeforeUnmount(() => {
  window.removeEventListener('scroll', handleScroll)
})

</script>

<style scoped>
.container-layout {
  width: 100%;
  display: flex;
  flex-direction: row;
}

.affix-container {
  height: 100%;
  min-width: 220px;
}

.main-container {
  text-align: left;
  display: block;
  width: 100%;
}
</style>
