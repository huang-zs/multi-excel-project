<template>
  <div class="mainFrame">
    <el-card id="loginElCard">
      <div slot="header" class="clearfix">
        <el-button type round plain icon="el-icon-back" v-goBack></el-button>

        <img src="static/images/create.png" width="300px" />
      </div>
      <el-form :model="createForm" ref="createForm" @submit.native.prevent>
        <el-form-item label="登录用户名" prop="name">
          <!-- <el-input
            v-model="createForm.name"
            placeholder="请输入登录用户名"
            @blur="userNameVaildCheckMethod"
          />-->
          <my-tooltip content="用户名格式：20位的英文、数字和中文组合" />
          <name-input v-model="createForm.name"></name-input>
        </el-form-item>
        <el-row>
          <el-col :span="24">
            <el-form-item label="登录邮箱" prop="email">
              <!-- <el-input
                v-model="createForm.email"
                placeholder="请输入登录邮箱"
                @blur="emailVaildCheckMethod"
              />-->
              <my-tooltip content="邮箱格式：xxx@xxx.com" />

              <email-input v-model="createForm.email"></email-input>
            </el-form-item>
          </el-col>
        </el-row>
        <br />
        <el-row>
          <el-col :span="8">
            <el-form-item prop="frontCode">
              <el-input v-model="createForm.frontCode" placeholder="请输入邮箱验证码"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8" :offset="1">
            <!-- <el-button icon="el-icon-message" @click="getCode" preventReClick>获取验证码</el-button> -->
            <get-email-code-button
              :email="createForm.email"
              :disableFlag="!createForm.email.length>0"
            ></get-email-code-button>
          </el-col>
        </el-row>
        <el-form-item label="登录密码" prop="password">
          <my-tooltip content="密码格式：8位英文数字组合" />

          <!-- <el-input v-model="createForm.password" placeholder="请输入登录密码" /> -->
          <password-input v-model="createForm.password"></password-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onCreate" v-preventReClick>注册</el-button>
          <el-button @click="reset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script>
import { create } from '@/api/user'
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
  data() {
    return {
      // 发送后端用户注册的参数
      createForm: {
        name: '',
        email: '',
        password: '',
        // 前端获取的code
        frontCode: ''
      }
    }
  },
  methods: {
    goBack() {
      this.$router.back()
    },
    // 重置
    reset() {
      this.$refs['createForm'].resetFields()
    },
    onCreate() {
      this.$refs.createForm.validate(vaild => {
        if (vaild) {
          create(this.createForm, this.createForm.frontCode)
            .then(response => {
              window.console.log(response)
              if (response.data.code === 200) {
                window.alert('注册成功')
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
