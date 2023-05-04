<template>
  <div>
    <el-steps :active="active" :space="400" finish-status="success"  simple>
      <el-step title="确认邮箱"></el-step>
      <el-step title="身份验证"></el-step>
      <el-step title="重置密码"></el-step>
      <el-step title="重置完成"></el-step>
    </el-steps>
    <div class="input-container" v-loading="loading" element-loading-background="rgba(255, 255, 255, 0.3)">
      <template v-if="active === 0">
        <email-form ref="emailForm"></email-form>
        <el-button type="primary" class="next-button" @click="verifyEmail">下一步</el-button>
      </template>
      <template v-else-if="active === 1">
        <email-form ref="emailForm" :disabled="true"></email-form>
        <code-form ref="codeForm" @send-code="getEmailValidateCode"></code-form>
        <p>
          <el-button class="next-button" @click="previous">上一步</el-button>
          <el-button type="primary" class="next-button" @click="verifyCode">下一步</el-button>
        </p>
      </template>
      <template v-else-if="active === 2">
        <renew-password-form ref="passwordForm"> </renew-password-form>
        <el-button type="primary" class="next-button" @click="resetPassword">下一步</el-button>
      </template>
      <template v-else>
        <p style="margin-top: 50px">(*^_^*)新登录密码重置成功，请重新登录！</p>
        <el-button type="primary" @click="logOn">重新登录</el-button>
      </template>
    </div>
  </div>
</template>

<script>
import {apiResetPassVerifyEmail, apiResetPassVerifyCode, apiResetPassword} from '../../scripts/API_Auth'
import EmailForm from './EmailForm.vue'
import CodeForm from './CodeForm.vue'
import PasswordForm from './PasswordForm.vue'
import RenewPasswordForm from './RenewPasswordForm.vue'

export default {
  name: 'ResetPassword',
  components: {RenewPasswordForm, PasswordForm, CodeForm, EmailForm},
  data () {
    return {
      active: 0,
      token: '',
      loading: false
    }
  },

  methods: {
    previous () {
      this.active--
    },
    async verifyEmail () {
      try {
        await this.$refs.emailForm.validate()
      } catch (e) {
        return
      }
      this.loading = true
      try {
        await apiResetPassVerifyEmail(this.$refs.emailForm.email)
        this.active++
        await this.$nextTick()
        this.$refs.codeForm.wait() // already sent email, start waiting
      } catch (e) {
        if (e.response.status === 400) {
          this.$alert('该邮箱未注册！')
        } else {
          console.log(e)
        }
      }
      this.loading = false
    },
    async verifyCode () {
      try {
        await this.$refs.codeForm.validate()
      } catch (e) {
        return
      }
      this.loading = true
      try {
        this.token = await apiResetPassVerifyCode(
          this.$refs.emailForm.email,
          this.$refs.codeForm.code
        )
        this.active++
      } catch (e) {
        if (e.response.status === 400) {
          this.$alert('验证失败，请重新输入验证码！', '', {confirmButtonText: '确定'})
          this.$refs.codeForm.clear()
        } else {
          console.log(e)
        }
      }
      this.loading = false
    },
    async resetPassword () {
      try {
        await this.$refs.passwordForm.validate()
      } catch (e) {
        console.log(e)
        return
      }
      console.log('1')
      this.loading = true
      try {
        await apiResetPassword(this.token, this.$refs.passwordForm.password)
        this.active++
      } catch (e) {
        if (e.response.status === 400) {
          this.$alert('密码重置失败：' + e.response.data, '', {confirmButtonText: '确定'})
          this.$refs.passwordForm.clear()
        } else {
          console.log(e)
        }
      }
      this.loading = false
    },
    async getEmailValidateCode () {
      this.$refs.codeForm.wait()
      await apiResetPassVerifyEmail(this.$refs.emailForm.email)
    },
    logOn () {
      this.$router.push('login')
    }
  }
}
</script>

<style scoped>
h4 {
  color: #0babeab8;
  font-size: 17px;
}

.next-button {
  width: 30%;
}

.input-container {
  margin: 30px auto;
  max-width: 500px;
}
</style>
