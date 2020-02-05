<template>
  <el-input
    type="text"
    v-model="input_value"
    @blur="checkMethod"
    :disabled="disableFlag"
    placeholder="请输入登录邮箱(组件)"
  />
</template>
<script>
import { emailVaildCheck } from '@/api/utils'
export default {
  name: 'email-input',
  props: {
    value: {
      type: String
    },
    disableFlag: {
      type: Boolean,
      default: false
    }

  },
  computed: {
    input_value: {
      get() {
        return this.value
      },
      set(val) {
        this.$emit('input', val)
      }
    }
  },
  methods: {
    //校验邮箱格式
    checkMethod() {
      if (!emailVaildCheck(this.input_value)) {
        this.input_value = ''
        this.$message({
          message: '邮箱格式不正确，请重新输入',
          type: 'error',
          duration: 2000
        })

      }
    },
  },
}
</script>