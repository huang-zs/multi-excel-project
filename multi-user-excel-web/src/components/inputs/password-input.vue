<template>
  <el-input
    maxlength="8"
    :type="text_input?'text':'password'"
    v-model="input_value"
    :disabled="disableFlag"
    @blur="checkMethod"
    placeholder="请输入登录密码(组件)"
  >
    <el-button
      slot="append"
      :icon="text_input?'el-icon-remove-outline':'el-icon-view'"
      @click="text_input=!text_input"
    ></el-button>
  </el-input>
</template>
<script>
import { passwordVaildCheck } from '@/api/utils'
export default {
  name: 'password-input',
  props: {
    value: {
      type: String
    },
    disableFlag: {
      type: Boolean,
      default: false
    }

  },
  data() {
    return {
      text_input: true
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
      if (!passwordVaildCheck(this.input_value)) {
        this.input_value = ''
        this.$message({
          message: '密码格式不正确，只能包含英文、数字,请重新输入',
          type: 'error',
          duration: 2000
        })

      }
    },
  },
}
</script>