<template>
<el-row>
  <el-col>
    <el-upload
      ref="uploader"
      style="margin: 0 auto"
      action="#"
      accept="image/jpeg,image/jpg,image/png"
      :with-credentials="true"
      :limit="1"
      :auto-upload="false"
      :show-file-list="false"
      :on-exceed="handleExceed!"
      :on-change="handleChange!"
      :before-upload="checkImage!"
      :http-request="uploadAvatar!"
    >
      <el-avatar v-if="imageUrl" class="avatar-size" :src="imageUrl" alt=""/>
      <div v-else class="avatar-size avatar-waiting">
        <el-icon size="50px"><Plus /></el-icon>
        <div>
          点击上传头像
        </div>
      </div>
      <template #tip>
        <div class="el-upload__tip">
          请上传 2Mb 以内的 jpg/png 文件
        </div>
      </template>
    </el-upload>
  </el-col>

</el-row>
</template>

<script setup lang="ts">
import {Plus} from '@element-plus/icons-vue'




</script>

<style scoped>
</style>