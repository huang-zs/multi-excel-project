<template>
  <el-form :model="importExcelForm" ref="importExcelForm" @submit.native.prevent>
    <el-form-item label="导入文件">
      <br />
      <input id="excelFile" type="file" />
    </el-form-item>
    <el-form-item label="文件描述">
      <el-input type="textarea" v-model="importExcelForm.excelDescribe" placeholder="请输入文件描述" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="toNewExcel">导入</el-button>
    </el-form-item>
  </el-form>
</template>
<script>
import { create } from '@/api/excel'
import ExcelIO from '@grapecity/spread-excelio'
export default {
  name: 'import-excel-info',
  data() {
    return {
      importExcelForm: {
        type: 'import', // 代表是importExcel
        name: '', //excel文件名
        excelDescribe: '', //excel文件描述
        json: ''
      }
    }
  },
  methods: {
    //解析excel文件成json后调后端接口新建excel
    toNewExcel() {
      let _this = this
      let file = document.getElementById('excelFile').files[0]
      let fileName = file.name
      //赋值文件名
      _this.importExcelForm.name = fileName.substring(0, fileName.lastIndexOf('.'))
      console.log('导入excel')
      let ex = new ExcelIO.IO()
      //读取excel文件为json
      ex.open(
        file,
        function (json) {
          //赋值json
          _this.importExcelForm.json = JSON.stringify(json)

          //调后端接口新建excel对象
          create(_this.importExcelForm)
            .then(response => {
              // 接口返回，保存excel对象到store里面
              _this.$store.commit('saveExcel', response.data.data)
              console.log(_this.$store.state.Excel.id)
              //跳转到excel页面
              _this.$router.push({
                path: '/excel',
                query: {
                  excelId: _this.$store.state.Excel.id                }
              })
            })

        },
        function (e) {
          //读excel报错
          console.log(e)
        }
      )

    }
  }
}
</script>
