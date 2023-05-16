<template>
  <el-dialog v-model="showDialog" center destroy-on-close>
    <template #header>
      <el-text size="large">上传文件至 {{ note.name }}</el-text>
    </template>
    <el-upload
      class="upload-demo"
      ref="uploadRef"
      drag
      action="#"
      :before-upload="checkUpload!"
      :on-remove="handleRemove!"
      :http-request="uploadFile!"
      :on-error="handleUploadError!"
      :on-change="handleUploadChange!"
      multiple
    >
      <el-icon class="el-icon--upload">
        <upload-filled/>
      </el-icon>
      <div class="el-upload__text">
        将文件拖入或者 <em> 点击上传 </em>
      </div>
      <template #tip>
        <div class="el-upload__tip" style="text-align: center">
          请上传 10MB 以内的文件
        </div>
      </template>
    </el-upload>
    <template #footer>
      <el-button @click="cancelUpload" style="width: 30%">取消</el-button>
      <el-button
        type="primary"
        @click="showDialog = false"
        style="width: 30%"
      >确定
      </el-button>
    </template>
  </el-dialog>

</template>

<script setup lang="ts">

import type { NoteInfo } from '@/scripts/interfaces'
import { UploadFilled } from '@element-plus/icons-vue'
import type { UploadFile, UploadFiles, UploadInstance, UploadProps } from 'element-plus'
import { ElMessage } from 'element-plus'
import { computed, ref } from 'vue'
import { apiDeleteFile, apiUploadFile } from '@/scripts/API_Notebook'

interface Props {
  note: NoteInfo
  modelValue: boolean // display the dialog or not
}

const props = defineProps<Props>()

const showDialog = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

interface Emit {
  (e: 'update:modelValue', value: boolean): void
}

const emit = defineEmits<Emit>()

const uploadRef = ref<UploadInstance>()

// 上传前检查
const checkUpload: UploadProps['beforeUpload'] = (file: File) => {
  const isLt10M = file.size < 10 * 1024 * 1024
  if (!isLt10M) {
    ElMessage.error(file.name + '无法上传：文件大小不能超过 10MB!')
  }
  return isLt10M
}

const fileToID = ref<Map<number, string>>(new Map()) // 建立文件UID到文件ID的映射
// 使用实际上上传的API
const uploadFile: UploadProps['httpRequest'] = async (params) => {
  fileToID.value[(params.file as UploadFile).uid] = await apiUploadFile(props.note.id, params.file)
}

const handleRemove: UploadProps['onRemove'] = async (file: UploadFile) => {
  console.log('handle remove on file: ' + file.name + ' under ' + file.status)
  if (file.status === 'ready') {
    return // 移除未上传的文件：不用做处理
  }
  if (file.status === 'uploading') {
    // 移除正在上传的文件：取消上传
    await uploadRef.value?.abort(file)
  }
  if (file.status === 'success') {
    console.log('delete file ' + fileToID.value[file.raw!.uid])
    // 移除已上传的文件：删除文件
    await apiDeleteFile(fileToID.value[file.raw!.uid])
  }
  fileToID.value.delete(file.raw!.uid)
}

const handleUploadError: UploadProps['onError'] = (err: Error, file: UploadFile) => {
  ElMessage.error(file.name + ' 上传失败：' + err.message)
  uploadRef.value?.abort(file)
}

// 获取上传文件列表，在删除时将其全部删除
const uploadFiles = ref<UploadFiles>([])
const handleUploadChange: UploadProps['onChange'] = (file: UploadFile, files: UploadFiles) => {
  uploadFiles.value = files
}

const cancelUpload = async () => {
  while (uploadFiles.value.length > 0) {
    const file = uploadFiles.value[0]
    uploadRef.value?.handleRemove(file)
  }
  showDialog.value = false
}
</script>
