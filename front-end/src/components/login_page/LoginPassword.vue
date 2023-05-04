<!--  密码的表单元件  -->
<template>
  <el-form ref="form" :model="pwdLoginForm" :rules="pwdRules"
                   label-position="right" label-width="80px">
    <el-row>
      <el-col :span="18">
        <el-form-item label="密码" prop="pwd" style="font-weight: bold">
          <el-input v-model="pwdLoginForm.pwd" placeholder="密码" show-password type="password"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="6" style="height: 35px;
      display: flex; flex-direction: row-reverse; align-items: flex-end">
        <el-link style="color: #0babeab8" @click="$router.push('reset_password')">忘记密码?</el-link>
      </el-col>
    </el-row>
  </el-form>
</template>

<script>

export default {
  name: 'LoginPassword',
  computed: {
    password () { return this.pwdLoginForm.pwd }
  },
  methods: {
    validate () { return this.$refs.form.validate() },
    clear () { this.pwdLoginForm.pwd = '' }
  },
  data () {
    return {
      pwdLoginForm: {
        pwd: ''
      },
      pwdRules: {
        pwd: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, max: 20, message: '密码长度在6到20个字符', trigger: 'blur'}
        ]
      }
    }
  }
}
</script>
