<template>
  <div id="loginDiv">
    <el-card id="loginElCard">
      <div slot="header" class="clearfix">
        <el-button type round plain icon="el-icon-back" v-goBack></el-button>
        <img src="static/images/updateInfo.png" width="300px" />
      </div>
      <el-form :model="updateForm" ref="updateForm" @submit.native.prevent>
        <el-form-item label="登录用户名" prop="name">
          <my-tooltip content="用户名格式：20位的英文、数字和中文组合" />
          <el-tooltip></el-tooltip>
          <el-tooltip class="item" effect="dark" content="需要更改用户名就打开" placement="top">
            <el-switch
              v-model="nameInputDisabledFlag"
              style="float:right"
              :disabled="nameInputDisabledFlag"
            ></el-switch>
          </el-tooltip>
          <name-input v-model="updateForm.name" :disableFlag="!nameInputDisabledFlag"></name-input>
        </el-form-item>
        <el-row>
          <el-col :span="24">
            <el-form-item label="登录邮箱" prop="email">
              <my-tooltip content="邮箱格式：xxx@xxx.com" />
              <el-tooltip class="item" effect="dark" content="需要重新绑定邮箱就打开" placement="top">
                <el-switch
                  v-model="emailInputDisabledFlag"
                  style="float:right"
                  :disabled="emailInputDisabledFlag"
                ></el-switch>
              </el-tooltip>
              <email-input v-model="updateForm.email" :disableFlag="!emailInputDisabledFlag"></email-input>
            </el-form-item>
          </el-col>
        </el-row>
        <br />
        <el-row>
          <el-col :span="8">
            <el-form-item prop="frontCode">
              <el-input
                v-model="updateForm.frontCode"
                placeholder="请输入邮箱验证码"
                :disabled="!emailInputDisabledFlag"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8" :offset="1">
            <get-email-code-button
              :email="updateForm.email"
              :disableFlag="(!updateForm.email.length>0|!emailInputDisabledFlag)?true:false"
            ></get-email-code-button>
          </el-col>
        </el-row>
        <el-form-item label="登录密码" prop="password">
          <my-tooltip content="密码格式：8位英文数字组合" />
          <el-tooltip class="item" effect="dark" content="需要更改密码就打开" placement="top">
            <el-switch
              v-model="passwordInputDisabledFlag"
              style="float:right"
              :disabled="passwordInputDisabledFlag"
            ></el-switch>
          </el-tooltip>
          <password-input v-model="updateForm.password" :disableFlag="!passwordInputDisabledFlag"></password-input>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            @click="onUpdate"
            preventReClick
            :disabled="(nameInputDisabledFlag|emailInputDisabledFlag|passwordInputDisabledFlag)?false:true"
          >修改信息</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script>
import { update } from '@/api/user'
import { emailVaildCheck, userNameVaildCheck } from '@/api/utils'
import getEmailCodeButton from '@/components/utils/get-email-code-button'
import emailInput from '@/components/inputs/email-input'
import nameInput from '@/components/inputs/name-input'
import passwordInput from '@/components/inputs/password-input'
import myTooltip from '@/components/common/my-tooltip'
export default {
  components: {
    getEmailCodeButton,
    emailInput,
    nameInput,
    passwordInput,
    myTooltip
  },
  mounted() {
    this.updateForm = JSON.parse(this.$store.state.ExcelUser)
    console.log(this.updateForm)
  },
  data() {
    return {
      nameInputDisabledFlag: false,
      emailInputDisabledFlag: false,
      passwordInputDisabledFlag: false,
      // 发送后端用户注册的参数
      updateForm: {
        id: '',
        name: '',
        email: '',
        password: '',
        // 前端获取的code
        frontCode: ''
      }
    }
  },
  methods: {
    onUpdate() {
      //哪里有修改就放哪个
      let form = { id: this.updateForm.id }
      if (this.nameInputDisabledFlag) this.$set(form, 'name', this.updateForm.name)
      if (this.emailInputDisabledFlag) {
        this.$set(form, 'email', this.updateForm.email)
        this.$set(form, 'frontCode', this.updateForm.frontCode)
      }
      if (this.passwordInputDisabledFlag) this.$set(form, 'password', this.updateForm.password)
      console.log(form)

      update(form)
        .then(response => {
          window.console.log(response)
          if (response.data.code === 200) {
            window.alert('修改成功,请重新登陆')
            this.$store.commit('deleteUserInfo')
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
