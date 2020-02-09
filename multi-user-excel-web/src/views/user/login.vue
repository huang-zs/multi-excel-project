<template>
  <div class="mainFrame">
    <el-card id="loginElCard">
      <div slot="header" class="clearfix">
        <el-button type round plain icon="el-icon-back" v-goBack></el-button>
        <img src="static/images/login.png" width="300px" />
      </div>
      <el-form :model="loginForm" ref="loginForm" @submit.native.prevent>
        <el-form-item label="登录邮箱">
          <!-- <el-input v-model="loginForm.email" placeholder="请输入登录邮箱" /> -->
          <email-input v-model="loginForm.email"></email-input>
        </el-form-item>
        <el-form-item label="登录密码">
          <!-- <el-input v-model="loginForm.password" placeholder="请输入登录密码" /> -->
          <password-input v-model="loginForm.password"></password-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onLogin" v-preventReClick="2000">登录</el-button>
          <el-button @click="toCreate" v-preventReClick="2000">注册</el-button>
          <router-link to="/user/resetPassword">
            <el-button type="text">忘记密码</el-button>
          </router-link>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script>

import emailInput from '@/components/inputs/email-input'
import passwordInput from '@/components/inputs/password-input'
import { login } from '@/api/user'
export default {
  components: {

    emailInput,
    passwordInput
  },
  data() {
    return {
      loginForm: {
        email: '',
        password: ''
      }
    }
  },
  methods: {
    toCreate() {
      window.console.log('去注册')
      this.$router.push({
        path: '/user/create'
      })
    },
    forgetPwd() {
      alert('不，你没忘')
    },
    onLogin() {
      this.$refs.loginForm.validate(vaild => {
        if (vaild) {
          // this.$store
          // .dispatch('user/Login',this.loginForm)
          login(this.loginForm)
            .then(response => {
              window.console.log('登录返回')
              window.console.log(response)
              if (response.data.code === 200) {
                // 存 token 进store里面
                // 应该封装到store的login moudle里面的action调？
                // this.$store.commit('saveToken', response.data.data.token)
                this.$store.commit('saveUserInfo', response.data.data)
                this.$router.push({
                  path: '/home'
                })
              } else {
                window.alert(response.data.msg)
              }
            })
            .catch(error => {
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
