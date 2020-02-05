<template>
  <div>
    <div class="sample-tutorial" style="width:100%;height:100%" id="spreadDiv"></div>
    <button @click="addRows">addRows</button>
    <button @click="addColumns">addColumns2</button>
    <button @click="setValue">setValue</button>
    <button @click="getTest">getTest</button>
    <button @click="addSheet">addSheet</button>
    <button @click="sheetNameChange">sheetNameChange</button>
    <button @click="setBorder">setBorder</button>
  </div>
</template>
<script>
import GC from '@grapecity/spread-sheets'
export default {
  name: 'test2',
  data() {
    return {
      spread: {},
      sheetsName: []

    }
  },
  methods: {
    setBorder() {
      let cell = this.spread.sheets[this.spread.getSheetIndex('Sheet1')].getCell(2, 2)
      cell.text('abc')
      cell.setBorder(new GC.Spread.Sheets.LineBorder('#' + Math.floor(Math.random() * 0xffffff).toString(16), GC.Spread.Sheets.LineStyle.thick), { all: true })
    },
    sheetNameChange() {
      this.spread.sheets[this.spread.getSheetIndex('Sheet1')].name('TestSheetName')
    },
    addSheet() {
      this.spread.addSheet(this.spread.getSheetCount())
    },
    getTest() {
      console.log(this.spread
        .getSheetFromName('Sheet1')
        .getCell(1, 1).text())
      console.log('getTest')

    },
    addRows() {
      this.spread.getSheetFromName('Sheet1').addRows(2, 1)
      console.log('addrow')

    },
    addColumns() {
      this.spread.getSheetFromName('Sheet1').addColumns(2, 1)
      console.log('addColumns')

    },
    setValue() {
      this.spread
        .getSheetFromName('Sheet1')
        .getCell(2, 1)
        .text('cc	dd↵qq	bb')
      console.log('setValue')
    }
  },
  mounted() {
    this.spread = new GC.Spread.Sheets.Workbook(document.getElementById('spreadDiv'))
    this.sheetsName = Object.keys(JSON.parse(JSON.stringify(this.spread)).sheets)
    // this.spread.options.newTabVisible = false
    // var menuData = this.spread.contextMenu.menuData
    // console.log(menuData)
    var _this = this
    this.spread.bind(GC.Spread.Sheets.Events.ValueChanged, function (e, args) {
      console.log(args)
    })
    this.spread.bind(GC.Spread.Sheets.Events.EditStarting, function (e, args) {
      console.log('startediting')

      console.log(e)

      console.log(args)
    })
    // 切换sheet
    this.spread.bind(GC.Spread.Sheets.Events.ActiveSheetChanged, function (
      sender,
      args
    ) {
      // spread.getSheetIndex(name);
      console.log('ActiveSheetChanged')
      let spread = JSON.parse(JSON.stringify(_this.spread))
      //现在的sheet
      let newSheetsName = Object.keys(spread.sheets)
      //新的sheet
      let oldSheetsName = _this.sheetsName
      console.log(newSheetsName)
      console.log(oldSheetsName)

      if (oldSheetsName.length === newSheetsName.length) {//换sheet
        console.log('换sheet')
      } else {//新增sheet
        if (oldSheetsName.length < newSheetsName.length) {
          _this.$set(args, 'type', 'add')
          let result = newSheetsName.filter(function (v) {
            return oldSheetsName.indexOf(v) === -1
          })
          console.log('增加' + result)
          _this.$set(args, 'sheetName', result[0])
          console.log(args)
        } else {//删除sheet
          _this.$set(args, 'type', 'delete')
          let result = oldSheetsName.filter(function (v) {
            return newSheetsName.indexOf(v) === -1
          })
          console.log('减少' + result)
          _this.$set(args, 'sheetName', result[0])
          console.log(args)
        }
        _this.sheetsName = newSheetsName
      }

    })
    this.spread.bind(GC.Spread.Sheets.Events.RowChanged, function (s, e) {
      console.log('RowChanged')
      console.log(s)
      console.log(e)
    })
    _this.spread.bind(GC.Spread.Sheets.Events.ColumnChanged, function (s, e) {
      console.log('ColumnChanged')
      console.log(s)
      console.log(e)
    })
    _this.spread.bind(GC.Spread.Sheets.Events.ClipboardPasted, function (
      sender,
      args
    ) {
      console.log('ClipboardPasted2')
      console.log(sender)
      console.log(args)
      let sheet = _this.spread
        .getSheetFromName(args.sheetName)
      let rowIndex = args.cellRange.row
      let rowCount = args.cellRange.rowCount
      let colIndex = args.cellRange.col
      let colCount = args.cellRange.colCount
      let dataArray = []
      for (let row = rowIndex, i = 0; row < rowIndex + rowCount; row++ , i++) {
        dataArray[i] = []
        for (let col = colIndex, j = 0; col < colIndex + colCount; col++ , j++) {
          console.log(sheet
            .getCell(row, col).text())
          dataArray[i][j] = sheet
            .getCell(row, col).text()
        }
      }
      _this.$set(args, 'dataArray', dataArray)
      console.log(args)
      sheet.setArray(rowIndex + 5, colIndex + 5, dataArray)
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
  }

}
</script>
<style scoped>
.sample-tutorial {
  position: relative;
  height: 100%;
  overflow: hidden;
}
</style>
