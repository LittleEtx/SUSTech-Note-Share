<!-- 邮箱的表单元件  -->
<template>
  <el-form :model="emailForm" :rules="emailRules" ref="form"
               label-position="right" label-width="80px">
    <el-form-item label="学校邮箱" prop="ID" style="font-weight: bold">
      <el-input v-model="emailForm.ID" placeholder="邮箱前缀" :disabled="disabled">
        <template #prefix>
          <el-icon><User /></el-icon>
        </template>
        <template #append>
          <el-form-item prop="region">
             <el-select v-model="emailForm.region" placeholder="请选择邮箱后缀" :disabled="disabled">
                <el-option v-for="(postfix, index) in validPostfixes"
                           :label="'@ ' + postfix" :value="postfix" :key="index"
                ></el-option>
             </el-select>
          </el-form-item>
        </template>
      </el-input>
    </el-form-item>
  </el-form>
</template>

<script>
import { studentEmailPostfix, validEmailPostfixes } from '@/scripts/LoginRules'
import { User } from '@element-plus/icons-vue'

export default {
  name: 'EmailForm',
  components: { User },
  expose: ['validate', 'email'],
  props: {
    disabled: {
      type: Boolean,
      default: false
    }
  },
  data () {
    const validateStudentEmail = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入邮箱前缀'))
      } else if (
        // 当选择了学校邮箱后缀时，验证学号是否为8位数字
        this.emailForm.region === studentEmailPostfix &&
        !/^[0-9]{8}$/.test(value)
      ) {
        callback(new Error('学号格式错误'))
      } else {
        callback()
      }
    }
    return {
      emailForm: {
        ID: '',
        region: ''
      },
      emailRules: {
        ID: [
          { required: true, message: '请输入邮箱前缀', trigger: 'blur' },
          { validator: validateStudentEmail, trigger: 'blur' }
        ],
        region: [
          { required: true, message: '请选择邮箱后缀', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    validPostfixes () { return validEmailPostfixes },
    email () { return this.emailForm.ID + '@' + this.emailForm.region }
  },
  methods: {
    validate () { return this.$refs.form.validate() },
    clear () {
      this.emailForm.ID = ''
      this.emailForm.region = ''
    }
  }
}
</script>
