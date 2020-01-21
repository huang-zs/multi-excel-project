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
import GC from '@grapecity/spread-sheets'
export default {
  name: 'new-excel',
  data() {
    return {
      spread: {},
      webSocket: null,
      excelId: '',
      rowChangedFlag: true, //收到websocket时置false
      ColumnChangedFlag: true //收到websocket时置false
    }
  },
  mounted() {
    console.log('excel123')
    //获取上一个页面传过来的 文件名、文件描述、类型
    let form = this.$router.currentRoute.params
    console.log(form)
    console.log('上个页面')

    let _this = this
    this.$nextTick(() => {
      //excel初始化
      _this.spread = new GC.Spread.Sheets.Workbook(
        document.getElementById('ss')
      )
      //如果是类型是打开文件的话就调后端获取excel的json渲染
      if (form.type === 'open') {
        console.log('打开excel')
        // 拿excelId 到后端获取excel对象
        get({ 'excelId': form.id }).then(response => {
          //把后端返回的excel对象存进store
          this.$store.commit('saveExcel', response.data.data)
          _this.excelId = this.$store.state.excel.id
          //渲染excel
          this.spread.fromJSON(JSON.parse(response.data.data.json))
          //  打开websocket
          this.webSocketInit()
        }).catch(error => {
          window.console.log(error)
          this.$router.push({
            path: '/error'
          })
        })
      } else {
        //如果类型是导入excel,读取上个页面传过来的excel文件渲染
        if (form.type === 'import') {
          console.log('导入excel')
          let ex = new ExcelIO.IO()
          //读取excel文件为json
          ex.open(
            form.excelFile,
            function (json) {
              //渲染excel
              _this.spread.fromJSON(json)
            },
            function (e) {
              console.log(e)
            }
          )

        }
        console.log('新建excel')
        //给上送报文赋 excel的json
        form.json = JSON.stringify(this.spread.toJSON())
        //调后端接口新建excel对象
        create(form)
          .then(response => {
            // 接口返回，保存excel对象到store里面
            this.$store.commit('saveExcel', response.data.data)
            _this.excelId = this.$store.state.excel.id
            //  打开websocket
            this.webSocketInit()
          })
          .catch(error => {
            window.console.log(error)
            this.$router.push({
              path: '/error'
            })
          })
      }
      console.log(JSON.parse(JSON.stringify(_this.spread)))
      //只值
      _this.spread.bind(GC.Spread.Sheets.Events.ValueChanged, function (s, e) {
        console.log('ValueChanged')
        console.log(s)
        console.log(e)
        console.log(JSON.parse(JSON.stringify(e)))
        _this.webSocket.send(JSON.stringify({ type: s.type, data: e }))
      })
      // 整行
      _this.spread.bind(GC.Spread.Sheets.Events.RowChanged, function (s, e) {
        console.log('RowChanged')
        console.log(s)
        console.log(e)
        if (_this.rowChangedFlag) {
          _this.webSocket.send(JSON.stringify({ type: s.type, data: e }))
        } else {
          _this.rowChangedFlag = true
        }
      })
      // 整列
      _this.spread.bind(GC.Spread.Sheets.Events.ColumnChanged, function (s, e) {
        console.log('ColumnChanged')
        console.log(s)
        console.log(e)
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
        console.log('ActiveSheetChanged')
        console.log(JSON.parse(JSON.stringify(_this.spread)))
        console.log(s)
        console.log(JSON.parse(JSON.stringify(e)))
      })
      //修改sheet名称
      _this.spread.bind(GC.Spread.Sheets.Events.SheetNameChanged, function (
        s,
        e
      ) {
        console.log('SheetNameChanged')
        console.log(s)
        console.log(e)
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
        console.log('ClipboardPasted')
        console.log(s)
        console.log(e)
        _this.webSocket.send(JSON.stringify({ type: s.type, data: e }))
      })



    })
  },
  watch: {
    sheet(newValue, oldVal) {
      console.log(newValue)
    }
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
      save(this.$store.state.excel)
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
        _this.webSocketInit();
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
            console.log('ClipboardPasted')
            console.log(jsonv)

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
