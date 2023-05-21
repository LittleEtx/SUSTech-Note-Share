<template>
<div v-loading="loading" element-loading-background="rgba(255, 255, 255, 0.3)">
  <div style="text-align: right">
    <el-button type="primary" @click="showCreateNotebook = true">
      <el-icon style="margin-right: 10px">
        <CirclePlus/>
      </el-icon>
      新建笔记本
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
        :default-active="selectedDir"
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
      <el-space
        v-if="notebookByDirs.get(selectedDir)?.length > 0"
        :size="20" wrap
        style="padding-left: 20px"
      >
        <div
          v-for="(notebook, index) in notebookByDirs.get(selectedDir)"
          :key="index"
          style="position: relative"
        >
          <notebook-card :notebook="notebook"></notebook-card>
          <el-popover trigger="click">
            <template #reference>
              <el-button
                text type="info"
                :icon="MoreFilled"
                style="position: absolute; right: 0; bottom: 0;"
              ></el-button>
            </template>
            <el-text size="small">
              <el-icon>
                <Edit/>
              </el-icon>
              切换分区
            </el-text>
            <br>
            <el-select
              filterable
              allow-create
              default-first-option
              v-model="notebook.directory"
              @change="changeDir(notebook)"
              placeholder="选择分区或输入新分区"
            >
              <el-option
                v-for="item in notebookByDirs.keys()"
                :key="item"
                :label="item"
                :value="item"
              ></el-option>
            </el-select>
          </el-popover>
        </div>
      </el-space>
      <el-empty v-else description="没有笔记本可以展示"></el-empty>
    </el-main>
  </el-container>
</div>
</template>

<script setup lang="ts">
import { NotebookInfo } from '@/scripts/interfaces'
import { onBeforeMount, ref } from 'vue'
import NotebookCard from '@/components/NotebookCard.vue'
import { apiGetUserNotebooks } from '@/scripts/API_Center'
import { CirclePlus, Edit, Folder, MoreFilled } from '@element-plus/icons-vue'
import NewNotebook from '@/components/personal_center/NewNotebook.vue'
import { ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { apiUpdateBasicInfo } from '@/scripts/API_Notebook'

const router = useRouter()

const showCreateNotebook = ref(false)

const notebookByDirs = ref<Map<string, NotebookInfo[]>>(new Map())
const selectedDir = ref<string>('')
// loading before data is loaded
const loading = ref<boolean>(true)

const updateNotebook = async () => {
  notebookByDirs.value.clear()
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
}

onBeforeMount(async () => {
  await updateNotebook()
  selectedDir.value = notebookByDirs.value.keys().next().value
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

// edit notebook
const changeDir = async (notebook: NotebookInfo) => {
  loading.value = true
  await apiUpdateBasicInfo(notebook.notebookID,
    { directory: notebook.directory })
  await updateNotebook()
  loading.value = false
}

</script>

<style scoped>
.dir-item-menu {
  white-space: nowrap;  /* 1.不换行 */
  overflow: hidden;  /* 2.超出的部分隐藏 */
  text-overflow: ellipsis; /* 3.文字用省略号替代超出的部分 */
}

</style>
