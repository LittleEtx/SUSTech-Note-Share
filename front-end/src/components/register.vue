<!--<template>-->
<!--  <div class="login_container">-->
<!--    <div class="login_logo">-->
<!--      <img />-->
<!--    </div>-->
<!--    <div class="login_box">-->
<!--      &lt;!&ndash; 登录表单区域 &ndash;&gt;-->
<!--      <el-tabs :stretch="true">-->
<!--        <el-tab-pane label="账号密码登录">-->
<!--          &lt;!&ndash; 账号密码登录表单 &ndash;&gt;-->
<!--          <el-form ref="pwdLoginFormRef" :model="pwdLoginForm" :rules="pwdLoginFormRules">-->
<!--            &lt;!&ndash; 用户名 &ndash;&gt;-->
<!--            <el-form-item prop="username">-->
<!--              <el-input placeholder="用户名/邮箱/手机号" clearable prefix-icon="el-icon-user-solid-->
<!--" v-model="pwdLoginForm.username">-->
<!--              </el-input>-->
<!--            </el-form-item>-->
<!--            &lt;!&ndash; 密码 &ndash;&gt;-->
<!--            <el-form-item prop="password">-->
<!--              <el-input placeholder="密码" type="password" show-password prefix-icon="el-icon-lock"-->
<!--                        v-model="pwdLoginForm.password">-->
<!--              </el-input>-->
<!--            </el-form-item>-->
<!--            &lt;!&ndash; 按钮区域 &ndash;&gt;-->
<!--            <el-form-item class="login_btns">-->
<!--              <el-button type="primary" @click="pwdLogin" :loading="loading">登录</el-button>-->
<!--              <el-button type="primary" @click='toRegister'>注册</el-button>-->
<!--            </el-form-item>-->
<!--          </el-form>-->
<!--        </el-tab-pane>-->
<!--        <el-tab-pane label="邮箱验证登录">-->
<!--          &lt;!&ndash; 邮箱验证登录表单 &ndash;&gt;-->
<!--          <el-form ref="emailLoginFormRef" :model="emailLoginForm" :rules="emailLoginFormRules">-->
<!--            &lt;!&ndash; 邮箱 &ndash;&gt;-->
<!--            <el-form-item prop="email">-->
<!--              <el-input placeholder="邮箱" clearable prefix-icon="el-icon-message" v-model="emailLoginForm.email">-->
<!--              </el-input>-->
<!--            </el-form-item>-->
<!--            &lt;!&ndash; 邮箱验证码 &ndash;&gt;-->
<!--            <el-form-item prop="emailCode">-->
<!--              <el-input placeholder="验证码" prefix-icon="el-icon-key" v-model="emailLoginForm.emailCode">-->
<!--                <template #append>-->
<!--                  <el-button :disabled="disabled" @click="getEmailValidateCode">{{buttonText}}-->
<!--                  </el-button>-->
<!--                </template>-->
<!--              </el-input>-->
<!--            </el-form-item>-->
<!--            &lt;!&ndash; 按钮区域 &ndash;&gt;-->
<!--            <el-form-item class="login_btns">-->
<!--              <el-button type="primary" @click="emailLogin">登录</el-button>-->
<!--              <el-button type="primary" @click='toRegister'>注册</el-button>-->
<!--            </el-form-item>-->
<!--          </el-form>-->
<!--        </el-tab-pane>-->
<!--        &lt;!&ndash; <el-tab-pane label="手机验证登录"> &ndash;&gt;-->
<!--        &lt;!&ndash; 手机验证登录表单 &ndash;&gt;-->
<!--        &lt;!&ndash; <el-form ref="phoneLoginFormRef" :model="phoneLoginForm" :rules="phoneLoginFormRules"> &ndash;&gt;-->
<!--        &lt;!&ndash; 手机号 &ndash;&gt;-->
<!--        &lt;!&ndash; <el-form-item prop="phone">-->
<!--              <el-input placeholder="手机号" clearable prefix-icon="el-icon-phone" v-model="phoneLoginForm.phone">-->
<!--              </el-input>-->
<!--            </el-form-item> &ndash;&gt;-->
<!--        &lt;!&ndash; 手机验证码 &ndash;&gt;-->
<!--        &lt;!&ndash; <el-form-item prop="phoneCode">-->
<!--              <el-input placeholder="验证码" prefix-icon="el-icon-key" v-model="phoneLoginForm.phoneCode">-->
<!--                <template #append>-->
<!--                  <el-button>获取验证码</el-button>-->
<!--                </template>-->
<!--              </el-input>-->
<!--            </el-form-item> &ndash;&gt;-->
<!--        &lt;!&ndash; 按钮区域 &ndash;&gt;-->
<!--        &lt;!&ndash; <el-form-item class="login_btns">-->
<!--              <el-button type="primary" @click="phoneLogin">登录</el-button>-->
<!--              <el-button type="success" @click='toRegister'>注册</el-button>-->
<!--            </el-form-item>-->
<!--          </el-form>-->
<!--        </el-tab-pane> &ndash;&gt;-->
<!--      </el-tabs>-->
<!--    </div>-->
<!--    <div class="login_footer">-->
<!--      登录即表示您已阅读并同意-->
<!--      <a href="#">服务条款</a>-->
<!--    </div>-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--import {-->
<!--  ref,-->
<!--  reactive,-->
<!--  toRefs,-->
<!--  getCurrentInstance-->
<!--} from 'vue'-->
<!--import qs from 'qs'-->
<!--import axios from 'axios'-->
<!--import {-->
<!--  useRouter-->
<!--} from 'vue-router'-->
<!--export default {-->
<!--  setup () {-->
<!--    // 验证邮箱的规则-->
<!--    var checkEmail = (rule, value, cb) => {-->
<!--      // 验证邮箱的正则表达式-->
<!--      const regEmail = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/-->
<!--      if (regEmail.test(value)) {-->
<!--        // 合法的邮箱-->
<!--        return cb()-->
<!--      }-->
<!--      cb(new Error('请输入合法的邮箱'))-->
<!--    }-->
<!--    // // 验证手机号的规则-->
<!--    // var checkMobile = (rule, value, cb) => {-->
<!--    //   // 验证手机号的正则表达式-->
<!--    //   const regMobile = /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/-->
<!--    //   if (regMobile.test(value)) {-->
<!--    //     return cb()-->
<!--    //   }-->
<!--    //   cb(new Error('请输入合法的手机号'))-->
<!--    // }-->
<!--    const {-->
<!--      proxy-->
<!--    } = getCurrentInstance()-->
<!--    const router = useRouter()-->
<!--    const pwdLoginFormRef = ref(null)-->
<!--    const emailLoginFormRef = ref(null)-->
<!--    // const phoneLoginFormRef = ref(null)-->
<!--    const state = reactive({-->
<!--      pwdLoginForm: {-->
<!--        username: 'admin',-->
<!--        password: '123456'-->
<!--      },-->
<!--      emailLoginForm: {-->
<!--        email: '',-->
<!--        emailCode: ''-->
<!--      },-->
<!--      // phoneLoginForm: {-->
<!--      //   phone: '',-->
<!--      //   phoneCode: ''-->
<!--      // },-->
<!--      pwdLoginFormRules: {-->
<!--        username: [{-->
<!--          required: true,-->
<!--          message: '请输入你的账号',-->
<!--          trigger: 'blur'-->
<!--        }],-->
<!--        // 验证密码是否合法-->
<!--        password: [{-->
<!--          required: true,-->
<!--          message: '请输入你的密码',-->
<!--          trigger: 'blur'-->
<!--        }]-->
<!--      },-->
<!--      emailLoginFormRules: {-->
<!--        email: [{-->
<!--          required: true,-->
<!--          message: '请输入你的邮箱',-->
<!--          trigger: 'blur'-->
<!--        },-->
<!--        {-->
<!--          validator: checkEmail,-->
<!--          trigger: 'blur'-->
<!--        }-->
<!--        ],-->
<!--        emailCode: [{-->
<!--          required: true,-->
<!--          message: '请输入你获取到的验证码',-->
<!--          trigger: 'blur'-->
<!--        }]-->
<!--      },-->
<!--      // phoneLoginFormRules: {-->
<!--      //   phone: [{-->
<!--      //       required: true,-->
<!--      //       message: '请输入你的手机号',-->
<!--      //       trigger: 'blur'-->
<!--      //     },-->
<!--      //     {-->
<!--      //       validator: checkMobile,-->
<!--      //       trigger: 'blur'-->
<!--      //     }-->
<!--      //   ],-->
<!--      //   phoneCode: [{-->
<!--      //     required: true,-->
<!--      //     message: '请输入你获取到的验证码',-->
<!--      //     trigger: 'blur'-->
<!--      //   }]-->
<!--      // },-->
<!--      loading: false,-->
<!--      // 控制获取验证码-->
<!--      buttonText: '获取验证码',-->
<!--      disabled: false,-->
<!--      duration: 60,-->
<!--      timer: null-->
<!--    })-->
<!--    // 账号密码登录-->
<!--    const pwdLogin = async () => {-->
<!--      state.loading = true-->
<!--      const obj = {-->
<!--        username: state.pwdLoginForm.username,-->
<!--        password: state.pwdLoginForm.password-->
<!--      }-->
<!--      try {-->
<!--        const res = await new proxy.$request(proxy.$urls.m().pwdLogin, qs.stringify(obj)).modepost()-->
<!--        console.log(res)-->
<!--        if (res.data.success !== true) {-->
<!--          new proxy.$tips(res.data.message, 'warning').mess_age()-->
<!--        } else {-->
<!--          new proxy.$tips(res.data.message, 'success').mess_age()-->
<!--          localStorage.setItem('token', res.data.data.token)-->
<!--          // 成功跳转页面-->
<!--        }-->
<!--        state.loading = false-->
<!--      } catch (e) {-->
<!--        state.loading = false-->
<!--        new proxy.$tips('服务器发生错误', 'error').mess_age()-->
<!--      }-->
<!--    }-->
<!--    // 获取邮箱验证码-->
<!--    const getEmailValidateCode = () => {-->
<!--      const obj = {-->
<!--        email: state.emailLoginForm.email-->
<!--      }-->
<!--      axios.post('requestUrl', qs.stringify(obj)).then(-->
<!--        res => {-->
<!--          if (res.data.success !== true) {-->
<!--            new proxy.$tips(res.data.message, 'warning').mess_age()-->
<!--          } else {-->
<!--            console.log(res.data)-->
<!--            console.log(res.headers)-->
<!--            new proxy.$tips(res.data.message, 'success').mess_age()-->
<!--            // localStorage.setItem('token', res.data.data.token)-->
<!--            state.timer = setInterval(() => {-->
<!--              state.disabled = true-->
<!--              const tmp = state.duration&#45;&#45;-->
<!--              state.buttonText = `${tmp}秒后重新获取`-->
<!--              if (tmp <= 0) {-->
<!--                clearInterval(state.timer)-->
<!--                state.duration = 60-->
<!--                state.buttonText = '重新获取验证码'-->
<!--                state.disabled = false-->
<!--              }-->
<!--            }, 1000)-->
<!--          }-->
<!--        })-->
<!--    }-->
<!--    // 邮箱验证登录-->
<!--    const emailLogin = async () => {-->
<!--      state.loading = true-->
<!--      const obj = {-->
<!--        email: state.emailLoginForm.email,-->
<!--        emailValidateCode: state.emailLoginForm.emailCode,-->
<!--        token: localStorage.getItem('token')-->
<!--      }-->
<!--      try {-->
<!--        const res = await new proxy.$request(proxy.$urls.m().emailLogin, qs.stringify(obj)).modepost()-->
<!--        if (res.data.success !== true) {-->
<!--          new proxy.$tips(res.data.message, 'warning').mess_age()-->
<!--        } else {-->
<!--          new proxy.$tips(res.data.message, 'success').mess_age()-->
<!--          localStorage.setItem('token', res.data.data.token)-->
<!--          // 成功跳转页面-->
<!--        }-->
<!--        state.loading = false-->
<!--      } catch (e) {-->
<!--        state.loading = false-->
<!--        new proxy.$tips('服务器发生错误', 'error').mess_age()-->
<!--      }-->
<!--    }-->
<!--    // 手机号验证登录-->
<!--    // const phoneLogin = async () => {-->
<!--    //   const obj = {-->
<!--    //     phone: state.phoneLoginForm.phone,-->
<!--    //     phoneCode: state.phoneLoginForm.phoneCode-->
<!--    //   }-->
<!--    // }-->
<!--    const toRegister = () => {-->
<!--      router.push({-->
<!--        path: '/register'-->
<!--      })-->
<!--    }-->
<!--    return {-->
<!--      ...toRefs(state),-->
<!--      pwdLoginFormRef,-->
<!--      emailLoginFormRef,-->
<!--      // phoneLoginFormRef,-->
<!--      pwdLogin,-->
<!--      getEmailValidateCode,-->
<!--      emailLogin,-->
<!--      // phoneLogin,-->
<!--      toRegister-->
<!--    }-->
<!--  }-->
<!--}-->
<!--</script>-->

