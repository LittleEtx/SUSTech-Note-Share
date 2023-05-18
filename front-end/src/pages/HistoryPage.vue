<template>
  <div>
<!--    <div class="header">-->
      <main-header></main-header>
<!--    </div>-->
<!--    <HistoryShow></HistoryShow>-->
    <comment></comment>
  </div>
</template>


<script>
import MainHeader from '../components/MainHeader.vue'
import HistoryShow from '../components/history_show/HistoryShow.vue'
import comment from "@/components/history_show/comment.vue";

export default {
  name: 'HistoryPage',
  components: { HistoryShow, MainHeader,comment }
}
</script>

<style scoped>

</style>
