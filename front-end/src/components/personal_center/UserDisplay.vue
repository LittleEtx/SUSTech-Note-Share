<template>
<div class="head-container">
  <div style="position: relative">
    <user-avatar :avatar-url="userInfo?.avatar" :user-id="userInfo?.userID" :size="240"></user-avatar>
    <!--suppress JSValidateTypes -->
    <el-button
      :icon="Switch" circle size="large"
      v-if="canModifyInfo"
      style="position: absolute; margin-left: -40px; margin-top: 170px"
      @click="showUploadAvatar = true"
    ></el-button>
  </div>
<!--  上传头像对话框   -->
  <el-dialog
    v-model="showUploadAvatar"
    :close-on-click-modal="false"
    show-close
    destroy-on-close
    title="上传头像"
  >
    <div v-loading="uploadingAvatar" element-loading-background="rgba(255, 255, 255, 0.3)">
      <img-uploader
        ref="imgUploader"
        type="avatar"
        :height="256" :width="256"
      >
        <el-icon size="50px">
          <Plus/>
        </el-icon>
        <p> 点击上传头像 </p>
      </img-uploader>
      <div style="margin-top: 20px"></div>
      <span>
        <el-button style="width: 30%" @click="showUploadAvatar = false">取消</el-button>
        <el-button type="primary" @click="submitAvatar" style="width: 30%">
          确定
        </el-button>
      </span>
    </div>
  </el-dialog>
  <div style="margin-top: 20px"></div>
  <div class="info-display">
<!--   用户信息显示   -->
    <template v-if="!editing">
      <!--    user name and gender    -->
      <span style="font-size: 20px">
        <b> {{ userInfo?.userName }} </b>
        <el-icon v-if="userInfo?.gender === 1" size="15px"><Male/></el-icon>
        <el-icon v-if="userInfo?.gender === 0" size="15px"><Female/></el-icon>
      </span>
      <!--   email   -->
      <p style="margin: 0; font-size: 10px"> {{ userInfo?.email }}</p>
      <p style="margin-top: 10px; margin-bottom: 0; font-size: 15px" v-show="userInfo?.birth">
        <el-icon>
          <Calendar/>
        </el-icon>
        {{ userInfo?.birth }}
      </p>
      <p style="margin-top: 10px; font-size: 10px"> {{ userInfo?.description }}</p>
      <el-button v-if="canModifyInfo" style="width: 100%" plain @click="editing = true"> 修改个人信息</el-button>
    </template>
<!--   修改用户信息   -->
    <template v-else>
      <div v-loading="submittingNewInfo" element-loading-background="rgba(255, 255, 255, 0.3)">
        <el-form label-position="top" label-width="10px" size="small">
          <el-form-item label="用户名（24小时内只能修改一次）">
            <el-input
              v-model="userInfo!.userName"
              maxlength="20"
              show-word-limit
            ></el-input>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio-group v-model="userInfo!.gender" size="small">
              <el-radio border :label="1">男</el-radio>
              <el-radio border :label="0">女</el-radio>
              <el-radio border :label="-1">保密</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="生日">
            <el-date-picker
              v-model="userInfo!.birth" type="date"
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
              v-model="userInfo!.description"
              maxlength="200"
              show-word-limit
            ></el-input>
          </el-form-item>
        </el-form>
        <div style="margin-top: 20px"></div>
        <p style="display: flex; justify-content: space-between">
          <el-button type="info" style="width: 45%" plain @click="cancelEdit"> 取消</el-button>
          <el-button type="primary" style="width: 45%" @click="submitEdit"> 确定</el-button>
        </p>
      </div>
    </template>
  </div>
</div>
</template>

<script lang="ts" setup>
import { apiGetUserInfo, apiUpdateInfo, apiUploadAvatar } from '@/scripts/API_User'
import { Calendar, Female, Male, Plus, Switch } from '@element-plus/icons-vue'
import ImgUploader from '@/components/ImgUploader.vue'
import { useStore } from '@/store/store'
import UserAvatar from '@/components/UserAvatar.vue'
import { computed, onBeforeMount, ref, watch } from 'vue'
import type { UserInfo } from '@/scripts/interfaces'
import { ElMessage, ElMessageBox } from 'element-plus'

interface Props {
  id?: number,
  canModify?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  canModify: true
})

const store = useStore()

const userInfo = ref<UserInfo>()
const editing = ref(false)
const showUploadAvatar = ref(false)
const submittingNewInfo = ref(false)
const uploadingAvatar = ref(false)

const canModifyInfo = computed(() => {
  return props.canModify && props.id === store.state.userInfo?.userID
})

const updateInfo = async () => {
  if (!props.id || props.id === store.state.userInfo?.userID) {
    userInfo.value = store.state.userInfo
  } else {
    userInfo.value = await apiGetUserInfo(props.id)
  }
}

onBeforeMount(updateInfo)
watch(() => props.id, updateInfo)

const imgUploader = ref<InstanceType<typeof ImgUploader>>()
const submitAvatar = async () => {
  const img = imgUploader.value!.getImgFile()
  if (!img) {
    ElMessage.warning('请先选择图片')
    return
  }
  uploadingAvatar.value = true
  try {
    await apiUploadAvatar(img.value)
  } catch (e) {
    await ElMessageBox.alert('上传失败: ' + e.response.data)
  }
  await store.dispatch('updateInfo')
  userInfo.value = store.state.userInfo
  uploadingAvatar.value = false
  showUploadAvatar.value = false
}
const cancelEdit = async () => {
  await store.dispatch('updateInfo')
  userInfo.value = store.state.userInfo
  editing.value = false
}
const submitEdit = async () => {
  if (userInfo.value?.userName === '') {
    ElMessage.warning('用户名不能为空')
    return
  }

  submittingNewInfo.value = true
  try {
    // 提交信息
    await apiUpdateInfo(
      userInfo.value!.userName,
      userInfo.value!.description,
      userInfo.value!.gender,
      userInfo.value!.birth
    )
    editing.value = false
  } catch (e) {
    await ElMessageBox.alert('修改失败: ' + e.response.data)
  }
  await store.dispatch('updateInfo')
  submittingNewInfo.value = false
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
