<template>
<div>
  <div class="header">
    <main-header></main-header>
  </div>
  <el-button
    style="position: absolute; left: 20px; top: 80px; z-index: 100"
    @click="router.push({ name: 'user', params: { userID: userInfo.userID } })"
  >视角：我自己
  </el-button>
  <div class="main-container">
    <transition name="el-zoom-in-center">
      <user-display :id="userInfo?.userID" v-show="activeSlot === 'info'"></user-display>
    </transition>
    <div style=" margin-left: 30px"></div>
    <div style="width: 100%">
      <el-tabs v-model="activeSlot">
        <el-tab-pane name="info" v-if="activeSlot !== 'info'">
          <template #label>
            <span>
              <user-avatar
                :size="20" style="vertical-align: bottom; margin-right: 5px"
                :avatar-url="userInfo?.avatar"
                :user-id="userInfo?.userID"
              ></user-avatar>
              <b> {{ userInfo?.userName }} </b>
            </span>
          </template>
        </el-tab-pane>
        <el-tab-pane name="note">
          <template #label>
            <span><el-icon><Collection /></el-icon> <b>笔记</b> </span>
          </template>
        </el-tab-pane>
        <el-tab-pane name="star">
          <template #label>
            <span><el-icon><Star /></el-icon> <b>收藏</b> </span>
          </template>
        </el-tab-pane>
        <el-tab-pane name="share">
          <template #label>
            <span><el-icon><Share /></el-icon> <b>分享</b> </span>
          </template>
        </el-tab-pane>
        <el-tab-pane name="group">
          <template #label>
            <span><el-icon><MenuIcon /></el-icon> <b>群组</b> </span>
          </template>
        </el-tab-pane>
      </el-tabs>
      <center-main v-show="activeSlot === 'info'"></center-main>
      <center-notebooks v-show="activeSlot === 'note'"></center-notebooks>
      <center-star v-show="activeSlot === 'star'"></center-star>
      <center-share v-show="activeSlot === 'share'"></center-share>
      <center-groups v-show="activeSlot === 'group'"></center-groups>
    </div>
  </div>
</div>
</template>

<script>
import UserDisplay from '../components/personal_center/UserDisplay.vue'
import { Collection, Menu as MenuIcon, Share, Star } from '@element-plus/icons-vue'
import CenterMain from '@/components/personal_center/CenterMain.vue'
import UserAvatar from '@/components/UserAvatar.vue'
import CenterGroups from '@/components/group/CenterGroups.vue'
import CenterNotebooks from '@/components/personal_center/CenterNotebooks.vue'
import MainHeader from '@/components/MainHeader.vue'
import CenterStar from '@/components/personal_center/CenterStar.vue'
import CenterShare from '@/components/personal_center/CenterShare.vue'
import { useRouter } from 'vue-router'

export default {
  components: {
    CenterShare,
    CenterStar,
    MainHeader,
    CenterNotebooks,
    CenterGroups,
    UserAvatar,
    CenterMain,
    Share,
    Star,
    Collection,
    MenuIcon,
    UserDisplay
  },
  setup () {
    const router = useRouter()
    return {
      router
    }
  },
  data () {
    return {
      activeSlot: 'info',
      userInfo: {}
    }
  },
  beforeMount () {
    this.userInfo = this.$store.state.userInfo
  }
}
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
