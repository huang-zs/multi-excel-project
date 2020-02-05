<template>
  <el-input
    maxlength="20"
    type="text"
    v-model="input_value"
    :disabled="disableFlag"
    @blur="checkMethod"
    placeholder="请输入登录用户名(组件)"
  />
</template>
<script>
import { userNameVaildCheck } from '@/api/utils'
export default {
  name: 'name-input',
  // props: ['value', 'disableFlag'],
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
      if (!userNameVaildCheck(this.input_value)) {
        this.input_value = ''
        this.$message({
          message: '名字格式不正确，只能包含英文、数字和中文,请重新输入',
          type: 'error',
          duration: 2000
        })

      }
    },
  },
}
</script>