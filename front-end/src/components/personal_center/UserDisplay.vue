<template>
<div class="head-container">
  <div><img :src="userInfo.avatar" class="avatar" alt=""></div>
  <div class="info-display">
    <!--    user name and gender    -->
    <div style="display: flex; flex-direction: row">
      <i v-show="getGenderIcon != null" :class="getGenderIcon"></i>
      <h4 style="margin: 0; font-size: 20px">{{ userInfo.userName }}</h4>
    </div>
    <!--   email   -->
    <p style="margin: 0; font-size: 10px"> {{ userInfo.email }}</p>
    <div v-show="userInfo.birth != null" style="margin-top: 10px;
    display: flex; flex-direction: row">
        <i class="el-icon-date"></i>
        <p style="margin-left: 10px; margin-top: 0; font-size: 15px">{{ userInfo.birth }}</p>
    </div>
    <p style="margin: 0; font-size: 10px"> {{ userInfo.description }}</p>
  </div>
</div>
</template>

<script>

import {apiGetUserInfo} from '../../scripts/API_User'

export default {
  name: 'UserDisplay',
  props: {
    id: {
      type: String,
      required: true
    }
  },
  computed: {
    editable () {
      return this.id === localStorage.getItem('userID')
    },
    getGenderIcon () {
      if (this.userInfo.gender === 0) {
        return 'el-icon-female'
      } else if (this.userInfo.gender === 1) {
        return 'el-icon-male'
      }
      return null
    }
  },
  data () {
    return {
      userInfo: {}
    }
  },
  async beforeMount () {
    this.userInfo = await apiGetUserInfo(this.id)
    // print all data in userInfo object
    for (let key in this.userInfo) {
      console.log(key + ': ' + this.userInfo[key])
    }
  }
}
</script>

<style scoped>
.head-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 200px;
}
.avatar {
  width: 150px;
  height: 150px;
  border-radius: 50%;
}

.info-display {
  max-width: 100%;
  margin-top: 10px;
  display: flex;
  flex-direction: column;
  align-items: baseline;
}
</style>
