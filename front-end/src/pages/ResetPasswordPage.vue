<template>
  <div>
    <img :src="SUSTechNoteShareIcon" alt=""
         style="width: 150px; margin-top: 30px; cursor: pointer"
         @click="$router.push('home')"
    >
    <h4 style="text-align: center">找回密码</h4>
    <reset-password class="reset-main"></reset-password>
  </div>
</template>

<script>
import SUSTechNoteShareIcon from '@/assets/icon/icon_with_word.svg'
import ResetPassword from '@/components/login_page/ResetPassword.vue'

export default {
  name: 'ResetPasswordPage',
  components: { ResetPassword },
  computed: {
    SUSTechNoteShareIcon () {
      return SUSTechNoteShareIcon
    }
  }
}
</script>

<style scoped>
.reset-main {
  max-width: 800px;
  margin: 0 auto;
}
</style>
