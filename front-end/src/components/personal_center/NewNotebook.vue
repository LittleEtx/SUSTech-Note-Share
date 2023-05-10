<template>
<div class="common-layout">
  <el-form label-position="top" label-width="10px" size="small">
    <el-container>
      <el-aside width="220px">
        <el-form-item label="封面">
          <img-uploader :width="200" type="cover" :height="120"></img-uploader>
        </el-form-item>
      </el-aside>
      <el-main>
        <el-form-item label="笔记本标题">
          <el-input
            v-model="info.title"
            maxlength="40"
            placeholder="标题"
            show-word-limit
          ></el-input>
        </el-form-item>
        <el-form-item label="分区">
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
        <el-form-item label="可见性">
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
          <el-space wrap>
            <el-tag
              v-for="tag in info.tags"
              :key="tag"
              class="mx-1"
              closable
              :disable-transitions="false"
              @close="handleClose(tag)"
            >
              {{ tag }}
            </el-tag>
            <el-input
              v-show="info.tags.length < 10"
              v-if="inputVisible"
              ref="inputRef"
              v-model="inputValue"
              class="ml-1 w-20"
              size="small"
              style="width: 60px"
              @keyup.enter="handleInputConfirm"
              @blur="handleInputConfirm"
            />
            <el-button v-else
                       v-show="info.tags.length < 10"
                       class="button-new-tag ml-1"
                       size="small"
                       @click="showInput"
            >
              + 新标签
            </el-button>
          </el-space>
        </el-form-item>
      </el-main>
    </el-container>
  </el-form>
</div>
</template>

<script setup lang="ts">
import ImgUploader from '@/components/personal_center/ImgUploader.vue'
import { nextTick, ref } from 'vue'
import { ElInput, ElMessage } from 'element-plus'

interface Props {
  directories: Iterable<string> // 现有的文件夹分类
}

defineProps<Props>()

interface NewNotebookInfo {
  title: string, // 笔记本标题
  tags: string[], // 笔记本标签
  description: string, // 笔记本描述
  directory: string, // 笔记本所处文件夹
  isPublic: boolean, // 是否公开
}

const info = ref<NewNotebookInfo>({
  title: '',
  tags: [],
  description: '',
  directory: '',
  isPublic: false
})

// 标签相关
const inputVisible = ref(false)
const inputRef = ref<InstanceType<typeof ElInput>>()
const inputValue = ref('')
const handleClose = (tag: string) => {
  info.value.tags.splice(info.value.tags.indexOf(tag), 1)
}

const showInput = () => {
  inputVisible.value = true
  nextTick(() => {
    inputRef.value!.input!.focus()
  })
}

const handleInputConfirm = () => {
  if (info.value.tags.indexOf(inputValue.value) > -1) {
    console.log('标签已存在')
    ElMessage.warning('标签已存在')
    return
  }
  if (inputValue.value) {
    info.value.tags.push(inputValue.value)
  }
  inputVisible.value = false
  inputValue.value = ''
}

</script>

<style scoped>
.el-radio-group :deep(.el-radio) {
  margin: 0 6px 0 0;
}
</style>
