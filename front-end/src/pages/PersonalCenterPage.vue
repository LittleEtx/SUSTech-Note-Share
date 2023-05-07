<template>
<div>
  <div class="header">
    <main-header></main-header>
  </div>
  <user-display :id="$router.params.userId"></user-display>
</div>
</template>

<script>
import MainHeader from '../components/MainHeader.vue'
import UserDisplay from '../components/personal_center/UserDisplay.vue'

export default {
  name: 'PersonalCenterPage',
  components: { UserDisplay, MainHeader }
}
</script>

<style scoped>

</style>
