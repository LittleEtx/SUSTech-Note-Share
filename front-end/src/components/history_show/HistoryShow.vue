<template>
  <el-timeline>
    <el-timeline-item v-for="commit in commits" :key="commit.id" :timestamp="commit.timestamp" placement="top">
      <el-card>
        <h4>{{ commit.title }}</h4>
        <p>{{ commit.author }} committed {{ commit.timestamp }} </p>
      </el-card>
    </el-timeline-item>
  </el-timeline>
</template>
<script>
export default {
  name: 'HistoryShow',
  mounted() {
    const userId = this.$route.params.userId;
    console.log(`User ID is: ${userId}`);
    // You can use the userId variable here to fetch data for that specific user
  },
  data() {
    return {
      commits: [
        { id: 1, title: 'Update Github template', author: 'Tom', timestamp: '2018/4/12 20:46' },
        { id: 2, title: 'Update Github template', author: 'Tom', timestamp: '2018/4/3 20:46' },
        { id: 3, title: 'Update Github template', author: 'Tom', timestamp: '2018/4/2 20:46' }
      ]
    };
  }
}
</script>
<style scoped>
.el-timeline-item {
  margin-bottom: 20px;
}

.el-card {
  border: none;
  box-shadow: 0px 2px 12px rgba(0, 0, 0, 0.08);
  border-radius: 6px;
  padding: 20px;
}

.el-card h4 {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
}

.el-card p {
  font-size: 14px;
  color: #999;
  margin: 0;
}

.el-timeline-item::before {
  background-color: #409EFF;
}

.el-timeline-item.is-top::before {
  top: 12px;
}

.el-timeline-item.is-bottom::before {
  bottom: 12px;
}

.el-timeline-item.is-top .el-card {
  margin-top: 0;
}

.el-timeline-item.is-bottom .el-card {
  margin-bottom: 0;
}
</style>