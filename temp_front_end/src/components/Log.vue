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
                <el-option label="mail.SUSTech.edu.cn" value="mail.SUSTech.edu.cn"></el-option>
                <el-option label="SUSTech.edu.cn" value="SUSTech.edu.cn"></el-option>
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

export default {
  name: 'Log',
  props: {
    isPush: Boolean
  },
  mounted () {
    this.getCookie()
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
          const url = 'http://10.12.110.180:8088/api/auth/login/password-login'
          if (this.checked || this.checked1) {
            this.rememberMe = 1
          } else {
            this.rememberMe = 0
          }
          axios.post(url, {email: this.pwdLogForm.ID + '@' + this.pwdLogForm.region,
            password: this.pwdLogForm.pwd,
            rememberMe: this.rememberMe}).then((res) => {
            if (res.data === 1) {
              this.$alert('登录成功！', '', {
                confirmButtonText: '确定'
              })
            } else {
              this.$alert('登录失败，请重新登录！', '', {
                confirmButtonText: '确定'
              })
              this.resetForm('pwdLogForm')
            }
          })
        }
      })
    },
    // submit by emailCode
    submitForm1 (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const url = 'http://10.12.110.180:8088/api/auth/login/email-code-login'
          if (this.checked || this.checked1) {
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
              this.$alert('登录成功！', '', {
                confirmButtonText: '确定'
              })
            } else {
              this.$alert('登录失败，请重新登录！', '', {
                confirmButtonText: '确定'
              })
              this.emailLogForm.emailCode = ''
            }
          })
        }
      })
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
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
      const url = 'http://10.12.110.180:8088/api/auth/sendEmailCode'
      axios.post(url, {
        email: this.emailLogForm.ID + '@' + this.emailLogForm.region
      }).then((res) => {
        console.log(res.data)
      })
    },
    temp () {
      this.activeName = 'second'
    },
    rememberUser () {
      const that = this
      // 判断复选框是否被勾选 勾选则调用配置cookie方法
      if (that.checked === true) {
        // 传入账号名，密码，和保存天数三个参数
        const account = that.pwdLogForm.ID + '@' + that.pwdLogForm.region
        that.setCookie(account, that.pwdLogForm.pwd, 7)
      } else {
        // 清空Cookie
        that.clearCookie()
      }
    },

    // 设置cookie
    // eslint-disable-next-line camelcase
    setCookie (c_name, c_pwd, exdays) {
      let exdate = new Date() // 获取时间
      console.log(c_pwd)
      exdate.setTime(exdate.getTime() + 24 * 60 * 60 * 1000 * exdays) // 保存的天数
      // 字符串拼接cookie
      window.document.cookie =
        // eslint-disable-next-line camelcase
        'account' + '=' + c_name + ';path=/;expires=' + exdate.toGMTString()
      window.document.cookie =
        // eslint-disable-next-line camelcase
        'password' + '=' + c_pwd + ';path=/;expires=' + exdate.toGMTString()
    },
    // 读取cookie
    getCookie: function () {
      // let that = this
      if (document.cookie.length > 0) {
        // let arr = document.cookie.split('; ') // 这里显示的格式需要切割一下自己可输出看下
        // for (let i = 0; i < arr.length; i++) {
        //   let arr2 = arr[i].split('=') // 再次切割
        //   // 判断查找相对应的值
        //   if (arr2[0] === 'account') {
        //     let account = arr2[1].split('@') // 保存到保存数据的地方
        //     that.pwdLogForm.ID = account[0]
        //     that.pwdLogForm.region = account[1]
        //   } else if (arr2[0] === 'password') {
        //     that.pwdLogForm.pwd = arr2[1]
        //   }
        // }
        this.choseItem()
      }
    },
    // 清除cookie
    clearCookie: function () {
      this.setCookie('', '', -1) // 修改两个值都为空，天数为-1天就好了
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
  margin: 0px;
}
</style>
