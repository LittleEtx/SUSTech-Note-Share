<template>
<div>
  <upload-files
    :note="selectedNote!"
    v-model="showUploadDialog"
    @finish-upload="onFinishUpload"
  ></upload-files>
  <el-menu
    ref="menuRef"
    unique-opened
    @select="onSelect"
    v-loading="loading"
    element-loading-background="rgba(255, 255, 255, 0.3)"
  >
    <!--  新建笔记  -->
    <el-menu-item index="create" v-if="canModify">
      <template v-if="!showCreateNote">
        <el-button link @click="onClickCreateNote">
          <el-icon>
            <Plus/>
          </el-icon>
          <span>新建笔记</span>
        </el-button>
      </template>
      <el-input
        v-else
        ref="createNoteInputRef"
        placeholder="请输入笔记名称"
        v-model="newNoteName"
        maxlength="20"
        clearable
        @clear="createNoteCancel"
        @keyup.escape="createNoteCancel"
        @keyup.enter="onInputEnter"
        @blur="createNote"
      ></el-input>
    </el-menu-item>
    <!--  展示所有笔记  -->
    <el-sub-menu v-for="note in notes" :key="note.id" :index="note.id">
      <template #title>
        <span class="text-truncated">
          <el-icon v-if="note.files.length != 0"><Folder/></el-icon>
          <el-icon v-else><FolderOpened/></el-icon>
          {{ note.name }}
        </span>
      </template>
      <el-menu-item :index="note.id + 'create'">
        <el-button link @click="onClickCreateFile(note)">
          <el-icon>
            <Upload/>
          </el-icon>
          <span>上传/创建文件</span>
        </el-button>
      </el-menu-item>
      <template v-for="file in note.files" :key="file.name">
        <el-popover
          placement="left"
          trigger="hover"
          :width="160"
          :show-after="350"
        >
          <template #reference>
            <el-menu-item :index="file.id">
              <span class="text-truncated">
                {{ file.name }}
              </span>
            </el-menu-item>
          </template>
          <!--    弹窗展示文件操作      -->
          <span>
            <el-button text :icon="Edit" @click="editFile(file)" size="small"></el-button>
            <el-button text :icon="Download" @click="downloadFile(file)" size="small"></el-button>
            <el-button text :icon="Delete" @click="deleteFile(file)" size="small"></el-button>
          </span>
        </el-popover>
      </template>
    </el-sub-menu>
  </el-menu>
  <!--  修改笔记本信息  -->
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
          maxlength="40"
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
import { apiCreateNote, apiGetNoteInfos, apiMoveFile, apiRenameFile } from '@/scripts/API_Notebook'
import { Delete, Download, Edit, Folder, FolderOpened, Plus, Upload } from '@element-plus/icons-vue'
import { ElForm, ElInput, ElMenu } from 'element-plus'
import { nextTick, onBeforeMount, ref, watch } from 'vue'
import type { FileInfo, NoteInfo } from '@/scripts/interfaces'
import UploadFiles from '@/components/notebook_page/UploadFiles.vue'

interface Props {
  canModify: boolean
  notebookId?: string
}

const props = defineProps<Props>()

interface Emit {
  (e: 'onSelectFile', file: FileInfo): void
}

const emit = defineEmits<Emit>()
const onSelect = (index: string) => {
  if (index.endsWith('create')) {
    return
  }
  const file = notes.value.flatMap(note => note.files).find(file => file.id === index)
  if (file) {
    emit('onSelectFile', file)
  }
}

const menuRef = ref<InstanceType<typeof ElMenu>>(null)
const loading = ref(false)

// ============ notes ===============
const notes = ref<NoteInfo[]>([])
watch(() => props.notebookId, async () => {
  loading.value = true
  await updateNotesInfo()
  loading.value = false
})
onBeforeMount(async () => {
  loading.value = true
  await updateNotesInfo()
  loading.value = false
})
const updateNotesInfo = async () => {
  if (!props.notebookId) {
    return
  }
  const infos = await apiGetNoteInfos(props.notebookId)
  infos.sort((a, b) => {
    const r = a.name.localeCompare(b.name)
    return r === 0 ? a.id.localeCompare(b.id) : r
  })
  notes.value = infos
}

// =============新建笔记================
const showCreateNote = ref(false)
const newNoteName = ref('')
const createNoteInputRef = ref<InstanceType<typeof ElInput>>()
const onClickCreateNote = () => {
  showCreateNote.value = true
  nextTick(() => {
    createNoteInputRef.value.input!.focus()
  })
}
const createNote = async () => {
  if (newNoteName.value === '') {
    showCreateNote.value = false
    return
  }
  loading.value = true
  const noteID = await apiCreateNote(
    props.notebookId!,
    newNoteName.value,
    true
  )
  await updateNotesInfo()
  loading.value = false
  showCreateNote.value = false
  await nextTick(() => {
    menuRef.value.open(noteID)
  })
}

const onInputEnter = (e: InputEvent) => {
  // 当按下回车时，触发blur效果，即上传新建笔记
  (e.target as HTMLInputElement).blur()
}

const createNoteCancel = () => {
  showCreateNote.value = false
}

// =============新建文件================
const showUploadDialog = ref(false)
const selectedNote = ref<NoteInfo>()
const onClickCreateFile = (note: NoteInfo) => {
  showUploadDialog.value = true
  selectedNote.value = note
}
const onFinishUpload = async () => {
  loading.value = true
  await updateNotesInfo()
  loading.value = false
  showUploadDialog.value = false
}

// TODO:文件移动、重命名、删除；笔记重命名、删除
const showEditFileInfo = ref(false)
const deleteFile = async (file: FileInfo) => {
  console.log('delete file', file)
}
const downloadFile = async (file: FileInfo) => {
  console.log('download file', file)
}

const editNoteInfo = ref<{
  name: string
  id: string
  noteID: string
}>()

const editFile = (file: FileInfo) => {
  editNoteInfo.value = {
    name: file.name,
    id: file.id,
    noteID: notes.value.find(note => note.files.find(f => f.id === file.id))!.id
  }
  showEditFileInfo.value = true
}

const submitEditFileInfo = async () => {
  showEditFileInfo.value = false
  loading.value = true
  await apiRenameFile(editNoteInfo.value!.id, editNoteInfo.value!.name)
  await apiMoveFile(editNoteInfo.value!.id, editNoteInfo.value!.noteID)
  await updateNotesInfo()
  loading.value = false
}

</script>

<style scoped>
.text-truncated {
  display: inline-block;
  max-width: 100%;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}
</style>
