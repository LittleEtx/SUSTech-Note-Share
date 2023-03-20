<template>
  <div>
  <h4 style="text-align: center">登录</h4>
  <hr>
  <el-tabs v-model="activeName" @tab-click="handleClick" :stretch="true" style="margin-top: -20px" type="card">
    <el-tab-pane label="密码登录" name="first">
      <el-form :model="pwdLogForm" :rules="pwdRules" ref="pwdLogForm" label-width="100px" class="demo-ruleForm">
        <el-row >
          <el-col :span="12">
            <el-form-item label="学校邮箱" style="font-weight: bold" label-width="100px" label-position="right" prop="ID">
              <el-input v-model="pwdLogForm.ID" placeholder="学号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="1">
            <p style="margin-top: 10px; margin-left: 10px">@</p>
          </el-col>
          <el-col :span="10">
            <el-form-item prop="region" style="margin-left: -80px">
              <el-select v-model="pwdLogForm.region" placeholder="请选择邮箱后缀">
                <el-option label="mail.SUSTech.edu.cn" value="mail.sustech.edu.cn"></el-option>
                <el-option label="SUSTech.edu.cn" value="sustech.edu.cn"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="18">
            <el-form-item label="密码" style="font-weight: bold" prop="pwd" class="item">
              <el-input type="password" show-password v-model="pwdLogForm.pwd" placeholder="密码"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-link @click="choseItem" style="color: cornflowerblue; margin-left: 60px; margin-top: 20px">忘记密码?</el-link>
          </el-col>
        </el-row>
        <el-checkbox v-model="checked" style="margin-bottom: 15px">记住我</el-checkbox>
        <el-form-item >
          <el-button type="primary" style="margin-left: -100px" @click="submitForm('pwdLogForm')">登录</el-button>
          <el-button type="primary" @click="temp">注册</el-button>
        </el-form-item>
      </el-form>
    </el-tab-pane>
    <el-tab-pane label="邮箱登录" name="second">
      <el-form :model="emailLogForm" :rules="emailRules" ref="emailLogForm" label-width="100px">
        <!-- 邮箱 -->
        <el-row >
          <el-col :span="12">
            <el-form-item label="学校邮箱" style="font-weight: bold" label-width="100px" label-position="right" prop="ID">
              <el-input v-model="emailLogForm.ID" placeholder="学号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="1">
            <p style="margin-top: 10px; margin-left: 10px">@</p>
          </el-col>
          <el-col :span="10">
            <el-form-item prop="region" style="margin-left: -80px">
              <el-select v-model="emailLogForm.region" placeholder="请选择邮箱后缀">
                <el-option label="mail.SUSTech.edu.cn" value="mail.sustech.edu.cn"></el-option>
                <el-option label="SUSTech.edu.cn" value="SUSTech.edu.cn"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 邮箱验证码 -->
        <el-form-item label="验证码" style="font-weight: bold" prop="emailCode">
          <el-input placeholder="验证码" prefix-icon="el-icon-key" v-model="emailLogForm.emailCode">
            <template #append>
              <el-button :disabled="disabled" @click="getEmailValidateCode">{{buttonText}}
              </el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-checkbox v-model="checked1" style="margin-bottom: 15px">记住我</el-checkbox>
        <el-form-item >
          <el-button type="primary" style="margin-left: -100px" @click="submitForm1('emailLogForm')">登录/注册</el-button>
        </el-form-item>
      </el-form>
    </el-tab-pane>
  </el-tabs>
  </div>
</template>

<script>
import axios from 'axios'
import router from '../router'

export default {
  name: 'Log',
  props: {
    isPush: Boolean
  },
  data () {
    return {
      activeName: 'first',
      checked: false,
      checked1: false,
      disabled: false,
      token: '',
      buttonText: '获取验证码',
      rememberMe: 0,
      pwdLogForm: {
        ID: '',
        region: '',
        pwd: ''
      },
      emailLogForm: {
        ID: '',
        region: '',
        emailCode: ''
      },
      pwdRules: {
        ID: [
          { required: true, message: '请输入学号', trigger: 'blur' },
          { pattern: /^\d{8}$/, message: '学号格式错误', trigger: 'blur' }
        ],
        region: [
          { required: true, message: '请选择邮箱后缀', trigger: 'change' }
        ],
        pwd: [
          {required: true, message: '请输入密码', trigger: 'blur'}
        ]
      },
      emailRules: {
        ID: [
          { required: true, message: '请输入学号', trigger: 'blur' },
          { pattern: /^\d{8}$/, message: '学号格式错误', trigger: 'blur' }
        ],
        region: [
          { required: true, message: '请选择邮箱后缀', trigger: 'change' }
        ],
        emailCode: [
          {required: true, message: '请输入验证码', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    handleClick (tab, event) {
      console.log(tab, event)
    },
    // submit by pwd
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const url = '/api/auth/login/password-login'
          if (this.checked) {
            this.rememberMe = 1
          } else {
            this.rememberMe = 0
          }
          axios.post(url, {email: this.pwdLogForm.ID + '@' + this.pwdLogForm.region,
            password: this.pwdLogForm.pwd,
            rememberMe: this.rememberMe
          }).then((res) => {
            if (res.data === 1) {
              router.push({path: '/home'})
            } else {
              alert('登录失败，请重新登录！')
              this.pwdLogForm.pwd = ''
            }
          }).catch((err) => {
            alert(err)
          })
        }
      })
    },
    // submit by emailCode
    submitForm1 (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const url = '/api/auth/login/email-code-login'
          if (this.checked1) {
            this.rememberMe = 1
          } else {
            this.rememberMe = 0
          }
          axios.post(url, {
            email: this.emailLogForm.ID + '@' + this.emailLogForm.region,
            verificationCode: this.emailLogForm.emailCode,
            rememberMe: this.rememberMe
          }).then((res) => {
            if (res.data === 1) {
              router.push({path: '/home'})
            } else {
              alert('登录失败，请重新登录')
              this.emailLogForm.emailCode = ''
            }
          }).catch((err) => {
            alert(err)
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
      this.tackBtn()
      const url = '/api/auth/sendEmailCode'
      axios.post(url, {
        email: this.emailLogForm.ID + '@' + this.emailLogForm.region
      }).then((res) => {
        console.log(res.data)
      })
    },
    temp () {
      this.activeName = 'second'
    }
  }
}
</script>

<style scoped>
.el-tabs >>> .el-tabs__header {
  padding: 5% 10% 0 10%;
}

.el-tabs >>> .el-tabs__item:hover {
  color: cadetblue;
  cursor: pointer;
}

.el-tabs >>> .el-tabs__item.is-active {
  color: red;
}
h4 {
  color: #0babeab8;
  font-size: 17px;
}

hr {
  background-color: #444;
  margin: 0;
}
</style>
