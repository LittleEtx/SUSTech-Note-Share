<template>
<div>
  <el-affix>
    <div class="header">
      <main-header></main-header>
    </div>
  </el-affix>
  <div style="margin-left: 10%; margin-right: 10%; margin-top: 20px">
    <div style="text-align: left">
      <el-text type="primary" style="margin-left: 40px; font-size: 20px">
        <el-icon>
          <Clock/>
        </el-icon>
        <b>历史记录</b>
      </el-text>
      <el-button
        style="float: right"
        :icon="Delete"
        type="danger" plain
        @click="onClearAllHistory"
      >删除全部记录
      </el-button>
    </div>
    <div style="margin-top: 20px"></div>
    <history-timeline ref="timeLineRef"></history-timeline>
  </div>
</div>
</template>

<script lang="ts" setup>
import MainHeader from '../components/MainHeader.vue'
import HistoryTimeline from '@/components/history_show/HistoryTimeline.vue'
import { Clock, Delete } from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'
import { ref } from 'vue'

const timeLineRef = ref<InstanceType<typeof HistoryTimeline>>(null)
const onClearAllHistory = async () => {
  try {
    await ElMessageBox.confirm('确定要删除全部历史记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      confirmButtonClass: 'el-button--danger',
      type: 'warning'
    })
  } catch (e) {
    return
  }
  await timeLineRef.value.clearAll()
}



</script>

<style scoped>

</style>
