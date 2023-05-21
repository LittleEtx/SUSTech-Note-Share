<template>
  <div
      style="text-align: left"
      v-loading="loading"
      element-loading-background="rgba(255, 255, 255, 0.3)"
  >
    <el-divider style="margin-bottom: 0"></el-divider>
    <!--   展示已经分享给的群组的表格    -->
    <el-table
        style="width: 100%"
        :data="filteredShareGroups"
        @selection-change="onGroupSelectionChange"
    >
      <el-table-column type="selection" width="50"/>
      <el-table-column>
        <template #header>
          <span v-if="!showGroupMultiDelete">全选</span>
          <el-button v-else type="danger" plain @click="deleteMultiGroupShare">
            删除{{ multiSelectGroups.length }}个群组的分享
          </el-button>
        </template>
        <template #default="scope">
          <div style="display: flex">
            <div style="margin-left: 10px">
              <el-text type="primary"><b>{{ (scope.row as GroupInfo).groupName }}</b></el-text>
              <br/>
              <el-text> {{ (scope.row as GroupInfo).groupID }}</el-text>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column>
        <template #header>
          <el-input
              style="float: right"
              placeholder="搜索群组"
              :prefix-icon="Search"
              v-model="searchShareGroups"
              @update:model-value="onUpdateSearchGroups"
          ></el-input>
        </template>
        <template #default="scope">
          <el-link
              :underline="false"
              style="float: right"
              @click="deleteGroupShare(scope.row)"
          >
            <el-icon>
              <Delete/>
            </el-icon>
          </el-link>
        </template>
      </el-table-column>
    </el-table>
    <add-group-share
        v-if="showGroupShareDialog"
        :shared-groups="sharedGroups"
        :notebook-info="notebookInfo!"
        v-model="showGroupShareDialog"
        @on-submit="id => submitGroupShare(id)"
    ></add-group-share>
  </div>
</template>

<script setup lang="ts">
import { Delete, Search } from '@element-plus/icons-vue'
import {GroupInfo, NotebookInfo} from '@/scripts/interfaces'
import { computed, nextTick, onMounted, ref, watch } from 'vue'
import {
  apiCancelGroupShare,
  apiGetNotebookSharedGroups, apiShareNotebookToGroup,
} from '@/scripts/API_Interact'
import UserAvatar from '@/components/UserAvatar.vue'
import { ElMessageBox } from 'element-plus'
import AddGroupShare from '@/components/notebook_page/setting/AddGroupShare.vue'

interface Props {
  notebookInfo: NotebookInfo | undefined
  modelValue: GroupInfo[]
}

const props = defineProps<Props>()

interface Emit {
  (e: 'update:modelValue', value: GroupInfo[]): void
}

const emit = defineEmits<Emit>()
const sharedGroups = computed({
  get () {
    return props.modelValue
  },
  set (value) {
    emit('update:modelValue', value)
  }
})

const loading = ref(false)
watch(
    () => props.notebookInfo,
    async () => {
      loading.value = true
      await updateInfo()
      loading.value = false
    }
)
onMounted(async () => {
  loading.value = true
  await updateInfo()
  loading.value = false
})

const searchShareGroups = ref('')
const filteredShareGroups = ref<GroupInfo[]>([])

const onUpdateSearchGroups = () => {
  if (searchShareGroups.value === '') {
    filteredShareGroups.value = sharedGroups.value
  } else {
    filteredShareGroups.value = sharedGroups.value.filter(group =>
        group.groupID.toString().includes(searchShareGroups.value) ||
        group.groupName.includes(searchShareGroups.value)
    )
  }
}

const updateInfo = async () => {
  if (!props.notebookInfo) {
    return
  }
  sharedGroups.value = await apiGetNotebookSharedGroups(props.notebookInfo.notebookID)
  await nextTick()
  onUpdateSearchGroups()
}

// ========= 群组分享 =========
const showGroupShareDialog = ref(false)
const addGroup = () => {
  showGroupShareDialog.value = true
}
defineExpose({
  addGroup
})

const submitGroupShare = async (selectedGroupID: number) => {
  showGroupShareDialog.value = false
  loading.value = true
  await apiShareNotebookToGroup(props.notebookInfo!.notebookID, selectedGroupID)
  await updateInfo()
  loading.value = false
}

const deleteGroupShare = async (group: GroupInfo) => {
  try {
    await ElMessageBox.confirm(`确定要取消分享给群组 ${group.groupName} 吗？`, '确认', {
      confirmButtonText: '确定',
      confirmButtonClass: 'el-button--danger',
      cancelButtonText: '取消',
      type: 'warning'
    })
  } catch (e) {
    return
  }
  loading.value = true
  await apiCancelGroupShare(props.notebookInfo!.notebookID, group.groupID)
  await updateInfo()
  loading.value = false
}

const multiSelectGroups = ref<GroupInfo[]>([])
const showGroupMultiDelete = ref(false)
const onGroupSelectionChange = (groups: GroupInfo[]) => {
  multiSelectGroups.value = groups
  showGroupMultiDelete.value = groups.length > 0
}

const deleteMultiGroupShare = async () => {
  try {
    await ElMessageBox.confirm(`确定要取消分享给这 ${multiSelectGroups.value.length} 个群组吗？`, '确认', {
      confirmButtonText: '确定',
      confirmButtonClass: 'el-button--danger',
      cancelButtonText: '取消',
      type: 'warning'
    })
  } catch (e) {
    return
  }
  loading.value = true
  await Promise.all(multiSelectGroups.value
      .map(group => apiCancelGroupShare(props.notebookInfo!.notebookID, group.groupID)))
  await updateInfo()
  loading.value = false
}
</script>

<style scoped>

</style>
