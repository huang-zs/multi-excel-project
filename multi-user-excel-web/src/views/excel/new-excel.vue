<template>
  <div>
    <el-container>
      <el-header height="20px">
        <el-button @click="saveExcel">保存</el-button>
        <el-button @click="exportExcel">导出excel</el-button>
      </el-header>
      <div class="spreadContainer">
        <div id='ss' style='width:100%; height:400px;'>
        </div>
      </div>
      <!-- <gc-spread-sheets :hostClass='"spreadHost"'
      @workbookInitialized='spreadInitHandle($event)'
      @valueChanged="valueChanged"
      @columnChanged="columnChanged"
      @rowChanged="rowChanged"
      >
      </gc-spread-sheets> -->
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
      excelId: '',
      sheet: {}
    }
  },
  mounted () {
    let _this = this;
    this.$nextTick(() => {
      _this.spread = new GC.Spread.Sheets.Workbook(document.getElementById('ss'))
      console.log(JSON.parse(JSON.stringify(_this.spread)))
      _this.sheet = _this.spread.getSheet(0);
      _this.spread.bind(GC.Spread.Sheets.Events.ValueChanged, function (s, e) {
          console.log(e);
      });
      _this.spread.bind(GC.Spread.Sheets.Events.RowChanged, function (s, e) {
        //整行
          console.log(e);
      });
      _this.spread.bind(GC.Spread.Sheets.Events.ColumnChanged, function (s, e) {
        //整列
          console.log(e);
      });
      _this.spread.bind(GC.Spread.Sheets.Events.ActiveSheetChanged, function (s, e) {
        //切换sheet
          console.log(e);
      });
       _this.spread.bind(GC.Spread.Sheets.Events.SelectionChanged, function (s, e) {
         //切换单元格
          console.log(e);
      });
      _this.spread.bind(GC.Spread.Sheets.Events.ClipboardPasted, function (sender, args) {
        //粘贴时
          console.log(args);
      });
    })
    
  },
  watch: {
    sheet(newValue, oldVal){
      console.log(newValue)
    }
  },
  computed: {
    dataTable () {
      let dataTable = []
      for (let i = 0; i < 42; i++) {
        dataTable.push({price: i + 0.56})
      }
      return dataTable
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
    cellChanged (sender, args) {
      console.log(1)
      console.log(sender)
      console.log(args)
    },

    spreadInitHandle: function (spread) {
      this.spread = spread
      create({ 'json': JSON.stringify(this.spread.toJSON()) }).then(response => {
        window.console.log(response)
        // 赋值后端返回的excelId 保存用
        this.excelId = response.data.data
        window.console.log('存excelId' + this.excelId)
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
    // 增加删除行
    rowChanged (sender, args) {
      console.log(sender)
      console.log(args)
    },
    // 增加删除列
    columnChanged (sender, args) {
      console.log(sender)
      console.log(args)
    },
    // 单元格改变
    valueChanged (sender, args) {
      console.log(args)
      // this.webSocket.send(JSON.stringify(args))
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
