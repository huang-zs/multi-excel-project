<template>
  <el-dialog title="修改excel信息" :visible.sync="visible">
    <el-form>
      <el-form-item label="文件名">
        <el-input v-model="excel.name" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="文件描述">
        <el-input v-model="excel.excelDescribe" auto-complete="off" type="textarea"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取 消</el-button>
      <el-button type="primary" @click="updateExcelInfoMethod">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
export default {
  props: {
    excel: {
      default: {
      }
    },
    dialogVisible: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    visible: {
      get() {
        return this.dialogVisible
      },
      set() {
        this.$emit('dialogVisibleClose', false)
      }
    }
  },
  methods: {
    updateExcelInfoMethod() {
      console.log(this.excel)
    }
  },
}
</script>