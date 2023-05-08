<script setup lang="ts">
import { useStore } from '@/store/store'
import { computed, ref } from 'vue'

interface Props {
  userId?: number,
  avatarUrl?: string,
  size?: number | 'large' | 'default' | 'small'
}
const props = defineProps<Props>()
const useID = ref(true)

const store = useStore()
const id = computed(() => props.userId || store.state.userInfo?.userID)
const url = computed(() => props.avatarUrl || store.state.userInfo?.avatar)
const onUrlFail = () => {
  useID.value = true
}
</script>

<template>

<el-avatar :src="url!" @error="onUrlFail" :size="size!">
  <template v-if="useID">
    {{ id }}
  </template>
</el-avatar>
</template>
