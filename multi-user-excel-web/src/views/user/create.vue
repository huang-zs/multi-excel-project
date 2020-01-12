<template>
  <div id="loginDiv">
    <el-card id="loginElCard">
      <div slot="header" class="clearfix">
        <router-link to="/user/login">
        <el-button type round plain icon="el-icon-back"></el-button>
        </router-link>
        <img src="../../assets/create.png" width="300px" />
      </div>
      <el-form :model="createForm" ref="createForm" @submit.native.prevent>
        <el-form-item label="登录用户名" prop="username">
          <el-input v-model="createForm.username" placeholder="请输入登录用户名" />
        </el-form-item>
        <el-row>
          <el-col :span="24">
            <el-form-item label="登录邮箱" prop="email">
              <el-input v-model="createForm.email" placeholder="请输入登录邮箱" />
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
            <el-button icon="el-icon-message" @click="getCode">获取验证码</el-button>
          </el-col>
        </el-row>
        <el-form-item label="登录密码" prop="password">
          <el-input v-model="createForm.password" placeholder="请输入登录密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onCreate">注册</el-button>
          <el-button @click="reset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script>
import { getCode, create } from '../../api/user'
export default {
  data () {
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
    // 重置
    reset () {
      this.$refs['createForm'].resetFields()
    },

    // 请求后端获取邮箱验证码
    getCode () {
      window.console.log(this.createForm)
      getCode(this.createForm).then(response => {
        window.console.log(response)
        if (response.data.code === 200) {
          this.backCode = response.data.data
        } else {
          alert(response.data.msg)
        }
      })
    },
    onCreate () {
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
