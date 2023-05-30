<template>
  <div class="container-layout">
    <div class="affix-container">
      <el-affix :offset="100" target=".container-layout">
        <el-tabs
          tab-position="right"
          style="text-align: left"
          v-model="activeTab"
          @tabClick="updateActiveTab"
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
        @jump-visibility="jumpTo('critical')"
        @jump-user-share="jumpTo('userSharing')"
        @jump-group-share="jumpTo('groupSharing')"
      ></setting-overview>
      <div style="margin-top: 30px"></div>
      <span style="font-size: 20px" id="userSharing">
        <b>用户分享</b>
        <el-button type="primary" style="float: right" @click="userShareSettingRef!.addUser()">
          <el-icon style="margin-right: 10px"><Share/></el-icon> 添加用户
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
        <el-button type="primary" style="float: right" @click="groupShareSettingRef!.addGroup()">
          <el-icon style="margin-right: 10px"><Share/></el-icon> 添加群组
        </el-button>
      </span>
      <group-share-setting
          ref="groupShareSettingRef"
          :notebook-info="notebookInfo"
          v-model="sharedGroups"
      ></group-share-setting>
      <div style="margin-top: 20px"></div>
      <div style="margin-top: 20px"></div>
      <span style="font-size: 20px" id="critical" ref="critical"><b>关键设置</b></span>
      <el-divider/>
      <div>
        <el-button
          :type="notebookInfo!.isPublic ? 'danger' : 'primary'"
          plain style="float: right"
          @click="() => notebookInfo!.isPublic ? onSetNotebookPrivate() : onSetNotebookPublic()"
        >
          将笔记本的可见性改为 {{ notebookInfo.isPublic ? '私有' : '公开' }}
        </el-button>
        <el-text size="large"><b> 改变笔记本的可见性 </b></el-text>
        <br>
        <el-text size="small">目前笔记本为{{ notebookInfo.isPublic ? '公开' : '私有' }}</el-text>
      </div>
      <el-divider/>
      <div>
        <el-button
          type="danger" plain
          style="float: right"
          @click="onDeleteNotebook"
        >
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
import GroupShareSetting from '@/components/notebook_page/setting/GroupShareSetting.vue'
import { Collection, Delete, Share, User } from '@element-plus/icons-vue'
import { onBeforeUnmount, onMounted, ref } from 'vue'
import SettingOverview from '@/components/notebook_page/setting/SettingOverview.vue'
import type { TabsPaneContext } from 'element-plus'
import { ElMessageBox } from 'element-plus'
import { apiDeleteNotebook } from '@/scripts/API_Notebook'
import { useRouter } from 'vue-router'
import { apiSetNotebookPrivate, apiSetNotebookPublic } from '@/scripts/API_Interact'

const activeTab = ref('overview')

interface Props {
  notebookInfo: NotebookInfo | undefined
}

const props = defineProps<Props>()

const sharedUsers = ref<UserInfo[]>([])
const sharedGroups = ref<GroupInfo[]>([])
const userShareSettingRef = ref<InstanceType<typeof UserShareSetting>>()
const groupShareSettingRef = ref<InstanceType<typeof GroupShareSetting>>()

// =========== 以下为滚动导航栏相关 ===========
const scrollRef = ref<InstanceType<typeof HTMLElement>>()
const scrollList = ref<{
  key: string
  scrollTopNum: number
}[]>([]) // 每个盒子的滚动距离列表

const listenForChange = ref(true)
const handleScroll = () => { // 滚动事件
  if (!listenForChange.value) return
  scrollList.value.forEach((item) => {
    if (window.scrollY + 70 >= item.scrollTopNum) {
      activeTab.value = item.key
    }
  })
}
const items = [
  'overview',
  'userSharing',
  'groupSharing',
  'critical'
]
const updateActiveTab = (pane: TabsPaneContext) => { // 单击单个导航执行事件
  jumpTo(pane.paneName as string)
}

const jumpTo = (item: string) => {
  const node = document.getElementById(item)! // 跳转到对应导航
  const targetY = node.offsetTop - 70
  const spendTime = Math.abs(window.scrollY - targetY) / 1.5 + 1000
  listenForChange.value = false
  window.scrollTo({
    top: targetY,
    behavior: 'smooth'
  })
  setTimeout(() => {
    listenForChange.value = true
  }, spendTime)
}

onMounted(() => {
  // 记录每个item距离页面顶端的距离
  items.forEach((item) => {
    scrollList.value.push({
      key: item,
      scrollTopNum: document.getElementById(item)!.offsetTop
    })
  })
  window.addEventListener('scroll', handleScroll, true)
})
onBeforeUnmount(() => {
  window.removeEventListener('scroll', handleScroll)
})

// ============关键设置================
const router = useRouter()
const onDeleteNotebook = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这个笔记本吗？所有和这个笔记本有关的一切都将被移除', '警告', {
        confirmButtonText: '确定',
        confirmButtonClass: 'el-button--danger',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
  } catch (e) {
    return
  }
  await apiDeleteNotebook(props.notebookInfo!.notebookID)
  router.back()
}

interface Emits {
  (e: 'onUpdateNotebookVisibility'): void
}

const emit = defineEmits<Emits>()

const onSetNotebookPrivate = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要将这个笔记本设为私有？点赞数和收藏数都将被清空，且所有评论将被删除', '警告', {
        confirmButtonText: '确定',
        confirmButtonClass: 'el-button--danger',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
  } catch (e) {
    return
  }
  await apiSetNotebookPrivate(props.notebookInfo!.notebookID)
  emit('onUpdateNotebookVisibility')
}

const onSetNotebookPublic = async () => {
  try {
    await ElMessageBox.confirm(
      '确定将笔记本设为公开？该笔记将对所有人可见。笔记本的评论区将会开放', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'primary'
      }
    )
  } catch (e) {
    return
  }
  await apiSetNotebookPublic(props.notebookInfo!.notebookID)
  emit('onUpdateNotebookVisibility')
}

</script>

<style scoped>
.container-layout {
  width: 100%;
  display: flex;
  flex-direction: row;
}

.affix-container {
  height: 100%;
  min-width: 230px;
}

.main-container {
  text-align: left;
  display: block;
  width: 100%;
}
</style>
