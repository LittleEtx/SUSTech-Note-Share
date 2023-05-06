<template>
<div>
  <div class="header">
      <main-header></main-header>
  </div>
  <user-display :id="userID"></user-display>

</div>
</template>

<script>
import MainHeader from './subcomponents/MainHeader.vue'
import Notebook from './subcomponents/Notebook.vue'
import UserDisplay from './personal_center/UserDisplay.vue'

export default {
  name: 'HomePage',
  components: {
    UserDisplay,
    Notebook,
    MainHeader
  },
  data () {
    return {
      userID: ''
    }
  },
  async beforeMount () {
    this.userID = await this.$store.getters.userID
  }
}
</script>

<style scoped>

</style>
