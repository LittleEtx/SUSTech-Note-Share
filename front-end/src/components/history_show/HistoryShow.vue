<template>
  <el-timeline>
    <el-timeline-item v-for="commit in commits" :key="commit.id" :timestamp="commit.visitTime" placement="top">
      <el-card :body-style="{ padding: '0px' }">
        <img
            :src=commit.cover
            class="image"
            @click="$router.push(`/notebook/${commit.notebookID}`)"
        />
        <div style="padding: 14px">
          <p>{{commit.notebookName}}</p>
          <p>{{commit.author}}</p>
        </div>
      </el-card>
    </el-timeline-item>
  </el-timeline>
</template>


<script>
import {store} from '@/store/store'
import axios from "axios";
import {getTags} from "@/scripts/interfaces";
export default {
  name: 'HistoryShow',
  mounted() {
    // this.makeDat()
    this.getData()
    // You can use the userId variable here to fetch data for that specific user
  },
  data() {
    return {
      commits: [],

    };
  },
  methods:{
    getData () {
      console.log(this.commits)
      axios.get('/api/history/getHistory', {
        userID: store.state.userInfo.userID
      }).then(res => {
        this.commits = res.data
        console.log(this.commits)
        for (let i = 0; i < res.data.length; i++) {
          axios.get('/api/user/get-info', {

          }).then(op => {
            console.log(op)
           this.commits[i].author = op.data.userName
          })
        }
      })
      console.log(this.commits)
    },
    makeDat () {
      axios.post('/api/history/createHistory', {
        userID: '12011508',
        notebookID: '12112628_1',
        visitTime: '2023-05-15 23:30:00'
      }).then(res => {
      })
    }
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