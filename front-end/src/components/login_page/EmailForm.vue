<!-- 邮箱的表单元件  -->
<template>
  <el-form :model="emailForm" :rules="emailRules" ref="form"
               label-position="right" label-width="80px">
    <el-row>
      <el-col :span="13">
        <el-form-item label="学校邮箱" prop="ID" style="font-weight: bold">
          <el-input v-model="emailForm.ID" placeholder="邮箱前缀" :disabled="disabled"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="2">
        <p style="margin-top: 10px">@</p>
      </el-col>
      <el-col :span="9">
        <!--   因为这里是form item，将label-width设为0以覆盖父节点的宽度     -->
        <el-form-item label-width="0" prop="region" style="width: 100%">
          <el-select v-model="emailForm.region" placeholder="请选择邮箱后缀" :disabled="disabled">
            <el-option v-for="(postfix, index) in validPostfixes" :label="postfix" :value="postfix" :key="index"></el-option>
         </el-select>
       </el-form-item>
     </el-col>
    </el-row>
  </el-form>
</template>

<script>
import {studentEmailPostfix, validEmailPostfixes} from '../../scripts/LoginRules'

export default {
  name: 'EmailForm',
  expose: ['validate', 'email'],
  computed: {
    validPostfixes () { return validEmailPostfixes },
    email () { return this.emailForm.ID + '@' + this.emailForm.region }
  },
  props: {
    disabled: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    validate () { return this.$refs.form.validate() },
    clear () {
      this.emailForm.ID = ''
      this.emailForm.region = ''
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
  }
}
</script>
