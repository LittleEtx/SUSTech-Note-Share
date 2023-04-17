<template>
  <div class="notebook" :style="{ width: isBig ? '240px' : '180px' }">
    <img :src="notebook.cover" class="notebook-cover" alt="">
    <!--  mask, like and star when public  -->
    <div style="position: absolute" v-show="showPublic">
      <img src="../../assets/notebook/notebook-cover-mask.png" class="notebook-cover" alt="">
      <div class="notebook-popularity">
        <img src="../../assets/notebook/thumb-up-fill.svg" alt="" style="height: 12px;">
        <div style="margin-left: 5px"> {{ notebook.like }} </div>
        <i class="el-icon-star-on" style="font-size: 14px; margin-left: 15px"></i>
        <div style="margin-left: 5px"> {{ notebook.star }} </div>
      </div>
    </div>
    <!--  title & info  -->
    <IconButton>
        <h5 class="notebook-title" :class="isBig ? 'notebook-title-big' : 'notebook-title-small'"> {{ notebook.title }}</h5>
    </IconButton>
    <IconButton> <div class="notebook-info"> {{ notebook.tag }}</div> </IconButton>
    <div class="notebook-info">
        <div class="el-icon-time"></div>
        {{ (isBig ? '上次更新于：' : '') + notebook.updateTime }}
    </div>
    <!--  up  -->
    <IconButton class="notebook-info">
        <img src="../../assets/notebook/up.svg" style="width: 20px" alt="">
        <div> {{ authorName }}</div>
    </IconButton>
  </div>
</template>

<script>

import IconButton from './IconButton.vue'
import { apiGetUserInfo } from '../../scripts/API_User'

export default {
  name: 'Notebook',
  components: {IconButton},
  computed: {
    isBig () {
      return this.size === 'big'
    }
  },
  props: {
    size: {
      type: String,
      default: 'big',
      range: ['big', 'small']
    },
    showPublic: {
      type: Boolean,
      default: true
    },
    notebook: {
      type: Object,
      default: () => {
        return {
          cover: require('../../assets/default-file/default-notebook-cover.png'),
          title: '笔记本标题',
          tag: 'CS304 - 软件工程',
          updateTime: new Date().toLocaleString(),
          isPublic: true,
          description: '这是一个笔记本',
          authorID: 12112628,
          like: 114,
          star: 514
        }
      },
      required: true
    }
  },
  data () {
    return {
      authorName: ''
    }
  },
  async beforeMount () {
    this.authorName = (await apiGetUserInfo(this.notebook.authorID)).userName
  }
}
</script>

<style scoped>
.notebook {
  display: flex;
  flex-direction: column;
  justify-content:flex-start;
  align-items: start;
}

.notebook-cover {
  cursor: pointer;
  width: 100%;
  border-radius: 10px;
}

.notebook-title {
  margin: 0;
  height: 40px;
  width: 100%;
  text-align: left;
  font-size: 16px;
  word-break: break-word;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
}

.notebook-title-big {
  -webkit-line-clamp: 2;
  height: 40px;
}

.notebook-title-small {
  -webkit-line-clamp: 1;
  height: 20px;
}

.notebook-info {
  margin-top: 4px;
  text-align: left;
  font-size: 12px;
  overflow:hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.notebook-popularity {
  font-size: 12px;
  color: white;
  float: left;
  position: relative;
  margin-top: -25px;
  margin-left: 10px;
  display: flex;
  flex-direction: row;
}
</style>
