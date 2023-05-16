<template>
<div>
  <upload-files :note="selectedNote!" v-model="showUploadDialog"></upload-files>
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
          <el-icon>
            <Folder/>
          </el-icon>
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
      <el-menu-item v-for="file in note.files" :key="file.name" :index="file.id">
        <span class="text-truncated">
          {{ file.name }}
        </span>
      </el-menu-item>
    </el-sub-menu>
  </el-menu>
</div>
</template>

<script setup lang="ts">
import { apiCreateNote, apiGetNoteInfos } from '@/scripts/API_Notebook'
import { Folder, Plus, Upload } from '@element-plus/icons-vue'
import { ElInput, ElMenu } from 'element-plus'
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
  infos.sort((a, b) => a.name.localeCompare(b.name))
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
  showCreateNote.value = false
  loading.value = false
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
