 <template>
    <el-table
      :data="excelList"
      style="width: 100%">
      <el-table-column
        prop="name"
        label="文件名"
        width="180">
      </el-table-column>
      <el-table-column
        prop="createDate"
        label="创建日期"
        width="180">
      </el-table-column>
      <el-table-column
        prop="lastModifyDate"
        label="最后修改日期"
        width="180">
      </el-table-column>
      <el-table-column
        label="操作">
        <template slot-scope="scope">
        <router-link :to= "{path:'/home/openExcel',query:{id:scope.row.id}}">
        <el-button
          size="mini"
         >打开</el-button>
        </router-link>
        <el-button size="mini"
          type="danger">删除</el-button>
      </template>
      </el-table-column>
    </el-table>
  </template>
<script>
import {list} from '@/api/excel'
export default {
  data () {
    return {
      excelList: []
    }
  },
  created () {
    list({})
      .then(response => {
        this.excelList = response.data.data
      }).catch(error => {
        window.console.log(error)
        this.$router.push({
          path: '/error'
        })
      })
  }
}
</script>
