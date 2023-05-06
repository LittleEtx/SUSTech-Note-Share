<template>
<div class="head-container">
  <div><img :src="userInfo.avatar" class="avatar" alt=""></div>
  <div class="info-display">
<!--   用户信息显示   -->
    <template v-if="!editing">
      <div style="margin-top: 20px"></div>
      <!--    user name and gender    -->
      <span style="font-size: 20px">
        <b> {{ userInfo.userName }} </b>
        <el-icon v-if="userInfo.gender === 1" size="15px"><Male /></el-icon>
        <el-icon v-if="userInfo.gender === 0" size="15px"><Female /></el-icon>
      </span>
      <!--   email   -->
      <p style="margin: 0; font-size: 10px"> {{ userInfo.email }}</p>
      <p style="margin-top: 10px; margin-bottom: 0; font-size: 15px" v-show="userInfo.birth !== null">
          <el-icon><Calendar /></el-icon>
          {{ userInfo.birth }}
      </p>
      <p style="margin-top: 10px; font-size: 10px"> {{ userInfo.description }}</p>
      <el-button v-show="editable" style="width: 100%" plain @click="editing = true"> 修改个人信息 </el-button>
    </template>
<!--   修改用户信息   -->
    <template v-else>
      <el-form label-position="top" label-width="10px">
        <el-form-item label="用户名">
          <el-input v-model="userInfo.userName"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="userInfo.gender" size="small">
            <el-radio border :label="1">男</el-radio>
            <el-radio border :label="0">女</el-radio>
            <el-radio border :label="null">保密</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="生日">
          <el-date-picker
            v-model="userInfo.birth" type="date"
            placeholder="选择日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd">
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="个性签名">
          <el-input
            type="textarea"
            :autosize="{ minRows: 3 }"
            placeholder="个性签名"
            v-model="userInfo.description"
          ></el-input>
        </el-form-item>
      </el-form>
      <div style="margin-top: 20px"></div>
      <p>
        <el-button v-show="editable" type="info" style="width: 40%" plain @click="editing = false"> 取消 </el-button>
        <el-button v-show="editable" type="primary" style="width: 40%" @click="editing = false"> 确定 </el-button>
      </p>
    </template>
  </div>
</div>
</template>

<script>

import { apiGetUserInfo } from '@/scripts/API_User'
import { Calendar, Female, Male } from '@element-plus/icons-vue'

export default {
  components: { Female, Male, Calendar },
  props: {
    id: {
      type: String,
      required: true
    }
  },
  data () {
    return {
      userInfo: {},
      editable: false,
      editing: false
    }
  },
  watch: {
    id: {
      handler: async function (newVal) {
        if (newVal === '') return
        this.editable = newVal === await this.$store.getters.userID
        this.userInfo = await apiGetUserInfo(newVal)
      },
      immediate: true
    }
  },
  methods: {
  }
}
</script>

<style scoped>
.head-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: space-between;
  min-width: 240px;
  width: 240px;
}
.avatar {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}

.info-display {
  width: 100%;
  text-align: left;
}

.el-form-item {
  margin: 0;
}

.el-form-item :deep(.el-form-item__label)  {
  font-size: 5px;
  padding: 0;
  margin-bottom: -5px;
}

.el-radio-group :deep(.el-radio)  {
  margin: 0;
}

</style>
