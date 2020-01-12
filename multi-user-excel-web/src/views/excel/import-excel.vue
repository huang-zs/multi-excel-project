<template>
  <div>
    <el-container>
      <el-header height="20px">
            <el-button @click="saveExcel">保存</el-button>
      </el-header>
     <el-main>
         <input type="file" @change="importExcel($event)">
        <div class="spreadContainer">
            <gc-spread-sheets :hostClass='"spreadHost"'
            @workbookInitialized='spreadInitHandle($event)'>
        </gc-spread-sheets>
        </div>
    </el-main>
    </el-container>
 </div>
</template>
<script>
import ExcelIO from '@grapecity/spread-excelio'
import FaverSaver from 'file-saver'
export default {
  name: 'new-excel',
  data () {
    return {
      spread: {}

    }
  },

  methods: {
    spreadInitHandle: function (spread) {
      this.spread = spread
    },
    importExcel (e) {
      let ex = new ExcelIO.IO()
      let excelFile = e.target.files[0]
      ex.open(excelFile, function (json) {
        this.spread.fromJSON(json)
      }, function (e) {
        console.log(e)
      })
    },
    exportExcel () {
      let ex = new ExcelIO.IO()
      let json = this.spread.toJSON()
      console.log(json)
      ex.save(json, function (blob) {
        FaverSaver.saveAs(blob, 'exportExcel.xlsx')
      }, function (e) {
        console.log(e)
      })
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
