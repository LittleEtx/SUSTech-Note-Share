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
        <el-button
          link @click="onClickCreateNote"
          :icon="FolderAdd"
        >新建笔记
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
        <div v-else>
          <el-popover
            v-if="canModify"
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
          <span v-else class="text-truncated">
            <el-icon v-if="note.files.length != 0"><Folder/></el-icon>
            <el-icon v-else><FolderOpened/></el-icon>
            {{ note.name }}
          </span>
        </div>
      </template>
      <!--   上传/创建文件   -->
      <el-menu-item :index="note.id + 'create'" v-if="canModify">
        <el-popover trigger="click">
          <template #reference>
            <el-button :icon="Plus" link>添加文件</el-button>
          </template>
          <div style="display: grid">
            <span>
               <el-button
                 link style="width: 100%"
                 @click="onClickUploadFile(note)"
                 :icon="Upload"
               >上传文件</el-button>
            </span>
            <span style="margin-top: 10px">
              <el-button
                link style="width: 100%"
                @click="onCreateNewFile(note)"
                :icon="Plus"
              >新建文件</el-button>
            </span>
          </div>
        </el-popover>
        <el-dialog v-model="showNewFileDialog" center>
          <template #header>
            <el-text size="large">新建文件至 {{ selectedNote!.name }}</el-text>
          </template>
          <el-form label-position="top">
            <el-form-item label="文件名称">
              <el-input
                placeholder="请输入文件名称"
                v-model="newFileName"
                maxlength="200"
                show-word-limit
              ></el-input>
            </el-form-item>
          </el-form>
          <template #footer>
            <el-button @click="showNewFileDialog = false" style="width: 30%">取消</el-button>
            <el-button type="primary" @click="onSubmitNewFile" style="width: 30%">确定</el-button>
          </template>
        </el-dialog>
      </el-menu-item>
      <!--   每个文件的按钮   -->
      <template v-for="file in note.files" :key="file.name">
        <el-menu-item :index="file.id">
          <el-popover
            v-if="canModify"
            placement="right"
            trigger="hover"
            :width="160"
            :show-after="350"
          >
            <template #reference>
              <span class="text-truncated">
                {{ file.name }}
              </span>
            </template>
            <!--    弹窗展示文件操作      -->
            <span>
              <el-button text :icon="Edit" @click="emit('onEditFile', file, note)" size="small"></el-button>
              <el-button text :icon="Download" @click="emit('onDownloadFile', file)" size="small"></el-button>
              <el-button text :icon="Delete" @click="emit('onDeleteFile', file)" size="small"></el-button>
            </span>
          </el-popover>
          <div v-else>
            <span class="text-truncated">{{ file.name }}</span>
          </div>
        </el-menu-item>
      </template>
    </el-sub-menu>
  </el-menu>
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
import { apiCreateNote, apiDeleteNote, apiGetNoteInfos, apiRenameNote, apiUploadFile } from '@/scripts/API_Notebook'
import { Delete, Download, Edit, Folder, FolderAdd, FolderOpened, Plus, Upload } from '@element-plus/icons-vue'
import { ElDialog, ElInput, ElMenu, ElMessage, ElMessageBox } from 'element-plus'
import { nextTick, onBeforeMount, ref, watch } from 'vue'
import type { FileInfo, NoteInfo } from '@/scripts/interfaces'
import UploadFiles from '@/components/notebook_page/UploadFiles.vue'

interface Props {
  canModify: boolean
  notebookId: string
}

const props = defineProps<Props>()

interface Emit {
  (e: 'onSelectFile', file: FileInfo, note: NoteInfo): void

  (e: 'onEditFile', file: FileInfo, note: NoteInfo): void

  (e: 'onDownloadFile', file: FileInfo): void,

  (e: 'onDeleteFile', file: FileInfo): void
}

const emit = defineEmits<Emit>()
const onSelect = (index: string) => {
  if (index.endsWith('create')) {
    return
  }
  const note = notes.value.find(note => note.files.some(file => file.id === index))
  if (note) {
    const file = note.files.find(f => f.id === index)
    emit('onSelectFile', file!, note)
  }
}

const menuRef = ref<InstanceType<typeof ElMenu>>(null)
const loading = ref(false)

// ============ 更新列表信息 ===============
const notes = ref<NoteInfo[]>([])
watch(() => props.notebookId, async () => {
  await updateNotesInfo()
})
onBeforeMount(async () => {
  await updateNotesInfo()
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
  for (const info of infos) {
    info.files.sort((a, b) => {
      const r = a.name.localeCompare(b.name)
      return r === 0 ? a.id.localeCompare(b.id) : r
    })
  }
  notes.value = infos
}

defineExpose({
  updateFileList: async () => {
    loading.value = true
    await updateNotesInfo()
    loading.value = false
  },
  notes
})

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
const onClickUploadFile = (note: NoteInfo) => {
  showUploadDialog.value = true
  selectedNote.value = note
}

const showNewFileDialog = ref(false)
const newFileName = ref('')
const onCreateNewFile = (note: NoteInfo) => {
  selectedNote.value = note
  showNewFileDialog.value = true
  newFileName.value = ''
}

const onSubmitNewFile = async () => {
  if (newFileName.value === '') {
    ElMessage.warning('文件名不能为空！')
    return
  }

  loading.value = true
  const file = new File([], newFileName.value)
  await apiUploadFile(selectedNote.value!.id, file)
  await updateNotesInfo()
  loading.value = false
  showNewFileDialog.value = false
}

const onFinishUpload = async () => {
  loading.value = true
  await updateNotesInfo()
  loading.value = false
  showUploadDialog.value = false
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
