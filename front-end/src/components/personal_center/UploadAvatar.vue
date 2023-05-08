<template>
<el-upload
  ref="uploader"
  style="margin: 0 auto"
  action="#"
  accept="image/jpeg,image/jpg,image/png"
  :with-credentials="true"
  :limit="1"
  :auto-upload="false"
  :show-file-list="false"
  :on-exceed="handleExceed"
  :on-change="handleChange"
  :before-upload="checkImage"
  :http-request="uploadAvatar"
>
  <el-avatar v-if="imageUrl" class="avatar-size" :src="imageUrl" alt=""/>
  <div v-else class="avatar-size avatar-waiting">
    <el-icon size="50px"><Plus /></el-icon>
    <div>
      点击上传头像
    </div>
  </div>
  <template #tip>
    <div class="el-upload__tip">
      请上传 2Mb 以内的 jpg/png 文件
    </div>
  </template>
</el-upload>
<div style="margin-top: 20px"></div>
<span>
  <el-button style="width: 30%" @click="$emit('close-dialog')">取消</el-button>
  <el-button type="primary" @click="submitAvatar" style="width: 30%">
    确定
  </el-button>
</span>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import type { UploadInstance, UploadProps, UploadRawFile } from 'element-plus'
import { ElMessage, genFileId } from 'element-plus'
import { apiUploadAvatar } from '@/scripts/API_User'
import type { File } from 'buffer'

const emit = defineEmits(['close-dialog', 'submit-avatar'])
const imageUrl = ref('')
const uploader = ref<UploadInstance>() // 获取上传组件实例，这里定义的名字应和ref的名字一致
const loading = ref(false)
let avatar: File
const handleChange: UploadProps['onChange'] = (file) => {
  // show file in DOM
  imageUrl.value = URL.createObjectURL(file.raw as Blob)
  avatar = file.raw as File
}

// 当超过限制时，清空文件列表，重新上传
const handleExceed: UploadProps['onExceed'] = (files) => {
  uploader.value!.clearFiles()
  const file = files[0] as UploadRawFile
  file.uid = genFileId()
  uploader.value!.handleStart(file)
}

// 提交头像
const submitAvatar = () => {
  uploader.value!.submit()
}

// 检查图片大小
const checkImage: UploadProps['beforeUpload'] = (file: UploadRawFile) => {
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isLt2M) {
    ElMessage.error('上传头像图片大小不能超过 2MB!')
  }
  return isLt2M
}

const uploadAvatar: UploadProps['httpRequest'] = async () => {
  if (!avatar) {
    ElMessage.error('请先选择头像')
    return
  }
  loading.value = true
  await apiUploadAvatar(avatar)
  loading.value = false
  emit('submit-avatar')
}

</script>

<style scoped>
.avatar-size {
  width: 256px;
  height: 256px;
  border-radius: 50%;
}

.avatar-waiting {
  border: 1px dashed var(--el-border-color);
  color: var(--el-color-info);
  border-radius: 50%;
  cursor: pointer;
  transition: var(--el-transition-duration-fast);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.avatar-waiting:hover {
  border-color: var(--el-color-primary);
  color: var(--el-color-primary);
}
</style>
