 <template>
  <div>
    <!--搜索表单-->
    <el-form :model="deletedExcelListForm" label-width="80px">
      <el-row>
        <el-col :span="6">
          <el-form-item label="文件名">
            <el-input v-model="deletedExcelListForm.name" />
          </el-form-item>
        </el-col>
        <el-col :span="13">
          <el-form-item label="创建日期">
            <el-date-picker
              v-model="deletedExcelListForm.date"
              type="daterange"
              range-separator="至"
              start-placeholde="开始日期"
              end-placeholde="结束日期"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item>
        <el-row>
          <el-col :span="18">
            <el-button @click="searchExcelList(1)">查询</el-button>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>
    <!--数据显示table-->
    <el-table :data="excelList" style="width: 100%">
      <el-table-column prop="name" label="文件名" width="180"></el-table-column>
      <el-table-column prop="createDate" label="创建日期" width="180"></el-table-column>
      <el-table-column prop="lastModifyDate" label="最后修改日期" width="180"></el-table-column>
      <el-table-column prop="fileDescribe" label="文件描述" width="180"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="recoverExcel(scope.row.id)">还原</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="deletedExcelListForm.pageNum"
      :page-sizes="[5,10,15,20]"
      :page-size="deletedExcelListForm.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    ></el-pagination>
  </div>
</template>
<script>
import { list, recover } from '@/api/excel'
export default {
  data () {
    return {
      excelList: [],
      deletedExcelListForm: {
        name: '',
        type: '0',
        date: [],
        pageSize: 5,
        pageNum: 1
      },
      total: 0
    }
  },
  mounted () {
    this.searchExcelList()
  },
  methods: {
    // 恢复excel
    recoverExcel (excelId) {
      recover({ 'excelId': excelId }).then(response => {
        alert(response.data.msg)
        this.searchExcelList(1)
      })
      console.log(excelId)
    },
    // 页显示个数变化
    handleSizeChange (val) {
      console.log(this.deletedExcelListForm)
      this.deletedExcelListForm.pageSize = val
      // 改变页显示个数时默认搜索第一页
      this.searchExcelList(1)
    },
    // 当前页变化
    handleCurrentChange (val) {
      console.log(this.deletedExcelListForm)
      this.deletedExcelListForm.pageNum = val
      this.searchExcelList()
    },

    searchExcelList (e) {
      if (e) {
        this.deletedExcelListForm.pageNum = 1
      }

      list(this.deletedExcelListForm)
        .then(response => {
          if (response.data.code === 200) {
            this.excelList = response.data.data.list
            this.total = response.data.data.total
          } else {
            alert(response.data.msg)
          }
        })
    }
  }
}
</script>
