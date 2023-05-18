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
        <el-input
          v-if="editNoteID === note.id"
          :ref="(el) => initRef(note.id, el)"
          placeholder="请输入笔记名称"
          v-model="editNoteName"
          maxlength="20"
          @keyup.escape="editNoteCancel"
          @keyup.enter="onInputEnter"
          @blur="onConfirmEditNote"
          @click.stop
        ></el-input>
        <el-popover
          v-else
          placement="right"
          trigger="hover"
          :show-after="350"
          :width="180"
        >
          <template #reference>
            <span class="text-truncated">
              <el-icon v-if="note.files.length != 0"><Folder/></el-icon>
              <el-icon v-else><FolderOpened/></el-icon>
              {{ note.name }}
            </span>
          </template>
          <span>
            <el-button text :icon="Edit" @click="onClickEditNote(note)" size="small">重命名</el-button>
            <el-button text :icon="Delete" @click="deleteNote(note)" size="small">删除</el-button>
          </span>
        </el-popover>
      </template>
      <!--   上传/创建文件   -->
      <el-menu-item :index="note.id + 'create'">
        <el-button link @click="onClickCreateFile(note)">
          <el-icon>
            <Upload/>
          </el-icon>
          <span>上传/创建文件</span>
        </el-button>
      </el-menu-item>
      <!--   每个文件的按钮   -->
      <template v-for="file in note.files" :key="file.name">
        <el-popover
          placement="right"
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
            <el-button text :icon="Edit" @click="editFile(file, note)" size="small"></el-button>
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
  <!--  确认删除笔记   -->
  <el-dialog
    v-model="showDeleteNoteConfirm"
    title="删除笔记"
    style="width: 500px"
    center
  >
    <template #header>
      <span>确认删除笔记 <b>{{ deleteNoteInfo?.name }}</b> 吗？</span>
    </template>
    <el-text size="small">笔记内的文件将被移至</el-text>
    <p>
      <el-select v-model="targetNoteID">
        <el-option
          v-for="note in deleteTargetNotes"
          :key="note.id"
          :label="note.name"
          :value="note.id"
        ></el-option>
      </el-select>
    </p>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="showDeleteNoteConfirm = false" style="width: 30%">取 消</el-button>
        <el-button type="primary" @click="deleteNoteConfirm" style="width: 30%">确 定</el-button>
      </span>
    </template>
  </el-dialog>
</div>
</template>

<script setup lang="ts">
import {
  apiCreateNote,
  apiDeleteFile,
  apiDeleteNote,
  apiGetNoteInfos,
  apiMoveFile,
  apiRenameFile,
  apiRenameNote
} from '@/scripts/API_Notebook'
import { Delete, Download, Edit, Folder, FolderOpened, Plus, Upload } from '@element-plus/icons-vue'
import { ElDialog, ElForm, ElInput, ElMenu, ElMessage, ElMessageBox } from 'element-plus'
import { nextTick, onBeforeMount, ref, watch } from 'vue'
import type { FileInfo, NoteInfo } from '@/scripts/interfaces'
import UploadFiles from '@/components/notebook_page/UploadFiles.vue'
import { downloadFile as download } from '@/scripts/Utils'

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

// ============ 更新列表信息 ===============
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
  newNoteName.value = ''
}

const onInputEnter = (e: InputEvent) => {
  // 当按下回车时，触发blur效果，即上传新建笔记
  (e.target as HTMLInputElement).blur()
}

const createNoteCancel = () => {
  showCreateNote.value = false
  newNoteName.value = ''
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
  loading.value = true
  await apiDeleteFile(file.id)
  await updateNotesInfo()
  loading.value = false
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
  loading.value = true
  await apiRenameFile(editNoteInfo.value!.id, editNoteInfo.value!.name)
  await apiMoveFile(editNoteInfo.value!.id, editNoteInfo.value!.noteID)
  await updateNotesInfo()
  loading.value = false
}

// ============ 笔记信息修改 ============
const editNoteID = ref('')
const editNoteName = ref('')

const refMap = new Map<string, InstanceType<typeof ElInput>>()
const initRef = (noteID: string, el: InstanceType<typeof ElInput>) => {
  refMap.set(noteID, el)
}
const onClickEditNote = (note: NoteInfo) => {
  editNoteID.value = note.id
  editNoteName.value = note.name
  nextTick(() => {
    refMap.get(note.id)!.input.focus()
  })
}

const editNoteCancel = () => {
  editNoteID.value = ''
}
const onConfirmEditNote = async () => {
  if (editNoteName.value === '') {
    ElMessage.warning('笔记名不能为空')
    return
  }
  if (editNoteName.value === notes.value.find(n => n.id === editNoteID.value)!.name) {
    // 名称未改变
    editNoteID.value = ''
    return
  }
  loading.value = true
  await apiRenameNote(editNoteID.value, editNoteName.value)
  editNoteID.value = ''
  await updateNotesInfo()
  loading.value = false
}

// ============ 删除笔记 ============
const showDeleteNoteConfirm = ref(false)
const deleteNoteInfo = ref<NoteInfo>()
const targetNoteID = ref('')
const deleteTargetNotes = ref<NoteInfo[]>()
const deleteNote = async (note: NoteInfo) => {
  if (note.files.length === 0) {
    // simply delete empty notes
    loading.value = true
    await apiDeleteNote(note.id)
    await updateNotesInfo()
    loading.value = false
    return
  }
  showDeleteNoteConfirm.value = true
  deleteTargetNotes.value = notes.value.filter(n => n.id !== note.id)
  deleteNoteInfo.value = note
}
const deleteNoteConfirm = async () => {
  if (deleteNoteInfo.value?.files.length > 0 && targetNoteID.value === '') {
    try {
      await ElMessageBox.confirm('没有指定笔记中文件的转移笔记，' +
        '该笔记中的所有文件都将被永久删除，确定删除该笔记吗？', '删除笔记', {
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
  }
  showDeleteNoteConfirm.value = false
  loading.value = true
  if (targetNoteID.value.length > 0) {
    await apiDeleteNote(deleteNoteInfo.value!.id, targetNoteID.value)
  } else {
    await apiDeleteNote(deleteNoteInfo.value!.id)
  }
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
