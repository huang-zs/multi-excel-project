<template>
  <el-form :model="newExcelForm" ref="newExcelForm" @submit.native.prevent>
    <el-form-item label="文件名">
      <el-input v-model="newExcelForm.name" placeholder="请输入文件名" />
    </el-form-item>
    <el-form-item label="文件描述">
      <el-input type="textarea" v-model="newExcelForm.fileDescribe" placeholder="请输入文件描述" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="toNewExcel">新建</el-button>
    </el-form-item>
  </el-form>
</template>
<script>
import { create } from '@/api/excel'
export default {
  name: 'new-excel-info',
  data() {
    return {
      newExcelForm: {
        type: 'new', // 代表是newExcel
        name: '',
        fileDescribe: ''
      }
    }
  },
  methods: {
    //调后端接口新建excel
    toNewExcel() {
      console.log(this.newExcelForm)
      let _this = this

      //调后端接口新建excel对象
      create(_this.newExcelForm)
        .then(response => {
          // 接口返回，保存excel对象到store里面
          _this.$store.commit('saveExcel', response.data.data)
          console.log(_this.$store.state.Excel.id)
          //跳转到excel页面
          _this.$router.push({
            path: '/home/newExcel',
            query: {
              excelId: _this.$store.state.Excel.id            }
          })
        })
    }
  }
}
</script>