<!--<style lang="less" scoped>-->
<!--.login_container {-->
<!--  width: 100%;-->
<!--  height: 100%;-->
<!--  //background: url(../assets/登录页面背景图.png);-->
<!--  position: fixed;-->
<!--  background-size: 100% 100%;-->
<!--}-->

<!--.login_box {-->
<!--  width: 395px;-->
<!--  height: 435px;-->
<!--  background-color: rgba(225, 225, 225, 0);-->
<!--  border: 1px solid rgba(225, 225, 225, 0);-->
<!--  box-shadow: 2px 3px 3px #aaaaaa;-->
<!--  border-radius: 5px;-->
<!--  position: relative;-->
<!--  left: 50%;-->
<!--  top: 50%;-->
<!--  transform: translate(-50%, -50%);-->
<!--}-->

<!--.login_btns {-->
<!--  text-align: center;-->
<!--}-->

<!--.login_footer {-->
<!--  position: absolute;-->
<!--  left: 50%;-->
<!--  top: 70%;-->
<!--  transform: translate(-50%, -50%);-->
<!--  border: 1px solid #ffffff;-->
<!--  width: 395px;-->
<!--  height: 30px;-->
<!--  text-align: center;-->
<!--}-->
<!--</style>-->

<!--<style scoped>-->
<!--.login_box {-->
<!--  backdrop-filter: blur(15px);-->
<!--  box-shadow: 0 0 5px #fff;-->
<!--}-->

