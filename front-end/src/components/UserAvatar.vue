<script setup lang="ts">

interface Props {
  userId?: number,
  avatarUrl?: string,
  size?: number | 'large' | 'default' | 'small'
}
withDefaults(defineProps<Props>(), {
  size: 'default'
})

</script>

<template>

  <el-avatar :src="avatarUrl!" :size="size!">
    <span style="white-space: nowrap">{{ userId }}</span>
  </el-avatar>
</template>
