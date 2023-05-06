<template>
<el-row type="flex" justify="center" align="middle" style="height: 100%">
    <el-col :span="5">
        <img src="../../assets/icon/icon_with_word.svg" class="icon"
             @click="this.$router.push('home')" alt="">
    </el-col>
    <el-col :span="4">
        <icon-button icon="el-icon-menu" class="section">
            <h4>全部分区</h4>
        </icon-button>
    </el-col>
    <el-col :span="6">
        <el-input placeholder="搜索笔记" class="search-bar" suffix-icon="el-icon-search"></el-input>
    </el-col>
    <el-col :span="5">
        <div class="fast-entry">
        <div>
            <icon-button icon="el-icon-date" attach-position="down">
                <h5 style="margin: 0">课程</h5>
            </icon-button>
        </div>
        <div>
            <icon-button icon="el-icon-collection" attach-position="down">
                <h5 style="margin: 0">笔记</h5>
            </icon-button>
        </div>
        <div>
            <icon-button icon="el-icon-star-off" attach-position="down">
                <h5 style="margin: 0">收藏</h5>
            </icon-button>
        </div>
        <div>
            <icon-button icon="el-icon-time" attach-position="down">
                <h5 style="margin: 0">历史</h5>
            </icon-button>
        </div>
    </div>
    </el-col>
    <el-col :span="4">
        <div class="personal-info">
            <el-popover placement="bottom" trigger="hover" width="60">
                <div class="pop-out" style="margin-left: 10px">
                    <icon-button icon="el-icon-user" class="popover-button">
                        <h5 class="popover-word">个人中心</h5>
                        <i class="el-icon-arrow-right"></i>
                    </icon-button>
                    <icon-button icon="el-icon-lock" class="popover-button" @click="resetPassword">
                        <h5 class="popover-word">修改密码</h5>
                        <i class="el-icon-arrow-right"></i>
                    </icon-button>
                    <icon-button icon="el-icon-switch-button"
                                 class="popover-button" @click="logout">
                        <h5 class="popover-word">登出</h5>
                        <i style="width: 10px"></i>
                    </icon-button>
                </div>
                <div slot="reference">
                    <icon-button>
                        <div><img :src="avatar" class="avatar" alt=""></div>
                        <div style="display: flex; flex-direction: column;
                        justify-content: center; align-items: flex-start">
                            <h4 style="margin: 0">{{userName}}</h4>
                            <p style="margin: 0; font-size: 10px"> {{userID}}</p>
                        </div>
                    </icon-button>
                </div>
            </el-popover>
        </div>
    </el-col>
</el-row>
</template>

<script>
import IconButton from './IconButton.vue'
import {apiGetUserInfo} from '@/scripts/API_User'
import {apiLogout} from '@/scripts/API_Auth'
import {router} from "@/router"

export default {
  name: 'main-header',
  components: {IconButton},
  data () {
    return {
      avatar: require('../../assets/default-file/default-avatar.png'),
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
    margin-left: 20px;
}

.section {
    float: right;
    margin-right: 20px;
}

.search-bar {
    height: 50px;
    margin-top: 10px;
}

.search-bar >>> .el-input__inner {
    background: #f0f0f0;
}

.fast-entry {
    margin-left: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.personal-info {
    margin-right: 20px;
    display: flex;
    justify-content: right;
}

.avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    margin-right: 10px;
}

.pop-out {
    display: flex;
    height: 100%;
    flex-direction: column;
    justify-content: space-between;
    align-items: flex-start;
}

.popover-button {
    width: 130px;

}

.popover-word {
    width: 80px;
    margin-left: 10px;
}
</style>
