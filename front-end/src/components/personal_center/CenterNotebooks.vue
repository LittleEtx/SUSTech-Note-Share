<template>
<div v-loading="loading" element-loading-background="rgba(255, 255, 255, 0.3)">
  <div style="text-align: right">
    <el-button type="primary" @click="showCreateNotebook = true">
      <el-icon><CirclePlus /></el-icon> 新建笔记本
    </el-button>
    <el-dialog
      v-model="showCreateNotebook"
      style="text-align: center"
      title="新建笔记本"
      show-close
      :close-on-click-modal="false"
      destroy-on-close
    >
      <new-notebook
        :directories="notebookByDirs.keys()"
        :default-dir="selectedDir"
        ref="newNotebookRef"
      ></new-notebook>
      <div style="text-align: right; margin-right: 20px">
        <el-button style="width: 30%" @click="showCreateNotebook = false">取消</el-button>
        <el-button type="primary" @click="submitCreateNotebook" style="width: 30%">
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
  <el-container>
    <el-aside width="180px">
      <!--   左侧边栏展示所有文件夹   -->
      <el-menu
        class="el-menu-vertical-demo"
        :default-active="notebookByDirs.keys().next().value"
        @open="selectedDir = $event"
        @select="selectedDir = $event"
      >
        <el-menu-item
          v-for="(dir, index) in notebookByDirs.keys()"
          :key="index"
          :index="dir"
          class="dir-item-menu"
        >
          <el-icon>
            <Folder/>
          </el-icon>
          {{ dir }}
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-main style="text-align: left">
      <!--   右侧展示所有卡片   -->
      <el-space :size="20" wrap style="padding-left: 20px">
        <div
          v-for="(notebook, index) in notebookByDirs.get(selectedDir)"
          :key="index"
          style="position: relative"
        >
          <notebook-display :notebook="notebook"></notebook-display>
          <el-icon type="info" class="more-icon">
            <MoreFilled/>
          </el-icon>
        </div>
      </el-space>
    </el-main>
  </el-container>
</div>
</template>

<script setup lang="ts">
import { NotebookInfo } from '@/scripts/interfaces'
import { onBeforeMount, ref } from 'vue'
import NotebookDisplay from '@/components/NotebookCard.vue'
import { apiGetUserNotebooks } from '@/scripts/API_Center'
import { CirclePlus, Folder, MoreFilled } from '@element-plus/icons-vue'
import NewNotebook from '@/components/personal_center/NewNotebook.vue'
import { ElMessageBox } from 'element-plus'
import { router } from '@/router'

const showCreateNotebook = ref(false)

const notebookByDirs = ref<Map<string, NotebookInfo[]>>(new Map())
const selectedDir = ref<string>('')
// loading before data is loaded
const loading = ref<boolean>(true)
onBeforeMount(async () => {
  const notebookInfos = await apiGetUserNotebooks()
  notebookInfos.forEach((notebookInfo) => {
    const dir = notebookInfo.directory
    if (notebookByDirs.value.has(dir)) {
      notebookByDirs.value.get(dir)!.push(notebookInfo)
    } else {
      notebookByDirs.value.set(dir, [notebookInfo])
    }
  })
  loading.value = false
})

// submit create notebook
const newNotebookRef = ref<InstanceType<typeof NewNotebook>>()
const submitCreateNotebook = async () => {
  try {
    const id = await newNotebookRef.value!.submit()
    showCreateNotebook.value = false
    // 跳转到新建的笔记本
    await router.push({ name: 'notebook', params: { id } })
  } catch (e) {
    if (e.response?.status === 400) {
      await ElMessageBox.alert('无法创建笔记本：' + e.response.data.message)
    }
    // else 表单未验证通过
  }
}

</script>

<style scoped>
.dir-item-menu {
  white-space: nowrap;  /* 1.不换行 */
  overflow: hidden;  /* 2.超出的部分隐藏 */
  text-overflow: ellipsis; /* 3.文字用省略号替代超出的部分 */
}

.more-icon {
  position: absolute;
  right: 10px;
  bottom: 10px;
}
</style>
