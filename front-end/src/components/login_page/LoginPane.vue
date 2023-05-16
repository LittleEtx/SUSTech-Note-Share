<template>
  <div v-loading="loading" element-loading-background="rgba(255, 255, 255, 0.3)">
    <h4 class="main-blue-color" style="text-align: center">登录</h4>
    <el-tabs v-model="activeName" :stretch="true" type="card">
      <el-tab-pane label="密码登录" name="password"></el-tab-pane>
      <el-tab-pane label="验证码登录" name="code"></el-tab-pane>
    </el-tabs>
    <div class="pane-style">
      <template v-if="activeName === 'password'">
          <!--   密码登录选框     -->
        <email-form ref="passEmailForm"></email-form>
        <password-form ref="passwordForm"></password-form>
        <el-checkbox v-model="rememberMe">记住我</el-checkbox>
        <br /> <el-text type="info" size="small">勾选后在该设备上将自动登录</el-text>
        <p>
          <el-button class="button-size" type="primary" @click="loginViaPassword">登录</el-button>
          <el-button class="button-size" @click="register">注册</el-button>
        </p>
      </template>
      <template v-else>
        <!--   验证码登录选框     -->
        <email-form ref="codeEmailForm"></email-form>
        <code-form ref="codeForm" @send-code="getEmailValidateCode"></code-form>
        <el-checkbox v-model="rememberMe">记住我</el-checkbox>
        <div> <el-text type="info" size="small">勾选后在该设备上将自动登录</el-text> </div>
        <div style="margin-top: 20px">
          <el-button
            class="button-size" type="primary"
            @click="loginViaCode()"
          >
            登录/注册
          </el-button>
        </div>
        <div> <el-text type="info" size="small">未注册的邮箱将自动注册</el-text> </div>
      </template>
    </div>
  </div>
</template>

<script>
import { router } from '@/router'
import { apiLoginViaCode, apiLoginViaPassword, apiSendEmailCode } from '@/scripts/API_Auth'
import PasswordForm from './PasswordForm.vue'
import EmailForm from './EmailForm.vue'
import CodeForm from './CodeForm.vue'
import { store } from '@/store/store'

async function onSuccessLogin () {
  await store.dispatch('updateInfo')
  await router.push({ name: 'home' })
}

export default {
  components: { CodeForm, PasswordForm, EmailForm },
  data () {
    return {
      loading: false,
      activeName: 'email',
      rememberMe: false
    }
  },
  methods: {
    // submit by pwd
    async loginViaPassword () {
      try {
        await this.$refs.passEmailForm.validate()
        await this.$refs.passwordForm.validate()
      } catch (e) {
        return
      }
      try {
        this.loading = true
        await apiLoginViaPassword(
          this.$refs.passEmailForm.email,
          this.$refs.passwordForm.password,
          this.rememberMe
        )
        await onSuccessLogin()
      } catch (e) {
        if (e.response.status === 400) {
          this.$refs.passwordForm.clear()
          await this.$alert('邮箱或密码错误，请重新登录！')
        } else {
          console.log(e)
        }
      }
      this.loading = false
    },
    // submit by emailCode
    async loginViaCode () {
      try {
        await this.$refs.codeEmailForm.validate()
        await this.$refs.codeForm.validate()
      } catch (e) {
        return
      }
      try {
        this.loading = true
        await apiLoginViaCode(
          this.$refs.codeEmailForm.email,
          this.$refs.codeForm.code,
          this.rememberMe
        )
        await onSuccessLogin()
      } catch (e) {
        if (e.response.status === 400) {
          this.$refs.codeForm.emailCode = ''
          await this.$alert('验证码错误，请重新登录！')
        } else {
          console.log(e)
        }
      }
      this.loading = false
    },
    async getEmailValidateCode () {
      try {
        await this.$refs.codeEmailForm.validate()
      } catch (e) {
        return
      }
      try {
        this.$refs.codeForm.wait()
        await apiSendEmailCode(this.$refs.codeEmailForm.email)
      } catch (err) {
        console.log('Fail to send email code !' + err)
      }
    },
    register () {
      this.activeName = 'second'
    }
  }
}
</script>

<style scoped>

.pane-style {
  padding: 10px 60px 10px 60px;
}

.button-size {
  width: 40%;
}

</style>
