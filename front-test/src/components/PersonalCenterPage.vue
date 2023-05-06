<template>
<div>
  <div class="header">
    <main-header></main-header>
  </div>
  <user-display :id="$route.params.userId"></user-display>
</div>
</template>

<script>
import MainHeader from './subcomponents/MainHeader.vue'
import UserDisplay from './personal_center/UserDisplay.vue'
import router from '../router'

export default {
  name: 'PersonalCenterPage',
  components: { UserDisplay, MainHeader },
  computed: {
    router () {
      return router
    }
  }
}
</script>

<style scoped>

</style>
