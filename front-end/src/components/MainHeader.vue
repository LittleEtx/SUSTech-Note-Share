<!--suppress JSValidateTypes -->
<template>
<div style="height: 60px; margin-left: 20px; margin-right: 20px">
  <el-row type="flex" justify="center" align="middle" style="height: 100%">
    <el-col :span="9">
      <img :src="SUSTechNoteIcon" class="icon"
           @click="$router.push({ name:'home' })" alt="">
    </el-col>
    <el-col :span="6">
      <el-input placeholder="搜索笔记" class="search-input">
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
    </el-col>
    <el-col :span="6"></el-col>
    <el-col :span="3">
      <el-popover placement="bottom-start" trigger="hover" width="200px">
        <template #reference>
          <user-avatar class="avatar" :size="40"></user-avatar>
        </template>
        <template #default>
          <div class="pop-out">
            <!-- 个人信息展示  -->
            <div style="display: flex; justify-content: right; margin-bottom: 10px">
              <user-avatar :size="40"></user-avatar>
              <div style="margin-left: 10px; display: flex; flex-direction: column;
              justify-content: center; align-items: start">
                <p style="margin: 0; font-size: 15px"><b>{{userName}}</b> </p>
                <p style="margin: 0; font-size: 10px"> {{userID}}</p>
              </div>
            </div>
            <!-- 快捷入口 -->
            <el-divider class="pop-divider" />
            <el-link :underline="false" :icon="User" class="popover-button" @click="$router.push({ name: 'home' })">
              <h5 class="popover-word">个人中心</h5>
              <el-icon>
                <ArrowRight/>
              </el-icon>
            </el-link>
            <el-link :underline="false" :icon="Lock" class="popover-button" @click="resetPassword">
              <h5 class="popover-word">修改密码</h5>
              <el-icon><ArrowRight /></el-icon>
            </el-link>
            <el-divider class="pop-divider" />
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
import { apiLogout } from '@/scripts/API_Auth'
import { ArrowRight, Lock, Search, SwitchButton, User } from '@element-plus/icons-vue'
import DefaultAvatar from '@/assets/default-file/default-avatar.png'
import SUSTechNoteIcon from '@/assets/icon/icon_with_word.svg'
import UserAvatar from '@/components/UserAvatar.vue'
import { useRouter } from 'vue-router'

export default {
  setup () {
    const router = useRouter()
    return {
      router
    }
  },
  components: { UserAvatar, ArrowRight, Search },
  computed: {
    SwitchButton () {
      return SwitchButton
    },
    Lock () {
      return Lock
    },
    User () {
      return User
    },
    DefaultAvatar () {
      return DefaultAvatar
    },
    SUSTechNoteIcon () {
      return SUSTechNoteIcon
    },
    avatar () {
      return this.$store.state.userInfo?.avatar
    },
    userName () {
      return this.$store.state.userInfo?.userName
    },
    userID () {
      return this.$store.state.userInfo?.userID
    }
  },
  methods: {
    async logout () {
      try {
        await apiLogout()
        this.$store.commit('logout')
        await this.router.push({ name: 'login' })
      } catch (e) {
        this.$message({
          message: '登出失败',
          type: 'error'
        })
      }
    },
    resetPassword () {
      this.router.push({ name: 'reset_password' })
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

.pop-divider {
  margin: 0;
}
</style>
