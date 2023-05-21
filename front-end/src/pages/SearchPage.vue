<script setup lang="ts">
interface Props {
  q: string
}

defineProps<Props>()
</script>

<template>
  <h4>{{ q }}</h4>
</template>

<style scoped>

</style>