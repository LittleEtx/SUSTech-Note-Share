<template>
<div class="head-container">
  <div>
    <user-avatar :src="userInfo?.avatar" :size="240"> </user-avatar>
    <!--suppress JSValidateTypes -->
    <el-button :icon="Switch" circle size="large"
               style="position: absolute; margin-left: -40px; margin-top: 170px"
               @click="showUploadAvatar = true"
    ></el-button>
  </div>
<!--  上传头像对话框   -->
  <el-dialog
    v-model="showUploadAvatar"
    :close-on-click-modal="false" :show-close="false"
    destroy-on-close
    title="上传头像"
  >
    <upload-avatar
      @close-dialog="showUploadAvatar = false"
      @submit-avatar="submitAvatar"
    ></upload-avatar>
  </el-dialog>
  <div style="margin-top: 20px"></div>
  <div class="info-display">
<!--   用户信息显示   -->
    <template v-if="!editing">
      <!--    user name and gender    -->
      <span style="font-size: 20px">
        <b> {{ userInfo?.userName }} </b>
        <el-icon v-if="userInfo?.gender === 1" size="15px"><Male /></el-icon>
        <el-icon v-if="userInfo?.gender === 0" size="15px"><Female /></el-icon>
      </span>
      <!--   email   -->
      <p style="margin: 0; font-size: 10px"> {{ userInfo?.email }}</p>
      <p style="margin-top: 10px; margin-bottom: 0; font-size: 15px" v-show="userInfo?.birth !== null">
          <el-icon><Calendar /></el-icon>
          {{ userInfo?.birth }}
      </p>
      <p style="margin-top: 10px; font-size: 10px"> {{ userInfo?.description }}</p>
      <el-button v-show="editable" style="width: 100%" plain @click="editing = true"> 修改个人信息 </el-button>
    </template>
<!--   修改用户信息   -->
    <template v-else>
      <div v-loading="submittingNewInfo" element-loading-background="rgba(255, 255, 255, 0.3)">
        <el-form label-position="top" label-width="10px" class="modify-info-form" size="small">
          <el-form-item label="用户名（24小时内只能修改一次）">
            <el-input
              v-model="userInfo.userName"
              maxlength="20"
              show-word-limit
            ></el-input>
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
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="个性签名">
            <el-input
              type="textarea"
              :autosize="{ minRows: 3 }"
              placeholder="个性签名"
              v-model="userInfo.description"
              maxlength="200"
              show-word-limit
            ></el-input>
          </el-form-item>
        </el-form>
        <div style="margin-top: 20px"></div>
        <p style="display: flex; justify-content: space-between">
          <el-button v-show="editable" type="info" style="width: 45%" plain @click="cancelEdit"> 取消 </el-button>
          <el-button v-show="editable" type="primary" style="width: 45%" @click="submitEdit"> 确定 </el-button>
        </p>
      </div>
    </template>
  </div>
</div>
</template>

<script>
import { apiGetUserInfo, apiUpdateInfo } from '@/scripts/API_User'
import { Calendar, Female, Male, Switch } from '@element-plus/icons-vue'
import UploadAvatar from '@/components/personal_center/UploadAvatar.vue'
import DefaultAvatar from '@/assets/default-file/default-avatar.png'
import { store } from '@/store/store'
import UserAvatar from '@/components/UserAvatar.vue'

// noinspection JSUnusedGlobalSymbols
export default {
  components: { UserAvatar, UploadAvatar, Female, Male, Calendar },
  props: {
    id: {
      default: undefined,
      type: Number,
      required: false
    }
  },
  data () {
    return {
      tempInfo: {},
      editable: false,
      editing: false,
      showUploadAvatar: false,
      submittingNewInfo: false
    }
  },
  computed: {
    Switch () {
      return Switch
    },
    DefaultAvatar () {
      return DefaultAvatar
    },
    userInfo () {
      return this.editable ? store.state.userInfo : this.tempInfo
    }
  },
  async beforeMount () {
    if (!this.id || this.id === store.state.userInfo?.userID) {
      this.editable = true
    } else {
      this.editable = false
      this.tempInfo = await apiGetUserInfo(this.id)
    }
  },
  methods: {
    async submitAvatar () {
      await store.dispatch('updateInfo')
      this.showUploadAvatar = false
    },
    async cancelEdit () {
      await store.dispatch('updateInfo')
      this.editing = false
    },
    async submitEdit () {
      this.submittingNewInfo = true
      try {
        // 提交信息
        await apiUpdateInfo(
          this.userInfo.userName,
          this.userInfo.description,
          this.userInfo.gender,
          this.userInfo.birth
        )
        this.editing = false
      } catch (e) {
        await this.$alert('修改失败: ' + e.response.data)
      }
      await store.dispatch('updateInfo')
      this.submittingNewInfo = false
    }
  }
}
</script>

<style scoped>
.head-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  min-width: 240px;
  width: 240px;
}

.info-display {
  width: 100%;
  text-align: left;
}

.modify-info-form :deep(.el-form-item) {
  margin: 0 0 8px 0;
}

.el-radio-group :deep(.el-radio)  {
  margin: 0 6px 0 0;
}
</style>
