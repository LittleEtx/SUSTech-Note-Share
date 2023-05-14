<script setup lang="ts">
import { NoteInfo } from '@/scripts/API_Notebook'

interface Props {
  notes: NoteInfo[]
}

const props = defineProps<Props>()

</script>

<template>
  <div>
    <el-menu>
      <el-sub-menu v-for="note in notes" :key="note.id">
        <template #title>

        </template>
      </el-sub-menu>

    </el-menu>
  </div>
</template>

<style scoped>

</style>
