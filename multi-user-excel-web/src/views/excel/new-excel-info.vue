<template>
    <el-form :model="newExcelForm" ref="newExcelForm" @submit.native.prevent>
        <el-form-item label="文件名">
          <el-input v-model="newExcelForm.name" placeholder="请输入文件名" />
        </el-form-item>
        <el-form-item label="文件描述">
          <el-input type="textarea" v-model="newExcelForm.describe" placeholder="请输入文件描述" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="toNewExcel">新建</el-button>
        </el-form-item>
      </el-form>
</template>
<script>
export default {
  name: 'new-excel-info',
  data () {
    return {
      newExcelForm: {
        type: 'new', // 代表是newExcel
        name: '',
        describe: '',
        json: ''
      }
    }
  },
  methods: {
    toNewExcel () {
      console.log(this.newExcelForm)
      this.$router.push({
        name: 'newExcel',
        params: this.newExcelForm}
      )
    }
  }
}
</script>
