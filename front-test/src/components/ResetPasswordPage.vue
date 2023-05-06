<template>
  <div>
    <img src="../assets/icon/icon_with_word.svg" alt=""
         style="width: 150px; margin-top: 30px; cursor: pointer"
         @click="$router.push('home')"
    >
    <h4 style="text-align: center">找回密码</h4>
    <reset-password class="reset-main"></reset-password>
  </div>
</template>

<script>

import ResetPassword from './login_page/ResetPassword.vue'

export default {
  name: 'ResetPasswordPage',
  components: { ResetPassword },
  beforeMount () {
    console.log('test')
  }
}
</script>

<style scoped>
.reset-main {
  max-width: 800px;
  margin: 0 auto;
}
</style>
