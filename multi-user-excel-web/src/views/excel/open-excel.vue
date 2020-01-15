<template>
  <div>
    <el-container>
      <el-header height="40px">
        <el-button @click="saveExcel">保存</el-button>
        <el-button @click="exportExcel">导出excel</el-button>
        <el-button
          v-clipboard:copy="excelId"
          v-clipboard:success="onCopy"
          v-clipboard:error="onError"
          disabled=""
        >邀请协助者</el-button>
      </el-header>
      <el-main>
        <div class="spreadContainer">
          <gc-spread-sheets
            :hostClass='"spreadHost"'
            @workbookInitialized="spreadInitHandle($event)"
            @valueChanged="valueChanged"
            @columnChanged="columnChanged"
            @rowChanged="rowChanged"
          ></gc-spread-sheets>
        </div>
      </el-main>
    </el-container>
  </div>
</template>
<script>
import ExcelIO from '@grapecity/spread-excelio'
import FaverSaver from 'file-saver'
import { get, save } from '@/api/excel'
export default {
  name: 'open-excel',
  data () {
    return {
      spread: {},
      webSocket: null,
      excelId: this.$route.query.id,
      shareButtonFlag: true
    }
  },
  beforeDestroy () {
    window.console.log('打开excel关闭websocket')
    // 离开页面的时候自动保存Excel到后端
    this.saveExcel()
    this.webSocket.close()
  },
  methods: {
    spreadInitHandle: function (spread) {
      this.spread = spread
      // 拿excelId 到后端获取excel对象赋值
      get({ 'excelId': this.$route.query.id }).then(response => {
        this.spread.fromJSON(JSON.parse(response.data.data.json))
        console.log(response.data.data)
        let user = JSON.parse(this.$store.state.ExcelUser)
        console.log(user.id)
        this.webSocketInit()
      }).catch(error => {
        window.console.log(error)
        this.$router.push({
          path: '/error'
        })
      })
    },
    exportExcel () {
      let ex = new ExcelIO.IO()
      // console.log(this.spread)
      let json = this.spread.toJSON()
      console.log(json)
      ex.save(json, function (blob) {
        FaverSaver.saveAs(blob, 'exportExcel.xlsx')
      }, function (e) {
        console.log(e)
      })
    },
    // 保存excel 把id 的excel对象修改
    saveExcel () {
      save({ 'json': JSON.stringify(this.spread.toJSON()), 'id': this.excelId }).then(response => {
        window.console.log(response)
      }).catch(error => {
        window.console.log(error)
        this.$router.push({
          path: '/error'
        })
      })
    },
    rowChanged (sender, args) {
      //
    },
    columnChanged (sender, args) {
      //
    },
    valueChanged (sender, args) {
      console.log(args)
      this.webSocket.send(JSON.stringify(args))
    },
    webSocketInit () {
      let _this = this
      console.log('websocket初始化' + this.excelId)
      this.webSocket = new WebSocket('ws://localhost:8081/ws/asset/' + this.excelId)
      // 连接打开事件
      this.webSocket.onopen = function () {
        window.console.log('Socket 已打开')
      }
      // 连接关闭事件
      this.webSocket.onclose = function () {
        window.console.log('Socket已关闭')
      }
      // 发生了错误事件
      this.webSocket.onerror = function () {
        alert('Socket发生了错误')
      }
      // 收到消息事件
      this.webSocket.onmessage = function (msg) {
        // 转成json对象
        let jsonv = JSON.parse(msg.data)
        _this.spread.getSheetFromName(jsonv.sheetName).getCell(jsonv.row, jsonv.col).text(jsonv.newValue)
      }
      // 窗口关闭时，关闭连接
      window.unload = function () {
        this.webSocket.close()
      }
    },
    onCopy (e) {
      this.$message.success('内容已复制到剪切板！')
    },
    // 复制失败时的回调函数
    onError (e) {
      this.$message.error('抱歉，复制失败！')
    }
  }
}
</script>

<style scoped>
.spreadContainer {
  padding: 10px;
  box-shadow: 0 0 20px red;
  width: 95%;
  height: 400px;
}
.spreadHost {
  width: 100%;
  height: 100%;
}
</style>
