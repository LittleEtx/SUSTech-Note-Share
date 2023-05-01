<template>
  <div>
    <h4 class="main-blue-color" style="text-align: center">登录</h4>
    <el-tabs v-model="activeName" :stretch="true" type="card" @tab-click="handleClick">
      <el-tab-pane class="pane-style" label="密码登录" name="first">
        <el-form ref="emailForm" :model="emailForm" :rules="emailRules"
                 label-position="right" label-width="80px">
          <el-row >
            <el-col :span="13">
              <el-form-item label="学校邮箱" prop="ID" style="font-weight: bold">
                <el-input v-model="emailForm.ID" placeholder="邮箱前缀"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="2">
              <p style="margin-top: 10px">@</p>
            </el-col>
            <el-col :span="9">
              <el-form-item label-width="0" prop="region" style="width: 100%">
                <el-select v-model="emailForm.region" placeholder="请选择邮箱后缀">
                 <el-option label="mail.SUSTech.edu.cn" value="mail.sustech.edu.cn"></el-option>
                 <el-option label="SUSTech.edu.cn" value="sustech.edu.cn"></el-option>
               </el-select>
             </el-form-item>
           </el-col>
          </el-row>
        </el-form>
        <el-form ref="pwdLoginForm" :model="pwdLoginForm" :rules="pwdRules"
                 label-position="right" label-width="80px">
          <el-row>
            <el-col :span="18">
              <el-form-item label="密码" prop="pwd" style="font-weight: bold">
                <el-input v-model="pwdLoginForm.pwd" placeholder="密码" show-password type="password"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6" style="height: 40px;
            display: flex; flex-direction: row-reverse; align-items: flex-end">
              <el-link style="color: #0babeab8" @click="choseItem">忘记密码?</el-link>
            </el-col>
          </el-row>
        </el-form>
        <el-checkbox v-model="rememberMe">记住我</el-checkbox>
        <p>
          <el-button class="button-size" type="primary" @click="loginViaPassword">登录</el-button>
          <el-button class="button-size" @click="register">注册</el-button>
        </p>
      </el-tab-pane>
      <el-tab-pane class="pane-style" label="验证码登录" name="second">
        <el-form ref="emailLogForm" :model="codeLoginForm" :rules="codeRules"
                 label-position="right" label-width="80px">
          <!-- 邮箱 -->
          <el-row >
            <el-col :span="13">
              <el-form-item label="学校邮箱" prop="ID" style="font-weight: bold">
                <el-input v-model="codeLoginForm.ID" placeholder="邮箱前缀"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="2">
              <p style="margin-top: 10px">@</p>
            </el-col>
            <el-col :span="9">
              <el-form-item label-width="0" prop="region">
                <el-select v-model="codeLoginForm.region" placeholder="请选择邮箱后缀">
                  <el-option label="mail.SUSTech.edu.cn" value="mail.sustech.edu.cn"></el-option>
                  <el-option label="SUSTech.edu.cn" value="SUSTech.edu.cn"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <!-- 邮箱验证码 -->
          <el-form-item label="验证码" prop="emailCode" style="font-weight: bold">
            <el-input v-model="codeLoginForm.emailCode" placeholder="验证码" prefix-icon="el-icon-key">
              <template #append>
                <el-button :disabled="disabled" @click="getEmailValidateCode">{{buttonText}}
                </el-button>
              </template>
            </el-input>
          </el-form-item>
          <el-checkbox v-model="rememberMe">记住我</el-checkbox>
          <p>
            <el-button class="button-size" type="primary" @click="loginViaCode('emailLogForm')">登录/注册</el-button>
          </p>
        </el-form>
     </el-tab-pane>
   </el-tabs>
  </div>
</template>

<script>
import router from '../../router'
import {apiLoginViaCode, apiLoginViaPassword, apiSendEmailCode} from '../../scripts/API_Auth'

export default {
  name: 'Login',
  props: {
    isPush: Boolean
  },
  data () {
    return {
      activeName: 'first',
      disabled: false,
      token: '',
      buttonText: '获取验证码',
      rememberMe: false,
      emailForm: {
        ID: '',
        region: ''
      },
      pwdLoginForm: {
        pwd: ''
      },
      codeLoginForm: {
        emailCode: ''
      },
      pwdRules: {
        pwd: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度在6到20个字符', trigger: 'blur' }
        ]
      },
      codeRules: {
        emailCode: [
          {required: true, message: '请输入验证码', trigger: 'blur'}
        ]
      },
      emailRules: {
        ID: [
          { required: true, message: '请输入邮箱前缀', trigger: 'blur' }
        ],
        region: [
          { required: true, message: '请选择邮箱后缀', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    handleClick (tab, event) {
      console.log(tab, event)
    },
    // submit by pwd
    async loginViaPassword () {
      try {
        await this.$refs['emailForm'].validate()
        await this.$refs['pwdLoginForm'].validate()
      } catch (e) {
        console.log(e)
        return
      }
      try {
        console.log(this.emailForm.ID + '@' + this.emailForm.region)
        await apiLoginViaPassword(
          this.emailForm.ID + '@' + this.emailForm.region,
          this.pwdLoginForm.pwd,
          this.rememberMe
        )
        router.push('home')
      } catch (e) {
        alert('登录失败，请重新登录！')
        this.pwdLoginForm.pwd = ''
      }
    },
    // submit by emailCode
    loginViaCode (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          apiLoginViaCode(
            this.codeLoginForm.ID + '@' + this.codeLoginForm.region,
            this.codeLoginForm.emailCode,
            this.rememberMe
          ).then(() => {
            router.push({path: '/home'})
          }).catch(() => {
            alert('登录失败，请重新登录')
            this.codeLoginForm.emailCode = ''
          })
        }
      })
    },
    tackBtn () { // 验证码倒数60秒
      let time = 60
      let timer = setInterval(() => {
        if (time === 0) {
          clearInterval(timer)
          this.buttonText = '获取验证码'
          this.disabled = false
        } else {
          this.disabled = true
          this.buttonText = time + '秒后重试'
          time--
        }
      }, 1000)
    },
    choseItem () {
      this.$emit('choseItem')
    },
    getEmailValidateCode () {
      apiSendEmailCode(
        this.codeLoginForm.ID + '@' + this.codeLoginForm.region
      ).then(() => {
        this.tackBtn()
      }).catch((err) => {
        alert('Fail to send email code !' + err.data)
      })
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
