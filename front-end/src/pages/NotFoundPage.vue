<script setup lang="ts">
import MainHeader from '@/components/MainHeader.vue'
import { router } from '@/router'
</script>
<template>
  <div>
    <div class="header">
      <main-header></main-header>
    </div>
    <h1>
      404 Not Found
    </h1>
    <span>
    糟糕，你访问的页面不存在！
  </span>
    <div style="margin-top: 20px">
      <el-button
        type="primary"
        @click="router.replace('home')"
        style="width: 120px"
      >
        返回首页
      </el-button>
    </div>
  </div>
</template>

<style scoped>

</style>
