<!-- 用于重置密码的表单  -->
<template>
  <el-form :model="passwordForm" :rules="pwdRules" ref="form" label-width="80px" style="font-weight: bold">
    <el-form-item label="新密码" prop="pwd">
      <el-input show-password placeholder="新密码" v-model="passwordForm.pwd">
      </el-input>
    </el-form-item>
    <el-form-item label="确认密码" prop="pwd2">
      <el-input show-password placeholder="重复新密码" v-model="passwordForm.pwd2">
      </el-input>
    </el-form-item>
  </el-form>
</template>
<script>
export default {
  name: 'RenewPasswordForm',
  computed: {
    password () { return this.passwordForm.pwd }
  },
  methods: {
    validate () {
      return this.$refs.form.validate()
    },
    clear () {
      this.passwordForm.pwd = ''
      this.passwordForm.pwd2 = ''
    }
  },
  data () {
    const passwordValidate = (rule, value, callback) => {
      if (this.passwordForm.pwd2 === '') {
        callback(new Error('请再次输入新密码'))
      } else if (this.passwordForm.pwd2 !== this.passwordForm.pwd) {
        callback(new Error('两次输入的密码不相符'))
      } else {
        callback()
      }
    }
    return {
      passwordForm: {
        pwd: '',
        pwd2: ''
      },
      pwdRules: {
        pwd: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度在6到20个字符', trigger: 'blur' }
        ],
        pwd2: [
          { required: true, message: '请再次输入新密码', trigger: 'blur' },
          { validator: passwordValidate, trigger: ['blur', 'change'] }
        ]
      }
    }
  }
}
</script>
