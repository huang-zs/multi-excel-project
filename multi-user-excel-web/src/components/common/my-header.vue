<template>
  <div>
    <div style="float:left;">
      <img src="static/images/title.png" height="60px" />
    </div>
    <!--用户登录才显示-->
    <div style="float:right;" v-if="userLoginFlag">
      <el-button
        type="text"
        icon="el-icon-share"
        v-clipboard:copy="url"
        v-clipboard:success="onCopy"
        v-clipboard:error="onError"
      >好东西我要分享</el-button>
      <el-button
        type="text"
        icon="el-icon-message"
        @click="talkToAuthorDialogVisibleFlag=true"
      >有话要和作者说</el-button>
      <talk-to-author-dialog
        :dialogVisible="talkToAuthorDialogVisibleFlag"
        @dialogVisibleClose="talkToAuthorDialogVisibleFlag=$event"
      ></talk-to-author-dialog>
      <el-button v-popover:popover icon="el-icon-menu" type="text">我的信息</el-button>
      <el-popover
        ref="popover"
        placement="bottom"
        width="200"
        trigger="click"
        :value="popoverVisibleFlag"
      >
        <!--用户登录显示用户信息-->
        <div v-if="userLoginFlag">
          <span>登录名:</span>
          <el-row>
            <el-input disabled :value="user.name" />
          </el-row>
          <span>登录邮箱:</span>
          <el-row>
            <el-input disabled :value="user.email" />
          </el-row>
          <el-row>
            <router-link to="/home/updateInfo">
              <el-button type="mini">修改个人信息</el-button>
            </router-link>
            <el-button type="mini" @click="userLogout()">退出账号</el-button>
          </el-row>
        </div>
        <!-- 用户未登录显示去登录 -->
        <div v-else>
          <router-link to="/user/login">
            <el-button>去登录</el-button>
          </router-link>
        </div>
      </el-popover>
    </div>
    <div style="float:right;" v-else>
      <router-link to="/user/login">
        <el-button type="mini">登录</el-button>
      </router-link>
      <router-link to="/user/create">
        <el-button type="mini">注册</el-button>
      </router-link>
    </div>
  </div>
</template>
<script>
import talkToAuthorDialog from '@/components/dialogs/talk-to-author-dialog'
import { logout } from '@/api/user'
export default {
  name: 'my-header',
  components: {
    talkToAuthorDialog
  },
  data() {
    return {
      talkToAuthorDialogVisibleFlag: false,
      popoverVisibleFlag: false,
      url: 'http://' + process.env.SERVER_IP_PORT
    }
  },
  computed: {
    user() {
      return JSON.parse(this.$store.state.ExcelUser)
    },
    userLoginFlag() {
      return this.user ? true : false
    }
  },
  methods: {
    userLogout() {
      //调后端接口用户登出
      logout().then(response => {
        //删除保存在前端的用户信息
        this.$store.commit('deleteUserInfo')
        this.$router.push({ path: '/' })
        this.popoverVisibleFlag = false
      })
    },
    onCopy(e) {
      this.$message({
        message: '网页地址已复制到粘贴板，分享给你的小伙伴们吧',
        type: 'success',
        duration: 3000
      })
    },
    // 复制失败时的回调函数
    onError(e) {
      this.$message.error('抱歉，复制失败！')
    }

  }
}
</script>
