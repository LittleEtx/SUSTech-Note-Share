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
        <email-form ref="emailForm" ></email-form>
        <password-form ref="passwordForm"></password-form>
        <el-checkbox v-model="rememberMe">记住我</el-checkbox>
        <p>
          <el-button class="button-size" type="primary" @click="loginViaPassword">登录</el-button>
          <el-button class="button-size" @click="register">注册</el-button>
        </p>
      </template>
      <template v-else>
        <!--   验证码登录选框     -->
        <email-form ref="emailForm" ></email-form>
        <code-form ref="codeForm" @send-code="getEmailValidateCode"
        ></code-form>
        <el-checkbox v-model="rememberMe">记住我</el-checkbox>
        <p>
          <el-button class="button-size" type="primary" @click="loginViaCode('emailLogForm')">登录/注册</el-button>
        </p>
      </template>
    </div>
  </div>
</template>

<script>
import {router} from '@/router'
import {apiLoginViaCode, apiLoginViaPassword, apiSendEmailCode} from '@/scripts/API_Auth'
import PasswordForm from './PasswordForm.vue'
import EmailForm from './EmailForm.vue'
import CodeForm from './CodeForm.vue'

export default {
  components: { CodeForm, PasswordForm, EmailForm },
  data () {
    return {
      loading: false,
      activeName: 'password',
      rememberMe: false
    }
  },
  methods: {
    // submit by pwd
    async loginViaPassword () {
      try {
        await this.$refs.emailForm.validate()
        await this.$refs.passwordForm.validate()
      } catch (e) {
        return
      }
      try {
        this.loading = true
        await apiLoginViaPassword(
          this.$refs.emailForm.email,
          this.$refs.passwordForm.password,
          this.rememberMe
        )
        // login success
        router.push('home')
      } catch (e) {
        if (e.response.status === 400) {
          alert('邮箱或密码错误，请重新登录！')
          this.$refs.passwordForm.clear()
        } else {
          console.log(e)
        }
      }
      this.loading = false
    },
    // submit by emailCode
    async loginViaCode () {
      try {
        await this.$refs.emailForm.validate()
        await this.$refs.codeForm.validate()
      } catch (e) {
        return
      }
      try {
        this.loading = true
        await apiLoginViaCode(
          this.$refs.emailForm.email,
          this.$refs.codeForm.code,
          this.rememberMe
        )
        router.push('home') // on success
      } catch (e) {
        if (e.response.status === 400) {
          alert('验证码错误，请重新登录！')
          this.$refs.codeForm.emailCode = ''
        } else {
          console.log(e)
        }
      }
      this.loading = false
    },
    async getEmailValidateCode () {
      try {
        await this.$refs.emailForm.validate()
      } catch (e) {
        return
      }
      try {
        this.$refs.codeForm.wait()
        await apiSendEmailCode(this.$refs.emailForm.ID + '@' + this.$refs.emailForm.region)
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
