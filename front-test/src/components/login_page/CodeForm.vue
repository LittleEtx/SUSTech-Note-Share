<template>
<el-form ref="form" :model="codeForm" :rules="codeRules"
         label-position="right" label-width="80px">
  <!-- 邮箱验证码 -->
  <el-form-item label="验证码" prop="emailCode" style="font-weight: bold">
    <el-input v-model="codeForm.emailCode" placeholder="验证码">
      <template #prefix>
        <el-icon><ChatLineSquare /></el-icon>
      </template>
      <template #append>
        <el-button :disabled="disabled" @click="$emit('send-code')">
            {{buttonText}}
        </el-button>
      </template>
    </el-input>
  </el-form-item>
</el-form>
</template>

<script>

import {ChatLineSquare} from "@element-plus/icons-vue"

export default {
  components: {ChatLineSquare},
  expose: ['validate', 'code', 'wait', 'clear'],
  computed: {
    code () { return this.codeForm.emailCode }
  },
  data () {
    return {
      disabled: false,
      buttonText: '获取验证码',
      codeForm: {
        emailCode: ''
      },
      codeRules: {
        emailCode: [
          {required: true, message: '请输入验证码', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    clear () {
      this.codeForm.emailCode = ''
    },
    validate () {
      return this.$refs.form.validate()
    },
    wait () { // 验证码倒数60秒
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
    }
  }
}
</script>
