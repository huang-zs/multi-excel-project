<template>
  <el-dialog title="我有话要和作者说" :visible.sync="visible" @close="reset">
    <el-form :model="adviceForm" ref="adviceForm">
      <el-form-item label="真实姓名" prop="name">
        <el-input auto-complete="off" v-model="adviceForm.name"></el-input>
      </el-form-item>
      <el-form-item label="联络电话" prop="telPhone">
        <el-input auto-complete="off" v-model="adviceForm.telPhone"></el-input>
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-radio-group v-model="adviceForm.type">
          <el-radio label="complant">投诉</el-radio>
          <el-radio label="advice">建议</el-radio>
          <el-radio label="bug">bug提交</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="描述" prop="describe">
        <el-input auto-complete="off" type="textarea" v-model="adviceForm.describe"></el-input>
      </el-form-item>
      <el-form-item label="图片补充">
        <el-upload
          :action="uri+'/user/fileAdvice'"
          ref="upload"
          :on-remove="handleRemove"
          :on-change="handleChange"
          :file-list="fileList"
          :data="adviceForm"
          list-type="picture"
          :auto-upload="false"
          :limit="5"
        >
          <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
          <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，最多5个，且每个文件不超过500kb</div>
        </el-upload>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取 消</el-button>
      <el-button type="primary" @click="talkToAuthorMethod">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
import { talkToAuthor } from '@/api/user'
export default {
  data() {
    return {
      uri: process.env.BASE_URL,
      adviceForm: {
        name: '',
        telPhone: '',
        type: '',
        describe: ''

      },
      fileList: []

    }
  },
  props: {
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
    reset() {
      console.log(this.$refs)
      this.$refs.adviceForm.resetFields()
      this.fileList = []

    },
    //文件添加
    handleChange(file, fileList) {
      this.fileList = fileList
    },
    //文件移除
    handleRemove(file, fileList) {
      this.fileList = fileList
    },
    //上传建议(判断是否上传file)
    talkToAuthorMethod() {
      console.log(this.adviceForm)
      if (this.fileList.length == 0) {//有上传文件
        talkToAuthor(this.adviceForm).then(response => {
          this.$message.success('我已经收到,谢谢您的建议')
        })
      } else {//无上传文件
        this.$refs.upload.submit()
        this.$message.success('我已经收到,谢谢您的建议')
      }

      this.visible = false
    }
  },

}
</script>