<!--.el-tabs >>> .el-tabs__header {-->
<!--  padding: 5% 10% 0 10%;-->
<!--}-->

<!--.el-tabs >>> .el-tabs__item:hover {-->
<!--  color: #fff;-->
<!--  cursor: pointer;-->
<!--}-->

<!--.el-tabs >>> .el-tabs__item.is-active {-->
<!--  color: #c915e5;-->
<!--}-->

<!--.el-form {-->
<!--  text-align: center;-->
<!--}-->

<!--.el-form-item>>>.el-form-item__error {-->
<!--  left: 10%;-->
<!--}-->

<!--.el-input {-->
<!--  width: 80%;-->
<!--}-->

<!--.el-input {-->
<!--  &#45;&#45;el-input-font-color: black;-->
<!--}-->

<!--.el-input>>>.el-input__inner {-->
<!--  background-color: rgba(225, 225, 225, 0);-->
<!--  box-shadow: 0 0 2px #fff;-->
<!--  border: 1px solid #fff;-->
<!--}-->

<!--.el-button {-->
<!--  border-radius: 10px;-->
<!--}-->

<!--.el-button&#45;&#45;primary {-->
<!--  &#45;&#45;el-button-font-color: #409e40;-->
<!--  &#45;&#45;el-button-background-color: #ffffff;-->
<!--  &#45;&#45;el-button-border-color: #409eff;-->
<!--  &#45;&#45;el-button-hover-color: #66b1ff;-->
<!--  &#45;&#45;el-button-active-font-color: #e6e6e6;-->
<!--  &#45;&#45;el-button-active-background-color: #0d84ff;-->
<!--  &#45;&#45;el-button-active-border-color: #0d84ff;-->
<!--}-->
<!--</style>-->
