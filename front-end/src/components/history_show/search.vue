<template>
  <div>
    <el-input
        v-model="input"
        placeholder="Please input"
        clearable
        :style="{
         textAlign: 'center',
        fontSize: '20px',
        height: '130px',
        padding: '20px',
        marginBottom: '50px',
        marginLeft: '50px',
        marginRight: '50px',
        marginTop: '50px'
      }">
      <template #prefix>
        <el-icon  @click="search()" ><Search /></el-icon>
      </template>
    </el-input>
  </div>
  <div style="text-align: left">
    <el-space :size="20" wrap v-if="relatedNotebooks.length > 0">
      <notebook-card
          v-for="(notebook, index) in relatedNotebooks"
          :key="index" :notebook="notebook"
      ></notebook-card>
    </el-space>
    <el-empty v-else description="找不到相关的笔记"></el-empty>
  </div>

</template>

<script>
import axios from "axios";
import {apiGetBasicInfo} from "@/scripts/API_Notebook";
import NotebookCard from "@/components/NotebookCard.vue";

export default {
  name: "search",
  components: {NotebookCard},
  data(){
    return {
      input: "",
      relatedNotebooks: []
    }
  },

  methods:{
    search(){
      this.relatedNotebooks = []
      axios.get('/api/search/notebook', {
        params: {
          key: this.input,
        }
      }).then(res => {
        for (let i = 0; i < res.data.length; i++) {
          const notebook = apiGetBasicInfo(res.data[i].notebookID)
          this.relatedNotebooks.push(notebook)
        }
        console.log("111111111111111")
        console.log(this.relatedNotebooks)
      })
    },
  }
}
</script>

<style>
.time {
  font-size: 12px;
  color: #999;
}

.bottom {
  margin-top: 60px;
  line-height: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.button {
  padding: 0;
  min-height: auto;
}

.image {
  width: 100%;
  display: block;
}
</style>
