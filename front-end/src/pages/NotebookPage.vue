<template>
<div>
  <h5> {{ '当前笔记本: ' + router.params.notebookID }} </h5>
</div>
</template>

<script setup lang="ts">
import { onBeforeMount } from 'vue'
import { useRoute } from 'vue-router'

const router = useRoute()
onBeforeMount(() => {
  console.log(router.params.notebookID)
})
</script>

<style scoped>

</style>
