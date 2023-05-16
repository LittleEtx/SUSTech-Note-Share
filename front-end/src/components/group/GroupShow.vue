<template>
    <el-container class="container">
    <el-aside width="300px" style="padding: 20px">
      <el-card class="group-card" shadow="hover">
        <div slot="header">
          <h3>
            <el-button class="card-button" text @click="goBack">
              <el-icon size="25px">
                <Back/>
              </el-icon>
            </el-button>
            <img :src="SUSTechLogo" alt="" style="width: 70px">
            {{ group.groupName }}
          </h3>
        </div>
        <div class="group-description">
          描述：{{ group.groupDescription }}
        </div>
        <hr>
        <div class="group-members" style=" overflow-y: auto;">
          <h4>成员</h4>
          <div v-for="(member, index) in group.members" :key="index" >
            <el-link :underline="false">
              <el-avatar :src="member.avatar === null ? defaultCover : member.avatar" style="margin-right: 10px; margin-top: 10px"></el-avatar>
              <el-tooltip :content="member.userName" :effect="tooltipEffect">
                <span class="ellipsis" v-if="group.groupOwnerID === member.userID"
                      style="margin-top: 10px; font-weight: bold">{{ member.userName }}</span>
                <span class="ellipsis" v-else style="margin-top: 10px">{{ member.userName }}</span>
              </el-tooltip>
            </el-link>
          </div>
        </div>
      </el-card>
    </el-aside>
    <el-main class="note-main" style="padding: 20px; overflow-y: auto; max-height: 720px">
      <el-row>
        <el-space :size="20" wrap style="padding-left: 20px">
          <div
              v-for="(notebook, index) in group.notebookInfos"
              :key="index"
              style="position: relative"
          >
            <notebook-card :notebook="notebook"></notebook-card>
          </div>
        </el-space>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import axios from 'axios'
import { useRoute } from 'vue-router'
import { getTags } from '@/scripts/interfaces'
import NotebookCard from '@/components/NotebookCard.vue'
import { Back } from '@element-plus/icons-vue'
import SUSTechLogo from '@/assets/icon/icon_with_words_shadow.svg'

export default {
  components: { NotebookCard, Back },
  computed: {
    SUSTechLogo () {
      return SUSTechLogo
    },
  },
  mounted () {
    const route = useRoute()
    this.group.groupID = route.params.groupID
    this.getData()
    // setInterval(() => {
    //   this.getData()
    // }, 100)
  },
  data () {
    return {
      defaultCover: 'https://th.bing.com/th/id/OIP.KwjH2oGKJygdvunLigvPrQAAAA?w=204&h=204&c=7&r=0&o=5&dpr=1.2&pid=1.7',
      isFavorite: false,
      tooltipEffect: 'light',
      group: {
        groupID: '',
        groupName: '群组名称',
        groupDescription: '群组描述',
        groupOwnerID: '',
        members: [
          { userID: 1, userName: '这是一个非常长的名字', avatar: '' }
        ],
        notebookInfos: [
          {notebookID: '', title: '', tags: '', updateTime: '', authorID: '', cover: '', description: '', isPublic: '', likeCount: '',
              starCount: '', directory: ''}
        ]
      }
    }
  },

  methods: {
    getData () {
      axios.post('/api/group/groupInfo', {
        groupID: this.group.groupID
      }).then(res => {
        this.group.groupName = res.data.groupName
        this.group.groupDescription = res.data.groupDescription
        this.group.groupOwnerID = res.data.groupOwnerID
      })
      axios.post('/api/group/groupMembers', {
        groupID: this.group.groupID
      }).then(res => {
        this.group.members = res.data
      })
      axios.post('/api/group/groupNotebooksInfo', {
        groupID: this.group.groupID
      }).then(res => {
        this.group.notebookInfos = res.data
        for (let i = 0; i < res.data.length; i++) {
          this.group.notebookInfos[i].tags = getTags(res.data[i].tag)
        }
      })
    },
    // handleClickNoteCard (note, event) {
    //   // 处理点击卡片事件
    //   if (event.target.tagName === 'BUTTON') {
    //     return
    //   }
    //   this.$router.push('/groupTest/')
    // },
    goBack () {
      // 处理返回逻辑
      this.$router.back()
    },
    toggleFavorite () {
      this.isFavorite = !this.isFavorite
      const icon = this.isFavorite ? 'el-icon-star-on' : 'el-icon-star-off'
      const text = this.isFavorite ? '取消收藏' : '收藏'
      this.$message.success(this.isFavorite ? '已收藏' : '已取消收藏')
      this.$refs.favoriteBtn.setIconClass(icon)
      this.$refs.favoriteBtn.setText(text)
    }
  }
}
</script>

<style scoped>
.ellipsis {
  display: inline-block;
  max-width: 100px; /* 根据实际情况设置最大宽度 */
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url("../../assets/login-page/background-img/2023.05.png");
  background-size: cover;
  background-position: center;
  /*background-image: linear-gradient(45deg, #f5f5f5, #e8e8e8);*/
}
.group-card {
  background-color: #fff;
  border-radius: 5px;
  position: relative;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  /*padding: 15px;*/
  padding: 20px;
  height: 90%;
}
.card-button {
  position: absolute;
  top: 0;
  left: 0;
}

.group-description {
  margin: 20px 0;
  font-size: 14px;
  color: #888;
  line-height: 1.5;
}

.group-members {
  height: 400px;
  max-height: 400px;
  overflow-y: auto;
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
  background-image: linear-gradient(45deg, #f5f5f5, #e8e8e8);
}
.note-main{
  height: 100%;
  overflow-y: auto;
}

.note-header h4 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

</style>
