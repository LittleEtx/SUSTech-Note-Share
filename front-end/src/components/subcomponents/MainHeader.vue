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
        <el-popover placement="bottom" trigger="hover" width="100"
        content="This is content">

            <div class="personal-info" slot="reference">
                <div><img :src="avatar" class="avatar" alt="unfind"></div>
                <div style="display: flex; flex-direction: column;
                justify-content: center; align-items: flex-start">
                    <h4 style="margin-bottom: 0">{{userName}}</h4>
                    <p style="margin-top: 0"> {{userID}}</p>
                </div>
            </div>
        </el-popover>
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
    align-items: center;
    flex-direction: row;
    justify-content: flex-end;
}

.avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    margin-right: 10px;
}

</style>
