<template>
  <div id="app">
    <el-container>
      <el-header>
        <my-header></my-header>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
      <el-footer>
        <my-footer></my-footer>
      </el-footer>
    </el-container>
  </div>
</template>

<script>

import myHeader from './components/common/my-header'
import myFooter from './components/common/my-footer'
export default {
  name: 'app',
  components: {
    myHeader, myFooter
  },
  // beforeDestroy() {
  //   this.$store.commit('deleteUserInfo')
  // },
}
</script>
<style >
html,
body,
#app {
  height: 100%;
}
.mainFrame {
  width: 100%;
  height: 100%;
}
</style>