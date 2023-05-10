<template>
<!-- 不使用uploader默认的上传方法  -->
<el-upload
  ref="uploader"
  style="margin: 0 auto"
  action="#"
  accept="image/jpeg,image/jpg,image/png"
  :with-credentials="true"
  :limit="1"
  :auto-upload="false"
  :show-file-list="false"
  :on-exceed="handleExceed!"
  :on-change="handleChange!"
  :before-upload="checkImage!"
>
  <el-image v-if="imageUrl" :src="imageUrl" :style="imgStyle" alt=""/>
  <div v-else class="avatar-waiting" :style="imgStyle">
    <slot>
      <el-icon size="50px"><Plus /></el-icon>
    </slot>
  </div>
  <template #tip>
    <div class="el-upload__tip">
      请上传 2Mb 以内的 jpg/png 文件
    </div>
  </template>
</el-upload>
</template>

<script setup lang="ts">
import {computed, reactive, ref} from 'vue'
import type { UploadInstance, UploadProps, UploadRawFile } from 'element-plus'
import { ElMessage, genFileId } from 'element-plus'
import { apiUploadAvatar } from '@/scripts/API_User'
import type { File } from 'buffer'
import { Plus } from '@element-plus/icons-vue'

interface Props {
  type: 'avatar' | 'cover'
  width: number
  height: number
}
const props = defineProps<Props>()
const imgStyle = reactive( {
  borderRadius: props.type === 'avatar' ? '50%' : '10%',
  width: `${props.width}px`,
  height: `${props.height}px`
})

const imageUrl = ref('')
const uploader = ref<UploadInstance>() // 获取上传组件实例，这里定义的名字应和ref的名字一致
let image: File
const handleChange: UploadProps['onChange'] = (file) => {
  // show file in DOM
  imageUrl.value = URL.createObjectURL(file.raw as Blob)
  image = file.raw as File
}

// 当超过限制时，清空文件列表，重新上传
const handleExceed: UploadProps['onExceed'] = (files) => {
  uploader.value!.clearFiles()
  const file = files[0] as UploadRawFile
  file.uid = genFileId()
  uploader.value!.handleStart(file)
}

// 检查图片大小
const checkImage: UploadProps['beforeUpload'] = (file: UploadRawFile) => {
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isLt2M) {
    ElMessage.error('上传图片大小不能超过 2MB!')
  }
  return isLt2M
}

// 提交头像
const submit = async () => {
  if (!image) {
    ElMessage.error('请先选择图片')
    return
  }
  await apiUploadAvatar(image)
}

defineExpose({
  submit,
  imageUrl
})

</script>

<style scoped>
.avatar-waiting {
  border: 1px dashed var(--el-border-color);
  color: var(--el-color-info);
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
