<template>
<div>
  <div class="header">
    <main-header></main-header>
  </div>
  <div  style="height: 100%; width: 100%">
    <notebook style="position: absolute; margin: 0 auto"></notebook>
  </div>

</div>
</template>

<script>
import MainHeader from './subcomponents/MainHeader.vue'
import Notebook from './subcomponents/Notebook.vue'

export default {
  name: 'HomePage',
  components: {
    Notebook,
    MainHeader
  }
}
</script>

<style scoped>

</style>
