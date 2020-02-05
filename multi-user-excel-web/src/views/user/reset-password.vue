<template>
  <div id="loginDiv">
    <el-card id="loginElCard">
      <div slot="header" class="clearfix">
        <el-button type round plain icon="el-icon-back" v-goBack></el-button>
        <img src="static/images/resetPassword.png" width="300px" />
        <my-tooltip content="重置密码后将会把新的密码发送到邮箱，请注意查收" />
      </div>
      <el-form :model="resetPasswordForm" ref="resetPasswordForm" @submit.native.prevent>
        <el-row>
          <el-col :span="24">
            <el-form-item label="登录邮箱" prop="email">
              <email-input v-model="resetPasswordForm.email"></email-input>
            </el-form-item>
          </el-col>
        </el-row>
        <br />
        <el-row>
          <el-col :span="8">
            <el-form-item prop="frontCode">
              <el-input v-model="resetPasswordForm.frontCode" placeholder="请输入邮箱验证码"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8" :offset="1">
            <get-email-code-button
              :email="resetPasswordForm.email"
              :disableFlag="!resetPasswordForm.email.length>0"
            ></get-email-code-button>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button type="primary" @click="onResetPassword" preventReClick>重置密码</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script>
import { resetPassword } from '@/api/user'
import { emailVaildCheck, userNameVaildCheck } from '@/api/utils'
import getEmailCodeButton from '@/components/utils/get-email-code-button'
import emailInput from '@/components/inputs/email-input'
import myTooltip from '@/components/common/my-tooltip'
export default {
  components: {
    getEmailCodeButton,
    emailInput,
    myTooltip
  },
  data() {
    return {
      // 发送后端用户充值密码的参数
      resetPasswordForm: {
        email: '',
        // 前端获取的code
        frontCode: ''
      }
    }
  },
  methods: {

    // 重置
    reset() {
      this.$refs['resetPasswordForm'].resetFields()
    },
    onResetPassword() {
      this.$refs.resetPasswordForm.validate(vaild => {
        if (vaild) {
          resetPassword(this.resetPasswordForm)
            .then(response => {
              window.console.log(response)
              if (response.data.code === 200) {
                window.alert('重置成功')
                this.$router.push({
                  path: '/user/login'
                })
              } else {
                window.alert(response.data.msg)
              }
            })
            .catch(error => {
              window.console.log('报错了')
              window.console.log(error)
              this.$router.push({
                path: '/error'
              })
            })
        } else {
          window.console.log('表单校验不通过')
          return false
        }
      })
    }
  }
}
</script>
<style scoped>
#loginElCard {
  width: 500px;
  margin: auto;
}
</style>
