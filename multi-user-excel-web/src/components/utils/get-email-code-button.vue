<template>
  <div>
    <el-button
      icon="el-icon-message"
      @click="openDialog"
      preventReClick
      :disabled="disableFlag"
    >获取验证码</el-button>
    <el-dialog title="发送邮箱验证码" :visible.sync="dialogFormVisible">
      <el-form>
        <el-form-item label="邮箱" v-show="false">
          <el-input v-model="email" auto-complete="off"></el-input>
        </el-form-item>
        <canvas id="s-canvas" :width="contentWidth" :height="contentHeight"></canvas>
        <el-button type="text" @click="changeImageCode">看不清，换一张</el-button>
        <el-form-item label="输入验证码">
          <el-input v-model="inputCode" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="getCodeMethod" :disabled="inputCode.length!=4">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// 获取邮箱验证码的组件，传入将要发送的邮箱
import { getCode } from '@/api/user'
import { guid } from '@/api/utils'
export default {
  name: 'get-email-button',
  props: {
    //传入的将要发送邮件的邮箱
    email: {
      type: String
    },
    //按钮是否可点
    disableFlag: {
      type: Boolean
    },
    fontSizeMin: {
      type: Number,
      default: 16
    },
    fontSizeMax: {
      type: Number,
      default: 40
    },
    backgroundColorMin: {
      type: Number,
      default: 180
    },
    backgroundColorMax: {
      type: Number,
      default: 240
    },
    colorMin: {
      type: Number,
      default: 50
    },
    colorMax: {
      type: Number,
      default: 160
    },
    lineColorMin: {
      type: Number,
      default: 40
    },
    lineColorMax: {
      type: Number,
      default: 180
    },
    dotColorMin: {
      type: Number,
      default: 0
    },
    dotColorMax: {
      type: Number,
      default: 255
    },
    contentWidth: {
      type: Number,
      default: 112
    },
    contentHeight: {
      type: Number,
      default: 38
    }  },
  data() {
    return {
      //图片验证码值
      imageCode: '',
      //输入验证码值
      inputCode: '',
      dialogFormVisible: false
    }
  },
  watch: {
    imageCode() {
      this.drawPic()
    }
  },
  methods: {
    //打开发送框
    openDialog() {
      this.dialogFormVisible = true
      this.inputCode = ''
      //加延时，不加没渲染完会报错
      setTimeout(() => {
        this.imageCode = guid(4)
      }, 500)
    },

    changeImageCode() {
      this.imageCode = guid(4)
    },
    // 请求后端获取邮箱验证码
    getCodeMethod() {
      //判断输入的验证码和图片的验证码是否一致

      if (this.imageCode != this.inputCode) {
        this.inputCode = ''
        this.$message({
          message: '验证码不正确，请重新输入',
          type: 'error',
          duration: 2000
        })
        return
      }

      getCode({ email: this.email }).then(response => {
        if (response.data.code === 200) {
          this.$message({
            message: '邮箱验证码已发送，请注意查收',
            type: 'success',
            duration: 2000
          })
        } else {
          this.$message({
            message: response.data.msg,
            type: 'error',
            duration: 2000
          })
        }
        this.dialogFormVisible = false
      })
    },
    // 生成一个随机数
    randomNum(min, max) {
      return Math.floor(Math.random() * (max - min) + min)
    },
    // 生成一个随机的颜色
    randomColor(min, max) {
      let r = this.randomNum(min, max)
      let g = this.randomNum(min, max)
      let b = this.randomNum(min, max)
      return 'rgb(' + r + ',' + g + ',' + b + ')'
    },
    drawPic() {
      let canvas = document.getElementById('s-canvas')
      let ctx = canvas.getContext('2d')
      ctx.textBaseline = 'bottom'
      // 绘制背景
      ctx.fillStyle = this.randomColor(this.backgroundColorMin, this.backgroundColorMax)
      ctx.fillRect(0, 0, this.contentWidth, this.contentHeight)
      // 绘制文字
      for (let i = 0; i < this.imageCode.length; i++) {
        this.drawText(ctx, this.imageCode[i], i)
      }
      this.drawLine(ctx)
      this.drawDot(ctx)
    },
    drawText(ctx, txt, i) {
      ctx.fillStyle = this.randomColor(this.colorMin, this.colorMax)
      ctx.font = this.randomNum(this.fontSizeMin, this.fontSizeMax) + 'px SimHei'
      let x = (i + 1) * (this.contentWidth / (this.imageCode.length + 1))
      let y = this.randomNum(this.fontSizeMax, this.contentHeight - 5)
      var deg = this.randomNum(-45, 45)
      // 修改坐标原点和旋转角度
      ctx.translate(x, y)
      ctx.rotate(deg * Math.PI / 180)
      ctx.fillText(txt, 0, 0)
      // 恢复坐标原点和旋转角度
      ctx.rotate(-deg * Math.PI / 180)
      ctx.translate(-x, -y)
    },
    drawLine(ctx) {
      // 绘制干扰线
      for (let i = 0; i < 8; i++) {
        ctx.strokeStyle = this.randomColor(this.lineColorMin, this.lineColorMax)
        ctx.beginPath()
        ctx.moveTo(this.randomNum(0, this.contentWidth), this.randomNum(0, this.contentHeight))
        ctx.lineTo(this.randomNum(0, this.contentWidth), this.randomNum(0, this.contentHeight))
        ctx.stroke()
      }
    },
    drawDot(ctx) {
      // 绘制干扰点
      for (let i = 0; i < 100; i++) {
        ctx.fillStyle = this.randomColor(0, 255)
        ctx.beginPath()
        ctx.arc(this.randomNum(0, this.contentWidth), this.randomNum(0, this.contentHeight), 1, 0, 2 * Math.PI)
        ctx.fill()
      }
    }
  },
}
</script>