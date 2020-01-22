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
      <el-main>
        <div class="spreadContainer">
          <div id="ss" style="width:100%; height:400px;"></div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>
<script>
import ExcelIO from '@grapecity/spread-excelio'
import FaverSaver from 'file-saver'
import { create, save, get } from '@/api/excel'
import { minusArray } from '@/api/utils'
import GC from '@grapecity/spread-sheets'
export default {
  name: 'new-excel',
  data() {
    return {
      spread: {},
      sheetsName: [],
      webSocket: null,
      excelId: '',
      rowChangedFlag: true, //收到websocket时置false
      ColumnChangedFlag: true //收到websocket时置false
    }
  },
  mounted() {
    console.log('excel123')
    //获取上一个页面传过来的 excelId
    this.excelId = this.$route.query.excelId

    let _this = this

    this.$nextTick(() => {
      //excel初始化
      _this.spread = new GC.Spread.Sheets.Workbook(
        document.getElementById('ss')
      )
      // 拿excelId 到后端获取excel对象
      get({ 'excelId': _this.excelId }).then(response => {
        //把后端返回的excel对象存进store
        _this.$store.commit('saveExcel', response.data.data)
        //渲染excel
        _this.spread.fromJSON(JSON.parse(response.data.data.json))
        //把当前的sheet名数组存起来
        _this.sheetsName = Object.keys(JSON.parse(JSON.stringify(_this.spread)).sheets)
        //  打开websocket
        _this.webSocketInit()
      }).catch(error => {
        window.console.log(error)
        this.$router.push({
          path: '/error'
        })
      })
      // 值
      _this.spread.bind(GC.Spread.Sheets.Events.ValueChanged, function (s, e) {
        console.log('发送ValueChanged')
        _this.webSocket.send(JSON.stringify({ type: s.type, data: e }))
      })
      // 整行
      _this.spread.bind(GC.Spread.Sheets.Events.RowChanged, function (s, e) {
        console.log('发送RowChanged')
        // 防止收到websocket后excel监听到变化后又发websocket
        if (_this.rowChangedFlag) {
          _this.webSocket.send(JSON.stringify({ type: s.type, data: e }))
        } else {
          _this.rowChangedFlag = true
        }
      })
      // 整列
      _this.spread.bind(GC.Spread.Sheets.Events.ColumnChanged, function (s, e) {
        console.log('发送ColumnChanged')
        // 防止收到websocket后excel监听到变化后又发websocket
        if (_this.ColumnChangedFlag) {
          _this.webSocket.send(JSON.stringify({ type: s.type, data: e }))
        } else {
          _this.ColumnChangedFlag = true
        }
      })
      // 切换sheet
      _this.spread.bind(GC.Spread.Sheets.Events.ActiveSheetChanged, function (
        s,
        e
      ) {
        console.log('发送ActiveSheetChanged')
        let spread = JSON.parse(JSON.stringify(_this.spread))
        //旧的sheet名数组
        let newSheetsName = Object.keys(spread.sheets)
        //新的sheet名数组
        let oldSheetsName = _this.sheetsName
        //判断这次切换sheet属于哪种情况,用sheet名字数组判断
        if (oldSheetsName.length === newSheetsName.length) {//换sheet
          console.log('换sheet')
        } else {//新增sheet
          if (oldSheetsName.length < newSheetsName.length) {
            _this.$set(e, 'type', 'add')
            //获取新增的sheet名
            let result = minusArray(newSheetsName, oldSheetsName)
            console.log('增加' + result)
            _this.$set(e, 'sheetName', result[0])
            console.log(e)
          } else {//删除sheet
            _this.$set(e, 'type', 'delete')
            //获取删除的sheet名
            let result = minusArray(oldSheetsName, newSheetsName)
            console.log('减少' + result)
            _this.$set(e, 'sheetName', result[0])
            console.log(e)
          }
          _this.sheetsName = newSheetsName
          _this.webSocket.send(JSON.stringify({ type: s.type, data: e }))
        }


      })
      // // 切换单元格
      // _this.spread.bind(GC.Spread.Sheets.Events.SelectionChanged, function (
      //   s,
      //   e
      // ) {
      //   console.log('SelectionChanged')
      //   console.log(s)
      //   console.log(e)
      // })
      // 粘贴时
      _this.spread.bind(GC.Spread.Sheets.Events.ClipboardPasted, function (
        s,
        e
      ) {
        console.log('发送ClipboardPasted')
        //获取要操作的sheet
        let sheet = _this.spread
          .getSheetFromName(e.sheetName)
        let rowIndex = e.cellRange.row
        let rowCount = e.cellRange.rowCount
        let colIndex = e.cellRange.col
        let colCount = e.cellRange.colCount
        let dataArray = []
        //把复制的值放到一个二元数组里面
        for (let row = rowIndex, i = 0; row < rowIndex + rowCount; row++ , i++) {
          dataArray[i] = []
          for (let col = colIndex, j = 0; col < colIndex + colCount; col++ , j++) {
            console.log(sheet
              .getCell(row, col).text())
            dataArray[i][j] = sheet
              .getCell(row, col).text()
          }
        }
        //把值数组赋给e
        _this.$set(e, 'dataArray', dataArray)
        _this.webSocket.send(JSON.stringify({ type: s.type, data: e }))
      })
    })
  },
  // 用destroyed会报错
  beforeDestroy() {
    // 离开页面的时候自动保存Excel到后端
    window.console.log('保存excel')
    this.saveExcel()
    // 离开页面的时候断开websocket
    window.console.log('关闭websocket')
    this.webSocket.close()
  },
  methods: {
    //导出excel
    exportExcel() {
      let ex = new ExcelIO.IO()
      let json = this.spread.toJSON()
      console.log(json)
      ex.save(
        json,
        function (blob) {
          FaverSaver.saveAs(blob, 'exportExcel.xlsx')
        },
        function (e) {
          console.log(e)
        }
      )
    },

    // 保存excel 把id 的excel对象修改
    saveExcel() {
      window.console.log('保存excel')
      //先把当前最新的json保存到store
      this.$store.commit('updateExcel', JSON.stringify(this.spread.toJSON()))
      save(this.$store.state.Excel)
        .then(response => {
          window.console.log(response)
        })
        .catch(error => {
          window.console.log(error)
          this.$router.push({
            path: '/error'
          })
        })
    },
    webSocketInit() {
      let _this = this
      console.log('websocket初始化' + process.env.WEBSOCKET_URL + _this.excelId)
      this.webSocket = new WebSocket(process.env.WEBSOCKET_URL + _this.excelId)
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
        console.log('收到信息')

        // 转成json对象
        let jsonv = JSON.parse(msg.data)
        let data = jsonv.data
        console.log(data)
        //判断excel进行什么变化
        switch (jsonv.type) {

          case 'ValueChanged'://值改变
            console.log('收到ValueChanged')
            _this.spread
              .getSheetFromName(data.sheetName)
              .getCell(data.row, data.col)
              .text(data.newValue)
            break

          case 'RowChanged'://行改变
            console.log('收到RowChanged')
            _this.rowChangedFlag = false
            if (data.propertyName === 'addRows') {//加行
              _this.spread
                .getSheetFromName(data.sheetName).addRows(data.row, data.count)
            } else if (data.propertyName === 'deleteRows') {//减行
              _this.spread
                .getSheetFromName(data.sheetName).deleteRows(data.row, data.count)
            }
            break

          case 'ColumnChanged'://列改变
            console.log('收到ColumnChanged')
            _this.ColumnChangedFlag = false
            if (data.propertyName === 'addColumns') {
              _this.spread
                .getSheetFromName(data.sheetName).addColumns(data.col, data.count)
            } else if (data.propertyName === 'deleteColumns') {
              _this.spread
                .getSheetFromName(data.sheetName).deleteColumns(data.col, data.count)
            }
            break

          case 'ClipboardPasted'://粘贴
            console.log('收到ClipboardPasted')
            console.log(data)
            _this.spread
              .getSheetFromName(data.sheetName).setArray(data.cellRange.row, data.cellRange.col, data.dataArray)
            break

          case 'ActiveSheetChanged'://sheet变化
            console.log('收到ActiveSheetChanged')
            console.log(jsonv)
            if (data.type === 'add') {//新增sheet
              _this.spread.addSheet(_this.spread.getSheetCount())
            } else {//删除sheet
              //先根据sheetname获取index，再删除第index的sheet
              _this.spread.removeSheet(_spread.getSheetIndex(data.sheetName))
            }
            break
          default:
            console.log('others')

            break
        }

      }
      // 窗口关闭时，关闭连接
      window.unload = function () {
        this.webSocket.close()
      }
    },
    onCopy(e) {
      this.$message.success('内容已复制到剪切板！')
    },
    // 复制失败时的回调函数
    onError(e) {
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
