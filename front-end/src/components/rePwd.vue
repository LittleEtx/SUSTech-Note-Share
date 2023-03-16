<template>
  <div>
    <h4 style="text-align: center">找回密码</h4>
    <el-steps :active="active" :space="200" finish-status="success"  align-center>
      <el-step title="身份验证" icon="el-icon-edit"></el-step>
      <el-step title="密码重置" icon="el-icon-s-promotion"></el-step>
      <el-step title="重置完成" icon="el-icon-key"></el-step>
    </el-steps>
    <div v-if="active === 0" class="common_div">
      <el-form :model="emailLogForm" :rules="emailRules" ref="emailLogForm" label-width="100px" style="margin-top: 20px">
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
                <el-option label="SUSTech.edu.cn" value="sustech.edu.cn"></el-option>
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
        <el-form-item >
          <el-button type="primary" style="margin-left: -100px" @click="next('emailLogForm')">下一步</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div v-if="active === 1" class="common_div">
      <el-form :model="changePwd" :rules="pwdRules" ref="changePwd" label-width="100px" style="margin-top: 20px">
        <el-form-item label="新密码" style="font-weight: bold" prop="pwd">
        <el-input type="password" show-password placeholder="请输入新密码" prefix-icon="el-icon-key" v-model="changePwd.pwd">
        </el-input>
        </el-form-item>
        <el-form-item label="确认密码" style="font-weight: bold" prop="pwd1">
          <el-input type="password" show-password placeholder="请输入新密码" prefix-icon="el-icon-key" v-model="changePwd.pwd1">
          </el-input>
        </el-form-item>
        <el-form-item >
          <el-button type="primary" style="margin-left: -100px" @click="next1('changePwd')">下一步</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div v-if="active === 2" class="common_div">
      <p style="margin-top: 50px">(*^_^*)新登录密码重置成功，请重新登录！</p>
      <el-button type="primary" @click="logOn">重新登录</el-button>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'rePwd',
  data () {
    return {
      active: 0,
      disabled: false,
      buttonText: '获取验证码',
      token: '',
      emailLogForm: {
        ID: '',
        region: '',
        emailCode: ''
      },
      changePwd: {
        pwd: '',
        pwd1: ''
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
      },
      pwdRules: {
        pwd: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { pattern: /^[A-Za-z0-9]{6,18}$/, message: '密码为长度为6-18位的字母、数字', trigger: 'blur' }
        ],
        pwd1: [
          { required: true, message: '请再次输入新密码', trigger: 'blur' },
          { pattern: /^[A-Za-z0-9]{6,18}$/, message: '密码为长度为6-18位的字母、数字', trigger: 'blur' }
        ]
      }
    }
  },

  methods: {
    next (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const url = '/api/auth/reset-password/verify-email'
          axios.post(url, {
            email: this.emailLogForm.ID + '@' + this.emailLogForm.region,
            verificationCode: this.emailLogForm.emailCode
          }).then((res) => {
            if (res.data === 'fail') {
              this.$alert('验证失败，请重新输入验证码！', '', {
                confirmButtonText: '确定'
              })
              this.emailLogForm.emailCode = ''
            } else {
              this.active++
              this.token = res.data
            }
          })
        }
      })
    },
    next1 (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.changePwd.pwd === this.changePwd.pwd1) {
            const url = '/api/auth/reset-password/reset-password'
            console.log(this.token)
            axios.post(url, {
              token: this.token,
              password: this.changePwd.pwd
            }).then((res) => {
              if (res.data === '密码修改成功') {
                this.active++
              } else {
                this.$message.error(res.data)
              }
            })
          } else {
            this.$alert('密码输入不一致，请重新输入密码！', '', {
              confirmButtonText: '确定'
            })
            this.changePwd.pwd = ''
            this.changePwd.pwd1 = ''
          }
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
    getEmailValidateCode () {
      const url = '/api/auth/reset-password/confirm-email'
      axios.post(url, {
        email: this.emailLogForm.ID + '@' + this.emailLogForm.region
      }).then((res) => {
        console.log(res.data)
        if (res.data === '邮箱未注册') {
          this.$message.error('该邮箱未注册')
        } else {
          this.tackBtn()
        }
      })
    },
    logOn () {
      this.$router.go(0)
    }
  }
}
</script>

<style scoped>
h4 {
  color: #0babeab8;
  font-size: 17px;
}
</style>
