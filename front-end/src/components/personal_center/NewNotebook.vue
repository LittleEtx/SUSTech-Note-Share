<template>
<div class="common-layout">
  <el-form label-position="top" label-width="10px" size="small"
           :rules="rules"
           ref="form" :model="info"
  >
    <el-container>
      <el-aside width="220px">
        <el-form-item label="封面">
          <img-uploader
            :width="200" type="cover" :height="120"
            ref="coverUploader"
          ></img-uploader>
        </el-form-item>
      </el-aside>
      <el-main style="padding-top: 0">
        <el-form-item label="笔记本标题" prop="title">
          <el-input
            v-model="info.title"
            maxlength="40"
            placeholder="这个笔记本的名字"
            show-word-limit
          ></el-input>
        </el-form-item>
        <el-form-item label="分区" prop="directory">
          <el-select
            filterable
            allow-create
            default-first-option
            v-model="info.directory"
            placeholder="选择分区或输入新分区">
            <el-option
              v-for="item in directories"
              :key="item"
              :label="item"
              :value="item"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="可见性" prop="isPublic">
          <el-radio-group v-model="info.isPublic" size="small">
            <el-radio border :label="true">公开</el-radio>
            <el-radio border :label="false">私有</el-radio>
          </el-radio-group>
          <el-text type="info">
            {{ info.isPublic ? '这个笔记本将对所有人可见' : '这个笔记本仅对你可见' }}
          </el-text>
        </el-form-item>
        <el-form-item label="简介">
          <el-input
            v-model="info.description"
            type="textarea"
            maxlength="200"
            placeholder="写一写这个笔记本的用途"
            :autosize="{ minRows: 3 }"
            show-word-limit
          ></el-input>
        </el-form-item>
        <!--    标签动态输入      -->
        <el-form-item label="标签（至多十个）">
          <notebook-tags
            :tags="info.tags" :modify="true"
            @update="(tags) => info.tags = tags"
          ></notebook-tags>
        </el-form-item>
      </el-main>
    </el-container>
  </el-form>
</div>
</template>

<script setup lang="ts">
import ImgUploader from '@/components/ImgUploader.vue'
import { reactive, ref } from 'vue'
import { ElForm, ElInput, FormInstance, FormRules } from 'element-plus'
import { apiCreateNotebook, apiUploadNotebookCover, NewNotebookInfo } from '@/scripts/API_Notebook'
import NotebookTags from '@/components/NotebookTags.vue'

interface Props {
  directories: Iterable<string> // 现有的文件夹分类
  defaultDir: string // 默认的文件夹分类
}

const props = defineProps<Props>()

const info = reactive<NewNotebookInfo>({
  title: '',
  tags: [],
  description: '',
  directory: props.defaultDir,
  isPublic: false
})

const rules = reactive<FormRules>({
  title: [
    { required: true, message: '请输入笔记本标题', trigger: 'blur' },
    { min: 1, max: 40, message: '长度在 1 到 40 个字符', trigger: 'blur' }
  ],
  directory: [
    { required: true, message: '请选择或输入分区', trigger: ['blur', 'change'] },
    { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
  ],
  isPublic: [
    { required: true, message: '请选择可见性', trigger: 'blur' }
  ]
})

// 上传图片相关
const coverUploader = ref<InstanceType<typeof ImgUploader>>()
const form = ref<FormInstance>()

// 提交表单，表单未验证通过会抛出异常
const submit = async (): Promise<string | undefined> => {
  await form.value!.validate()
  const file = coverUploader.value!.getImgFile()
  console.log(file.value)
  const id = await apiCreateNotebook(info)
  if (file.value) {
    // 同时上传封面
    await apiUploadNotebookCover(id, file.value)
  }
  return id
}
defineExpose({ submit })

</script>

<style scoped>
.el-radio-group :deep(.el-radio) {
  margin: 0 6px 0 0;
}
</style>
