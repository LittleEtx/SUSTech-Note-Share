<template>
  <div>
    <el-container style="height: 90vh" class="common-layout">
      <el-aside width="200px">
        <el-scrollbar>
          <notebook-file-list
            ref="fileList"
            :can-modify="canModify"
            :notebook-id="notebookId"
            @on-select-file="(file, note) => onSelectFile(file, note)"
            @on-delete-file="file => deleteFile(file)"
            @on-download-file="file => downloadFile(file)"
            @on-edit-file="(file, note) => editFile(file, note)"
          ></notebook-file-list>
        </el-scrollbar>
      </el-aside>
      <template v-if="!currentFile">
        <el-main>
          <el-empty description="选择文件以预览"/>
        </el-main>
      </template>
      <template v-else>
        <el-container>
          <el-header style="text-align: left">
            <div style="margin-top: 15px"></div>
            <span style="float: right">
            <el-button text :icon="Edit" @click="editFile(currentFile!, currentNote!)" size="small"></el-button>
            <el-button text :icon="Download" @click="downloadFile(currentFile!)" size="small"></el-button>
            <el-button text :icon="Delete" @click="deleteFile(currentFile!)" size="small"></el-button>
          </span>
            <div style="margin-left: 10%; width: 60%">
              <el-text
                v-if="!editingFileName"
                @click="onEditFileName"
                truncated
                style="font-size: 20px"
              ><b>{{ currentFile?.name }}</b></el-text>
              <!--     修改标题     -->
              <el-input
                v-else
                v-model="currentFile!.name"
                ref="fileNameInputRef"
                placeholder="请输入标题"
                @blur="onConfirmEditFileName"
                @keyup.enter="onInputEnter"
                @keyup.esc="onCancelEditFileName"
                :maxlength="200"
                show-word-limit
              ></el-input>
            </div>
          </el-header>
          <el-main>
            <file-display
              :file="currentFile!"
              :can-modify="canModify">
            </file-display>
          </el-main>
        </el-container>
      </template>
    </el-container>
    <!--  修改文件信息弹窗  -->
    <el-dialog
      v-model="showEditFileInfo"
      title="编辑文件信息"
      style="width: 500px"
      center
    >
      <el-form label-position="top" :model="editNoteInfo">
        <el-form-item label="文件名">
          <el-input
            v-model="editNoteInfo!.name"
            maxlength="200"
            show-word-limit
            placeholder="这个文件的名字"
          ></el-input>
        </el-form-item>
        <el-form-item label="所属文件夹">
          <el-select v-model="editNoteInfo!.noteID" placeholder="请选择所属文件夹">
            <el-option
              v-for="note in notes"
              :key="note.id"
              :label="note.name"
              :value="note.id"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="showEditFileInfo = false" style="width: 30%">取 消</el-button>
        <el-button type="primary" @click="submitEditFileInfo" style="width: 30%">确 定</el-button>
      </span>
      </template>
    </el-dialog>
  </div>

</template>
<script setup lang="ts">
import FileDisplay from '@/components/notebook_page/FileDisplay.vue'
import NotebookFileList from '@/components/notebook_page/NotebookFileList.vue'
import { computed, nextTick, ref } from 'vue'
import { FileInfo, NoteInfo } from '@/scripts/interfaces'
import { apiDeleteFile, apiMoveFile, apiRenameFile } from '@/scripts/API_Notebook'
import { ElDialog, ElForm, ElInput, ElMessageBox } from 'element-plus'
import { downloadFile as download } from '@/scripts/Utils'
import { Delete, Download, Edit } from '@element-plus/icons-vue'

interface Props {
  notebookId: string
  canModify: boolean
}

defineProps<Props>()
const fileList = ref<InstanceType<typeof NotebookFileList>>(null)
const currentFile = ref<FileInfo>()
const currentNote = ref<NoteInfo>()
const editingFileName = ref(false)
const onSelectFile = (file: FileInfo, note: NoteInfo) => {
  console.log('onSelectFile', file, note)
  currentFile.value = file
  currentNote.value = note
  editingFileName.value = false // 切换时取消输入
}

const notes = computed(() => {
  return fileList.value.notes
})

// ============ 编辑文件名 ===============
const tempFileName = ref('')
const fileNameInputRef = ref<InstanceType<typeof ElInput>>(null)
const onInputEnter = (e: InputEvent) => {
  // 当按下回车时，触发blur效果，即上传新建笔记
  (e.target as HTMLInputElement).blur()
}

const onEditFileName = () => {
  editingFileName.value = true
  tempFileName.value = currentFile.value!.name
  nextTick(() => {
    fileNameInputRef.value.input.focus()
  })
}

const onConfirmEditFileName = async () => {
  await apiRenameFile(currentFile.value!.id, currentFile.value!.name)
  editingFileName.value = false
}

const onCancelEditFileName = () => {
  currentFile.value!.name = tempFileName.value
  editingFileName.value = false
}

// =============文件信息修改=================
const showEditFileInfo = ref(false)
const deleteFile = async (file: FileInfo) => {
  try {
    await ElMessageBox.confirm('确定删除此文件？该文件将被永久移除', '删除文件', {
      confirmButtonText: '确定',
      confirmButtonClass: 'el-button--danger',
      cancelButtonText: '取消',
      cancelButtonClass: 'el-button--default',
      type: 'warning'
    })
  } catch (e) {
    // 取消
    return
  }
  // 确认
  await apiDeleteFile(file.id)
  await fileList.value.updateFileList()
  // 如果删除的是当前预览文件，清空
  if (currentFile.value?.id === file.id) {
    currentFile.value = undefined
    currentNote.value = undefined
  }
}
const downloadFile = (file: FileInfo) => {
  download(file.url, file.name)
}

const editNoteInfo = ref<{
  name: string
  id: string
  noteID: string
}>()

const editFile = (file: FileInfo, note: NoteInfo) => {
  editNoteInfo.value = {
    name: file.name,
    id: file.id,
    noteID: note.id
  }
  showEditFileInfo.value = true
}

const submitEditFileInfo = async () => {
  showEditFileInfo.value = false
  await apiRenameFile(editNoteInfo.value!.id, editNoteInfo.value!.name)
  await apiMoveFile(editNoteInfo.value!.id, editNoteInfo.value!.noteID)
  await fileList.value.updateFileList()
}

</script>
<style scoped>

</style>
