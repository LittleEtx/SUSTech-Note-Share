<template>
<el-row type="flex" justify="center" align="middle" style="height: 100%">
    <el-col :span="5">
        <img src="../../assets/icon/icon_with_word.svg" class="icon"
             @click="router.go(0)" alt="">
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
                <div class="pop-out" style="margin-left: 20px">
                    <icon-button icon="el-icon-user" class="pro-out-button">
                        <h5 class="pop-out-word">个人中心</h5>
                        <i class="el-icon-arrow-right"></i>
                    </icon-button>
                    <icon-button icon="el-icon-lock" class="pro-out-button">
                        <h5 class="pop-out-word">修改密码</h5>
                        <i class="el-icon-arrow-right"></i>
                    </icon-button>
                    <icon-button icon="el-icon-switch-button" class="pro-out-button">
                        <h5 class="pop-out-word">登出</h5>
                        <i class="el-icon-arrow-right"></i>
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
import axios from 'axios'
import router from '../../router'

export default {
  name: 'main-header',
  computed: {
    router () {
      return router
    }
  },
  components: {IconButton},
  data () {
    return {
      avatar: require('../../assets/default-file/default-avatar.png'),
      userName: '未登录',
      userID: ''
    }
  },
  beforeMount () {
    axios.get('/api/user/get-id').then((res) => {
      this.userID = res.data
      axios.get('/api/user/get-info', {
        params: { userID: this.userID }
      }).then((res) => {
        // this.avatar = res.data.avatar
        this.userName = res.data.userName
      })
    })
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

.pro-out-button {
    width: 130px;

}

.pop-out-word {
    width: 80px;
    margin-left: 10px;
}
</style>
