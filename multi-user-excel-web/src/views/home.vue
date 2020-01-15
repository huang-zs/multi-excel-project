<template>
 <el-container>
  <el-aside width="200px">
    <el-menu default-active="">
      <el-submenu index="" title="我的功能">
        <template slot="title"><i class="el-icon-message"></i>我的功能</template>
        <!-- <el-menu-item index=""><router-link to="/home/newExcel">新建Excel</router-link></el-menu-item> -->
        <!-- <el-menu-item index=""><router-link :to= "{path:'/home/openExcel',query:{id:'user20191231101250-excel-20200111103555'}}">打开Excel</router-link></el-menu-item> -->
        <el-menu-item index=""><router-link to="/home/openExcelList">打开Excel</router-link></el-menu-item>
        <!-- <el-menu-item index=""><router-link to="/home/openExcel">导入Excel</router-link></el-menu-item> -->
        <el-menu-item index=""><router-link to="/home/newExcelInfo">新建Excel</router-link></el-menu-item>
        <el-menu-item index=""><router-link to="/home/importExcelInfo">导入Excel</router-link></el-menu-item>
        <el-menu-item index=""><router-link to="/home/test">test</router-link></el-menu-item>
        <!-- <el-menu-item index=""><router-link to="/home/testExcel">测试Excel</router-link></el-menu-item> -->
      </el-submenu>
      <el-submenu index="" title="回收站">
        <template slot="title"><i class="el-icon-message"></i>回收站</template>
        <el-menu-item index="">单人Excel</el-menu-item>
        <el-menu-item index="">多人Excel</el-menu-item>
      </el-submenu>
    </el-menu>
  </el-aside>
  <el-main>
     <router-view/>
  </el-main>
</el-container>

</template>

<script>
export default {
  name: 'home'
}
</script>
