<template>
    <el-form :model="importExcelForm" ref="importExcelForm" @submit.native.prevent>
        <el-form-item label="导入文件"><br>
          <input id="excelFile" type="file" >
        </el-form-item>
        <el-form-item label="文件描述">
          <el-input type="textarea" v-model="importExcelForm.describe" placeholder="请输入文件描述" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="toNewExcel">导入</el-button>
        </el-form-item>
      </el-form>
</template>
<script>
export default {
  name: 'new-excel-info',
  data () {
    return {
      importExcelForm: {
        type: 'import', // 代表是importExcel
        name: '',
        describe: '',
        excelFile: null,
        json: ''
      }
    }
  },
  methods: {
    toNewExcel () {
      const file = document.getElementById('excelFile').files[0]
      const fileName = file.name
      this.importExcelForm.name = fileName.substring(fileName.lastIndexOf('.'))
      this.importExcelForm.excelFile = file
      this.$router.push({
        name: 'newExcel',
        params: this.importExcelForm}
      )
    }
  }
}
</script>
