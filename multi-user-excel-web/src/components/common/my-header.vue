<template>
  <div>
    <div style="float:left;">
      <img src="../../assets/title.png" height="60px" />
    </div>
    <div style="float:right;">
      <el-button type="text" icon="el-icon-share" @click="shareWeb">好东西我要分享</el-button>
      <el-button type="text" icon="el-icon-message" @click="talkToAuthor">有话要和作者说</el-button>
      <el-button type="text" icon="el-icon-setting" @click="myInfo"></el-button>
    </div>
  </div>
</template>
<script>
export default {
  name: 'my-header',
  methods: {
    shareWeb () {
      window.alert('还没开通')
    },
    myInfo () {
      let user = this.$store.state.ExcelUser
      console.log(user)
      if (user) {
        window.alert(JSON.stringify(user))
      } else {
        window.alert('你还没有登录')
      }
    },
    talkToAuthor () {
      window.alert('还没开通')
    }

  }
}
</script>
