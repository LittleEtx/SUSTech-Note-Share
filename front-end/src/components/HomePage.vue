<template>
<div>
  <div class="header">
    <main-header></main-header>
  </div>
</div>
</template>

<script>
import MainHeader from './subcomponents/MainHeader.vue'

export default {
  name: 'HomePage',
  components: {
    MainHeader
  }
}
</script>

<style scoped>
.header {
    position: fixed;
    top: 0;
    width: 100%;
    height: 60px;
    border-bottom: 2px solid #e0e0e0;
}
</style>
