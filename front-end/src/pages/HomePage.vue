<template>
<div>
  <div class="header">
      <main-header></main-header>
  </div>
  <div class="main-container">
    <user-display :id="userID"></user-display>
    <div style=" margin-left: 30px"></div>
    <el-tabs style="width: 100%">
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
  </div>
</div>
</template>

<script>
import MainHeader from '../components/MainHeader.vue'
import UserDisplay from '../components/personal_center/UserDisplay.vue'
import { Collection, Share, Star, Menu as MenuIcon } from '@element-plus/icons-vue'

export default {
  name: 'HomePage',
  components: {
    Share,
    Star,
    Collection,
    MenuIcon,
    UserDisplay,
    MainHeader
  },
  data () {
    return {
      userID: -1
    }
  },
  async beforeMount () {
    this.userID = this.$store.state.userInfo.userID
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
