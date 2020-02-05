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

import '@grapecity/spread-sheets-vue'
import '@grapecity/spread-sheets/styles/gc.spread.sheets.excel2016colorful.css'
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