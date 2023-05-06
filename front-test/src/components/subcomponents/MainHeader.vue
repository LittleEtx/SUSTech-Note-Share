<template>
<div style="height: 60px; margin-left: 20px; margin-right: 20px">
  <el-row type="flex" justify="center" align="middle" style="height: 100%">
    <el-col :span="9">
      <img src="../../assets/icon/icon_with_word.svg" class="icon"
           @click="this.$router.push('home')" alt="">
    </el-col>
    <el-col :span="6">
      <el-input placeholder="搜索笔记">
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
    </el-col>
    <el-col :span="6"></el-col>
    <el-col :span="3">
      <el-popover placement="bottom-start" trigger="hover" width="200px">
        <template #reference>
          <img :src="avatar" class="avatar" alt="">
        </template>
        <template #default>
          <div class="pop-out">
            <!-- 个人信息展示  -->
            <div style="display: flex; justify-content: right; margin-bottom: 10px">
              <img :src="avatar" class="avatar" alt="">
              <div style="margin-left: 10px; display: flex; flex-direction: column;
              justify-content: center; align-items: start">
                <p style="margin: 0; font-size: 15px"><b>{{userName}}</b> </p>
                <p style="margin: 0; font-size: 10px"> {{userID}}</p>
              </div>
            </div>
            <!-- 快捷入口 -->
            <el-divider />
            <el-link :underline="false" :icon="User" class="popover-button" @click="$router.push('home')">
              <h5 class="popover-word">个人中心</h5>
              <el-icon><ArrowRight /></el-icon>
            </el-link>
            <el-link :underline="false" :icon="Lock" class="popover-button" @click="resetPassword">
              <h5 class="popover-word">修改密码</h5>
              <el-icon><ArrowRight /></el-icon>
            </el-link>
            <el-divider />
            <el-link :underline="false" :icon="SwitchButton" class="popover-button" @click="logout">
              <h5 class="popover-word">登出</h5>
            </el-link>
          </div>
        </template>
      </el-popover>
    </el-col>
  </el-row>
</div>
</template>

<script>
import IconButton from './IconButton.vue'
import {apiGetUserInfo} from '@/scripts/API_User'
import {apiLogout} from '@/scripts/API_Auth'
import {router} from '@/router'
import DefaultAvatar from '@/assets/default-file/default-avatar.png'
import {ArrowRight, Lock, Search, SwitchButton, User} from "@element-plus/icons-vue"

export default {
  name: 'main-header',
    computed: {
        SwitchButton() {
            return SwitchButton
        },
        Lock() {
            return Lock
        },
        User() {
            return User
        }
    },
  components: {ArrowRight, Search, IconButton},
  data () {
    return {
      avatar: DefaultAvatar,
      showFullName: false,
      userName: '未登录',
      userID: ''
    }
  },
  async beforeMount () {
    this.userID = await this.$store.getters.userID
    const userData = await apiGetUserInfo(this.userID)
    this.avatar = userData.avatar
    this.userName = userData.userName
  },
  methods: {
    async logout () {
      try {
        await apiLogout()
        this.$store.commit('logout')
        await router.push('/login')
      } catch (e) {
        this.$message({
          message: '登出失败',
          type: 'error'
        })
      }
    },
    resetPassword () {
      router.push('reset_password')
    }
  }
}
</script>

<style scoped>
.icon {
  height: 50px;
  float: left;
  cursor: pointer;
}

.avatar {
  height: 50px;
  border-radius: 50%;
  float: right;
  cursor: pointer;
}

.pop-out {
  display: flex;
  height: 100%;
  flex-direction: column;
  justify-content: space-between;
  align-items: flex-start;
  margin-left: 20px;
}

.popover-button {
  display: flex;
  justify-content: start;
}

.popover-word {
  width: 100px;
  margin-left: 10px;
}

.el-divider {
  margin: 0;
}

</style>
