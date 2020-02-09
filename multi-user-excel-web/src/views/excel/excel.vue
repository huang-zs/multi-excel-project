<template>
  <div class="mainFrame">
    <el-container>
      <el-header height="40px">
        <span>
          <el-tooltip class="item" effect="dark" content="保存并退出" placement="top">
            <el-button size="small" icon="el-icon-arrow-left" v-goBack></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="文件名" placement="top">
            <el-tag type="info">{{excelName}}</el-tag>
          </el-tooltip>
          <!-- <span>表格每30s自动保存</span> -->
        </span>
        <div style="float:right">
          <el-tooltip class="item" effect="dark" content="保存" placement="top">
            <el-button @click="hardSaveExcel" size="small">保存</el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="导出Excel到文件" placement="top">
            <el-button @click="exportExcel" size="small">导出excel</el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="邀请协助者一起编辑excel" placement="top">
            <el-button
              v-clipboard:copy="excelId"
              v-clipboard:success="onCopy"
              v-clipboard:error="onError"
              size="small"
            >邀请协助者</el-button>
          </el-tooltip>

          <el-dropdown>
            <span class="el-dropdown-link">
              <el-badge :value="users.length" class="item">
                <el-button size="small">协助ing</el-button>
              </el-badge>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-for="item in users" :key="item">{{item}}</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>

        <!--可以抽取组件，但失败-->
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
import { create, easySave, hardSave, get } from '@/api/excel'
import { minusArray, getColor } from '@/api/utils'
import GC from '@grapecity/spread-sheets'
export default {
  name: 'excel',
  data() {
    return {
      spread: {},
      sheetsName: [],
      users: [],
      userColors: {},
      timer: null,
      webSocket: null,
      excelId: '',
      excelName: '',
      rowChangedFlag: true, //收到websocket时置false
      ColumnChangedFlag: true,//收到websocket时置false
    }
  },
  mounted() {
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
        _this.excelName = _this.$store.state.Excel.name
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
        console.log(e)
        _this.webSocket.send(JSON.stringify({ type: s.type, data: e }))
      })
      // 整行
      _this.spread.bind(GC.Spread.Sheets.Events.RowChanged, function (s, e) {
        // 防止收到websocket后excel监听到变化后又发websocket
        if (_this.rowChangedFlag) {
          console.log('发送RowChanged')
          console.log(e)
          _this.webSocket.send(JSON.stringify({ type: s.type, data: e }))
        } else {
          _this.rowChangedFlag = true
        }
      })
      // 整列
      _this.spread.bind(GC.Spread.Sheets.Events.ColumnChanged, function (s, e) {
        // 防止收到websocket后excel监听到变化后又发websocket
        if (_this.ColumnChangedFlag) {
          console.log('发送ColumnChanged')
          console.log(e)
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
        let spread = JSON.parse(JSON.stringify(_this.spread))
        //新的sheet名数组
        let newSheetsName = Object.keys(spread.sheets)
        //旧的sheet名数组
        let oldSheetsName = _this.sheetsName
        //判断这次切换sheet属于哪种情况,用sheet名字数组判断
        if (oldSheetsName.length === newSheetsName.length) {//换sheet
          console.log('换sheet')
        } else {//新增sheet
          let e = {}
          if (oldSheetsName.length < newSheetsName.length) {
            _this.$set(e, 'type', 'add')
            //获取新增的sheet名
            let result = minusArray(newSheetsName, oldSheetsName)
            _this.$set(e, 'sheetName', result[0])
            console.log('增加' + result)
          }
          else {//删除sheet
            _this.$set(e, 'type', 'delete')
            //获取删除的sheet名
            let result = minusArray(oldSheetsName, newSheetsName)
            _this.$set(e, 'sheetName', result[0])
            console.log('减少' + result)
          }
          _this.sheetsName = newSheetsName
          _this.webSocket.send(JSON.stringify({ type: s.type, data: e }))
          console.log('发送ActiveSheetChanged')
          console.log(e)
        }


      })
      //修改sheet名称
      _this.spread.bind(GC.Spread.Sheets.Events.SheetNameChanged, function (
        s,
        e
      ) {
        console.log('发送SheetNameChanged')
        console.log(e)
        _this.webSocket.send(JSON.stringify({ type: s.type, data: e }))

      })
      // //编辑开始
      // this.spread.bind(GC.Spread.Sheets.Events.EditStarting, function (s, e) {
      //   //EditStarting  
      //   console.log('发送EditStarting')
      //   _this.$set(e, 'userName', _this.$store.state.ExcelUser.name)
      //   console.log(e)
      //   _this.webSocket.send(JSON.stringify({ type: s.type, data: e }))

      // })
      // //编辑结束
      // this.spread.bind(GC.Spread.Sheets.Events.EditEnded, function (s, e) {
      //   //EditStarting  
      //   console.log('EditEnded')
      //   _this.$set(e, 'userName', _this.$store.state.ExcelUser.name)
      //   console.log(e)
      //   _this.webSocket.send(JSON.stringify({ type: s.type, data: e }))

      // })
      // 粘贴时
      _this.spread.bind(GC.Spread.Sheets.Events.ClipboardPasted, function (
        s,
        e
      ) {

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
            dataArray[i][j] = sheet
              .getCell(row, col).text()
          }
        }
        //把值数组赋给e
        _this.$set(e, 'dataArray', dataArray)
        console.log('发送ClipboardPasted')
        console.log(e)
        _this.webSocket.send(JSON.stringify({ type: s.type, data: e }))
      })
    })
    //初始化每30s存一次redis的定时器
    this.timer = setInterval(() => {
      this.easySaveExcel()
      this.$message({
        message: '每30s自动保存',
        type: 'success',
        duration: 1000
      })
    }, 30000)
  },
  // 用destroyed会报错
  beforeDestroy() {
    if (this.timer)
      clearInterval(this.timer)
    // 离开页面的时候自动保存Excel到后端
    window.console.log('保存excel')
    this.hardSaveExcel()
    // 离开页面的时候断开websocket
    window.console.log('关闭websocket')
    if (this.webSocket)
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
    easySaveExcel() {
      window.console.log('保存excel')
      //先把当前最新的json保存到store
      this.$store.commit('updateExcel', JSON.stringify(this.spread.toJSON()))
      easySave(this.$store.state.Excel)

    },
    // 保存excel 把id 的excel对象修改
    hardSaveExcel() {
      window.console.log('保存excel')
      //先把当前最新的json保存到store
      this.$store.commit('updateExcel', JSON.stringify(this.spread.toJSON()))
      hardSave(this.$store.state.Excel)
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

      console.log('websocket初始化' + process.env.WEBSOCKET_URL + _this.excelId + '/' + JSON.parse(_this.$store.state.ExcelUser).name)
      this.webSocket = new WebSocket(process.env.WEBSOCKET_URL + _this.excelId + '/' + JSON.parse(_this.$store.state.ExcelUser).name)
      // 连接打开事件
      this.webSocket.onopen = function () {
        window.console.log('Socket 已打开')
      }
      // 连接关闭事件
      this.webSocket.onclose = function () {
        window.console.log('Socket已关闭')
        // _this.webSocketInit()
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
        console.log(jsonv)

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
            _this.spread
              .getSheetFromName(data.sheetName).setArray(data.cellRange.row, data.cellRange.col, data.dataArray)
            break

          case 'SheetNameChanged'://sheet名字改变
            console.log('收到SheetNameChanged')
            //先根据sheetname获取index，在第index个sheet更改名字
            _this.spread.sheets[_this.spread.getSheetIndex(data.oldValue)].name(data.newValue)
            break

          case 'ActiveSheetChanged'://sheet变化
            console.log('收到ActiveSheetChanged')
            if (data.type === 'add') {//新增sheet
              _this.spread.addSheet(_this.spread.getSheetCount())
            } else {//删除sheet
              //先根据sheetname获取index，再删除第index的sheet,可行，sheetname唯一
              _this.spread.removeSheet(_this.spread.getSheetIndex(data.sheetName))
            }
            _this.sheetsName = Object.keys(JSON.parse(JSON.stringify(_this.spread)).sheets)
            break
          case 'users'://编辑中用户
            console.log('收到users')
            // console.log(_this.users)
            // console.log(data)
            // if (_this.users.length == 0) {//第一次进来
            //   data.forEach(element => {
            //     _this.$set(_this.userColors, element, new GC.Spread.Sheets.LineBorder(getColor(), GC.Spread.Sheets.LineStyle.double))
            //   })
            //   console.log('前面没用户')

            // } else {

            //   if (_this.users.length < data.length) {//有加入


            //     minusArray(data, _this.users).forEach(element => {
            //       _this.$set(_this.userColors, element, new GC.Spread.Sheets.LineBorder(getColor(), GC.Spread.Sheets.LineStyle.double))
            //     })
            //     console.log('新增')

            //   } else {//有退出
            //     minusArray(_this.users, data).forEach(element => {
            //       _this.$set(_this.userColors, element, new GC.Spread.Sheets.LineBorder(getColor(), GC.Spread.Sheets.LineStyle.double))

            //     })
            //     console.log('减少')

            //   }
            // }
            // console.log(_this.userColors)
            _this.users = data

            break
          // case 'EditStarting'://用户编辑开始
          //   console.log(_this.userColors)

          //   _this.spread.sheets[_this.spread.getSheetIndex(data.sheetName)].getCell(data.row, data.col).setBorder(_this.userColors.userName, { all: true })
          //   break
          // case 'EditEnded'://用户编辑结束
          //   _this.spread.sheets[_this.spread.getSheetIndex(data.sheetName)].getCell(data.row, data.col).setBorder(new GC.Spread.Sheets.LineBorder('#D4D4D4', GC.Spread.Sheets.LineStyle.thin), { all: true })
          //   break
          default:
            console.log('others')

            break
        }
        //立即渲染？
        _this.spread.repaint()
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
  /* padding: 10px;
  box-shadow: 0 0 20px red; */
  width: 100%;
  height: 100%;
}
.spreadHost {
  width: 100%;
  height: 100%;
}
</style>
