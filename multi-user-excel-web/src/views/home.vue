<template>
  <div class="mainFrame">
    <el-container>
      <el-aside width="200px">
        <el-menu default-active>
          <el-submenu index>
            <template slot="title">
              <i class="el-icon-tickets">我的功能</i>
            </template>
            <router-link to="/home/openExcelList">
              <el-menu-item index>打开Excel</el-menu-item>
            </router-link>
            <router-link to="/home/newExcelInfo">
              <el-menu-item index>新建Excel</el-menu-item>
            </router-link>
            <router-link to="/home/importExcelInfo">
              <el-menu-item index>导入Excel</el-menu-item>
            </router-link>
          </el-submenu>
          <el-submenu index>
            <template slot="title">
              <i class="el-icon-delete">回收站</i>
            </template>
            <router-link to="/home/deletedExcelList">
              <el-menu-item index>恢复Excel</el-menu-item>
            </router-link>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'home'
}
</script>
