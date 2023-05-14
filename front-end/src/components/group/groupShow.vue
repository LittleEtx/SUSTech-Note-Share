<template>
    <el-container class="container">
    <el-aside width="300px" style="padding: 20px; height: 100%">
      <el-card class="group-card" shadow="hover">
        <div slot="header">
          <h3>
<!--            <el-button class="card-button" icon="el-icon-arrow-left" @click="goBack"></el-button>-->

            <el-button class="card-button"  style="width: 40px" text @click="goBack">
            <el-icon size="25px"><Back /></el-icon></el-button>
            <img src="../../assets/icon/icon_with_words_shadow.svg" alt="" style="width: 70px">
            {{ group.groupName }}
          </h3>
        </div>
        <div class="group-description">
          描述：{{ group.groupDescription }}
        </div>
        <hr>
        <div class="group-members" style=" overflow-y: auto;">
          <h4>成员</h4>
          <el-list-item v-for="(member, index) in group.members" :key="index">
            <el-link :underline="false" style="display:flex; align-items:center;">
              <el-avatar :src="'http://10.32.58.153:8088'+member.avatar" size="medium" style="margin-right: 10px; margin-top: 10px"></el-avatar>
              <span v-if="group.groupOwnerID === member.userID" style="margin-top: 10px; font-weight: bold">{{ member.userName }}</span>
              <span v-else style="margin-top: 10px">{{ member.userName }}</span>
            </el-link>
          </el-list-item>
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
            <el-icon type="info" class="more-icon">
              <MoreFilled/>
            </el-icon>
          </div>
        </el-space>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import axios from "axios";
import { useRoute } from 'vue-router'
import { getTags } from '@/scripts/interfaces'
import NotebookCard from "@/components/NotebookCard.vue";
import { Back } from '@element-plus/icons-vue'


export default {
  components: {NotebookCard, Back},
  mounted () {
    const route = useRoute()
    this.group.groupID = route.params.groupID
    this.getData()
    // setInterval(() => {
    //   this.getData()
    // }, 100)
  },
  data() {
    return {
      isFavorite: false,
      group: {
        groupID: '',
        groupName: '群组名称',
        groupDescription: '群组描述',
        groupOwnerID: '',
        members: [
          {userID: 1, userName: '成员1', avatar: ''}
        ],
        notebookInfos: [
          // {notebookID: '', title: '', tags: '', updateTime: '', authorID: '', cover: '', description: '', isPublic: '', likeCount: '',
          //     starCount: '', directory: ''}
        ]
      }
    }
  },
  methods: {
    getData(){
      axios.post("http://10.32.58.153:8088/api/group/groupInfo",{
        groupID: this.group.groupID
      }).then(res => {
        this.group.groupName = res.data.groupName
        this.group.groupDescription = res.data.groupDescription
        this.group.groupOwnerID = res.data.groupOwnerID
      });
      axios.post("http://10.32.58.153:8088/api/group/groupMembers",{
        groupID: this.group.groupID
      }).then(res => {
        this.group.members = res.data
      });
      axios.post("http://10.32.58.153:8088/api/group/groupNotebooksInfo",{
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
    goBack() {
      // 处理返回逻辑
      this.$router.back()
    },
    toggleFavorite() {
      this.isFavorite = !this.isFavorite;
      const icon = this.isFavorite ? 'el-icon-star-on' : 'el-icon-star-off';
      const text = this.isFavorite ? '取消收藏' : '收藏';
      this.$message.success(this.isFavorite ? '已收藏' : '已取消收藏');
      this.$refs.favoriteBtn.setIconClass(icon);
      this.$refs.favoriteBtn.setText(text);
    }
  }
}
</script>

<style scoped>

.more-icon {
  position: absolute;
  right: 10px;
  bottom: 10px;
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
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
}
.card-button {
  position: absolute;
  top: 20px;
  left: 20px;
}

.group-description {
  margin: 20px 0;
  font-size: 14px;
  color: #888;
  line-height: 1.5;
}

.group-members {
  height: 450px;
  max-height: 450px;
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

.note-card {
  margin: 10px;
  width: 240px;
  height: 220px;
  position: relative;
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 10px;
  transition: all 0.2s ease-in-out;
}
.note-button {
  position: absolute;
  bottom: 0;
  right: 0;
}

.note-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

.note-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.note-header h4 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.note-time {
  font-size: 14px;
  color: #888;
}

.note-content {
  margin-top: 10px;
  font-size: 14px;
  color: #555;
  line-height: 1.5;
}
</style>
