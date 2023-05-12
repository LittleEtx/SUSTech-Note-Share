<script setup lang="ts">

import { ElInput, ElMessage } from 'element-plus'
import { nextTick, ref } from 'vue'

interface Props {
  tags: string[]
  modify: boolean
}

interface Emits {
  (e: 'update', tags: string[]): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

// 标签相关
const inputVisible = ref(false)
const inputRef = ref<InstanceType<typeof ElInput>>()
const inputValue = ref('')
const handleClose = (tag: string) => {
  // use concat() to copy the array to avoid modification
  const tempTags = props.tags.concat()
  tempTags.splice(tempTags.indexOf(tag), 1)
  emit('update', tempTags)
}

const showInput = () => {
  inputVisible.value = true
  nextTick(() => {
    inputRef.value!.input!.focus()
  })
}

const handleInputConfirm = () => {
  if (props.tags.indexOf(inputValue.value) > -1) {
    ElMessage.warning('标签已存在')
    return
  }
  if (inputValue.value) {
    // check valid characters
    const reg = /^[a-zA-Z0-9\u4e00-\u9fa5]+$/
    if (!reg.test(inputValue.value)) {
      ElMessage.warning('标签只能包含中文、英文和数字')
      return
    }
    const tempTags = props.tags.concat()
    tempTags.push(inputValue.value)
    emit('update', tempTags)
  }
  inputVisible.value = false
  inputValue.value = ''
}

</script>

<template>
  <el-space wrap>
    <template v-if="modify">
      <el-tag
        v-for="tag in tags"
        :key="tag"
        effect="plain"
        class="mx-1"
        closable
        :disable-transitions="false"
        @close="handleClose(tag)"
      >
        {{ tag }}
      </el-tag>
      <el-input
        v-show="tags.length < 10"
        v-if="inputVisible"
        ref="inputRef"
        v-model="inputValue"
        class="ml-1 w-20"
        size="small"
        style="width: 60px"
        @keyup.enter="handleInputConfirm"
        @blur="handleInputConfirm"
      />
      <el-button
        v-else
        v-show="tags.length < 10"
        class="button-new-tag ml-1"
        size="small"
        @click="showInput"
      >
        + 新标签
      </el-button>
    </template>
    <template v-else>
      <el-tag
        v-for="tag in tags"
        :key="tag"
        class="mx-1"
        effect="plain"
      >
        {{ tag }}
      </el-tag>
    </template>
  </el-space>
</template>
