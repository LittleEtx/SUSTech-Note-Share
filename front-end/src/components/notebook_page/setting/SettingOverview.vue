<script setup lang="ts">
import { Share, User, View } from '@element-plus/icons-vue'
import type { GroupInfo, NotebookInfo, UserInfo } from '@/scripts/interfaces'

interface Props {
  notebookInfo: NotebookInfo | undefined
  sharedUsers: UserInfo[]
  sharedGroups: GroupInfo[]
}

defineProps<Props>()

interface Emits {
  (e: 'jumpUserShare'): void
  (e: 'jumpGroupShare'): void
  (e: 'jumpVisibility'): void
}

const emit = defineEmits<Emits>()

</script>

<template>
  <el-space :size="10" wrap>
    <!--    笔记本公开性卡片    -->
    <el-card class="info-card" shadow="hover">
      <template #header>
    <span>
      <el-text size="small">
        {{ notebookInfo?.isPublic ? '公开' : '私有' + '笔记本' }}
      </el-text>
      <el-icon style="float: right"><View/></el-icon>
    </span>
      </template>
      <el-text size="small">
        {{
          notebookInfo?.isPublic ?
              '这是一个公开的笔记本，所有人都可以访问' :
              '这是一个私有的笔记本，只有被你分享给的用户和群组成员才能访问'
        }}
      </el-text>
      <div style="position: absolute; bottom: 15px">
        <el-link
            type="primary"
            style="font-size: 10px"
            @click="emit('jumpVisibility')"
        >修改可见度
        </el-link>
      </div>
    </el-card>
    <el-card
      class="info-card" shadow="hover"
      v-if="sharedUsers.length > 0"
    >
      <template #header>
        <el-text size="small">直接访问</el-text>
        <el-icon style="float: right">
          <Share/>
        </el-icon>
      </template>
      <el-text size="small">
        这个笔记本被分享给了 <br/>
        <span style="font-size: 25px">{{ sharedUsers.length }}</span>个用户 <br/>
        他们可以直接访问这个笔记本
      </el-text>
      <div style="position: absolute; bottom: 15px">
        <el-link
          type="primary"
          style="font-size: 10px"
          @click="emit('jumpUserShare')"
        >管理
        </el-link>
      </div>
    </el-card>
    <el-card
      class="info-card" shadow="hover"
      v-if="sharedGroups.length > 0"
    >
      <template #header>
        <el-text size="small">通过群组访问</el-text>
        <el-icon style="float: right">
          <User/>
        </el-icon>
      </template>
      <el-text size="small">
        这个笔记本被分享给了 <br/>
        <span style="font-size: 25px">{{ sharedGroups.length }}</span>个群组 <br/>
        群组成员可以访问这个笔记本
      </el-text>
      <div style="position: absolute; bottom: 15px">
        <el-link
          type="primary"
          style="font-size: 10px"
          @click="emit('jumpGroupShare')"
        >管理
        </el-link>
      </div>
    </el-card>
  </el-space>
</template>

<style scoped>
.info-card {
  width: 200px;
  height: 180px;
  position: relative;
}
</style>
