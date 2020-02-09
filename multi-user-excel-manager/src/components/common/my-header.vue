<template>
  <div id="appHeader">
    <el-row>
      <el-col :span="15">
        <img src="static/images/title.png" height="60px" />
      </el-col>
      <el-col :span="3" :push="6">
        <a :href="tarUrl">
          <el-button type="text" icon="el-icon-download">我要试下香不香</el-button>
        </a>
      </el-col>
    </el-row>
  </div>
</template>
<script>
export default {
  name: 'my-header',
  data() {
    return {
      tarUrl: 'http://' + process.env.FILE_IP_PORT + '/download/multiUserExcelApp-win32-x64.zip'
    }
  },
}
</script>
<style scoped>
#appHeader {
  color: #999;
  background-color: white;
  width: 100%;
  position: fixed;
  top: 0px;
  z-index: 999;
}
</style>