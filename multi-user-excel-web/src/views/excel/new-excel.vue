<template>
  <div>
    <el-container>
      <el-header height="20px">
<el-button @click="saveExcel">保存</el-button>
      <el-button @click="exportExcel">导出excel</el-button>
      </el-header>
<el-main>

</el-main>
<div class="spreadContainer">
      <gc-spread-sheets :hostClass='"spreadHost"'
      @workbookInitialized='spreadInitHandle($event)'
      @valueChanged="valueChanged"
      @columnChanged="columnChanged"
      @rowChanged="rowChanged"
      >
      </gc-spread-sheets>
    </div>
    </el-container>
</div>
</template>
<script>
import ExcelIO from '@grapecity/spread-excelio'
import FaverSaver from 'file-saver'
import { create, save } from '@/api/excel'
export default {
  name: 'new-excel',
  data () {
    return {
      spread: {},
      webSocket: null,
      excelId: ''
    }
  },
  // 用destroyed会报错
  beforeDestroy () {
    window.console.log('新建excel关闭websocket')
    this.webSocket.close()
    // 离开页面的时候自动保存Excel到后端
    window.console.log('新建excel保存excel')
    this.saveExcel()
  },

  methods: {
    spreadInitHandle: function (spread) {
      this.spread = spread
      let form = (this.$router.currentRoute.params)
      // 如果是import的话就取文件渲染
      if (form.type === 'import') {
        let _this = this
        let ex = new ExcelIO.IO()
        ex.open(form.excelFile, function (json) {
          _this.spread.fromJSON(json)
        }, function (e) {
          console.log(e)
        })
      }
      form.json = JSON.stringify(this.spread.toJSON())
      create(form).then(response => {
        window.console.log(response)
        // 赋值后端返回的excelId 保存用
        this.excelId = response.data.data
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
      window.console.log('保存excel')
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
      this.webSocket.onmessage = this.webSocketOnMessage
      // 窗口关闭时，关闭连接
      window.unload = function () {
        this.webSocket.close()
      }
    },
    // 放 this.webSocket.onmessage 里面会获取不到this.spread,不知道为什么
    webSocketOnMessage (msg) {
      // 转成json对象
      let jsonv = JSON.parse(msg.data)
      this.spread.getSheetFromName(jsonv.sheetName).getCell(jsonv.row, jsonv.col).text(jsonv.newValue)
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
