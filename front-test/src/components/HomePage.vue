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
        <span slot="label"><i class="el-icon-collection"></i> <b>笔记</b> </span>
      </el-tab-pane>
      <el-tab-pane name="star">
        <span slot="label"><i class="el-icon-star-off"></i> <b>收藏</b> </span>
      </el-tab-pane>
      <el-tab-pane name="share">
        <span slot="label"><i class="el-icon-share"></i> <b>收藏</b> </span>
      </el-tab-pane>
      <el-tab-pane name="group">
        <span slot="label"><i class="el-icon-menu"></i> <b>群组</b> </span>
      </el-tab-pane>
    </el-tabs>
  </div>
</div>
</template>

<script>
import MainHeader from './subcomponents/MainHeader.vue'
import Notebook from './subcomponents/Notebook.vue'
import UserDisplay from './personal_center/UserDisplay.vue'

export default {
  name: 'HomePage',
  components: {
    UserDisplay,
    Notebook,
    MainHeader
  },
  data () {
    return {
      userID: ''
    }
  },
  async beforeMount () {
    this.userID = await this.$store.getters.userID
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
