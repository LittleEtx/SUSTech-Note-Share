<template>
  <div>
    <el-menu
      ref="menuRef"
      unique-opened
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
          <el-icon>
            <Folder/>
          </el-icon>
          {{ note.name }}
        </template>
        <el-menu-item :index="note.id + 'create'">
          <el-button link @click="onClickCreateFile(note.id)">
            <el-icon>
              <Upload/>
            </el-icon>
            <span>上传/创建文件</span>
          </el-button>
        </el-menu-item>
        <el-menu-item v-for="file in note.files" :key="file.name" :index="file.id">
          {{ file.name }}
        </el-menu-item>
      </el-sub-menu>

    </el-menu>
  </div>
</template>

<script setup lang="ts">
import { apiCreateNote, apiGetNoteInfos, NoteInfo } from '@/scripts/API_Notebook'
import { Folder, Plus, Upload } from '@element-plus/icons-vue'
import { ElInput, ElMenu } from 'element-plus'
import { nextTick, onBeforeMount, ref, watch } from 'vue'
import type { FileInfo } from '@/scripts/interfaces'

interface Props {
  canModify: boolean
  notebookId?: string
}

const props = defineProps<Props>()

interface Emit {
  (e: 'onSelectFile', file: FileInfo): void
}

const emit = defineEmits<Emit>()

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
const onClickCreateFile = (noteID: string) => {

}

</script>

<style scoped>

</style>
