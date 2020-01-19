<template>
  <div>
    <el-container>
      <el-header height="40px">
        <el-button @click="saveExcel">保存</el-button>
        <el-button @click="exportExcel">导出excel</el-button>
        <!--可以抽取组件，但失败-->
        <el-button
          v-clipboard:copy="excelId"
          v-clipboard:success="onCopy"
          v-clipboard:error="onError"
        >邀请协助者</el-button>
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
    this.webSocketInit()
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
        console.log(sender)
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
    // 离开页面的时候自动保存Excel到后端
    window.console.log('新建excel保存excel')
    this.saveExcel()
    window.console.log('新建excel关闭websocket')
    this.webSocket.close()
  },
  mounted () {
    console.log(this.$store.state)
  },
  methods: {
    cellChanged (sender, args) {
      console.log(1)
      console.log(sender)
      console.log(args)
    },

    spreadInitHandle: function (spread) {
      let _this = this
      this.spread = spread
      let form = (this.$router.currentRoute.params)
      // 如果是import的话就取文件渲染
      if (form.type === 'import') {
        let ex = new ExcelIO.IO()
        ex.open(form.excelFile, function (json) {
          _this.spread.fromJSON(json)
        }, function (e) {
          console.log(e)
        })
      }
      form.json = JSON.stringify(this.spread.toJSON())
      create(form).then(response => {
        // 保存excel对象
        this.$store.commit('saveExcel', response.data.data)
        _this.excelId = this.$store.state.excel.id
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
      this.$store.commit('updateExcel', JSON.stringify(this.spread.toJSON()))
      save(this.$store.state.excel).then(response => {
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
      let _this = this
      console.log('websocket初始化' + this.$store.state.excel.id)
      this.webSocket = new WebSocket('ws://localhost:8081/multi-user-excel-system/ws/asset/' + this.$store.state.excel.id)
      // this.webSocket = new WebSocket('ws://118.190.156.144:8081/multi-user-excel-system/ws/asset/' + this.$store.state.excel.id)
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
