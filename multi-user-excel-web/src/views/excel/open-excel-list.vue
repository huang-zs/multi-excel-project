 <template>
  <div>
    <!--搜索表单-->
    <el-form :model="openExcelListForm" label-width="80px">
      <el-row>
        <el-col :span="6">
          <el-form-item label="文件名">
            <el-input v-model="openExcelListForm.name" />
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="文件类型">
            <el-select v-model="openExcelListForm.type" placeholder>
              <el-option label="我创建的" value="1"></el-option>
              <el-option label="我加入的" value="2"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="13">
          <el-form-item label="创建日期">
            <el-date-picker
              v-model="openExcelListForm.date"
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
            <el-button plain type="info" @click="searchExcelList(1)">查询</el-button>
            <el-button plain type="primary" @click="openExcel()" :disabled="controlFlag">打开</el-button>
            <el-button
              plain
              type="warning"
              @click="openUpdateExcelInfoDialog()"
              :disabled="controlFlag"
            >修改</el-button>
            <el-button plain type="danger" @click="deleteExcel()" :disabled="controlFlag">删除</el-button>
          </el-col>
          <el-col :span="6" style="text-align:center;">
            <el-button @click="openExcelByCode">邀请码打开</el-button>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>
    <!--数据显示table-->
    <el-table
      :data="excelList"
      style="width: 100%"
      highlight-current-row
      @current-change="rowChange"
    >
      <el-table-column prop="name" label="文件名" width="200"></el-table-column>
      <el-table-column prop="createDate" label="创建日期" width="180"></el-table-column>
      <el-table-column prop="lastModifyDate" label="最后修改日期" width="180"></el-table-column>
      <el-table-column prop="excelDescribe" label="文件描述"></el-table-column>
      <!-- <el-table-column label="操作">
        <template slot-scope="scope">
          <router-link :to="{path:'/excel',query:{excelId:scope.row.id}}">
            <el-button size="mini">打开</el-button>
          </router-link>
          <el-button size="mini" type="warning" @click="openUpdateExcelInfoDialog(scope.row)">修改</el-button>
          <el-button size="mini" type="danger" @click="deleteExcel(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>-->
    </el-table>
    <!-- 打开修改excel的dialog -->
    <update-excel-dialog
      :excel="excelInfo"
      :dialogVisible="updateExcelDialogFlag"
      @dialogVisibleClose="updateExcelDialogFlag=$event"
    ></update-excel-dialog>
    <!-- 分页 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="openExcelListForm.pageNum"
      :page-sizes="[5,10,15,20]"
      :page-size="openExcelListForm.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    ></el-pagination>
  </div>
</template>
<script>
import { list, checkAndBindExcel, remove } from '@/api/excel'
import updateExcelDialog from '@/components/dialogs/update-excel-dialog'
export default {
  components: {
    updateExcelDialog
  },
  data() {
    return {
      //excel是否被选中
      controlFlag: true,
      //修改excel的dialog是否可见
      updateExcelDialogFlag: false,
      excelList: [],
      copyUrl: 'copyUrl',
      openExcelListForm: {
        name: '',
        type: '1',
        date: [],
        pageSize: 5,
        pageNum: 1
      },
      excelInfo: {},
      total: 0
    }
  },
  watch: {
    excelInfo: {
      handler(newVal, oldVal) {
        //有选中,就可以对excel操作
        this.controlFlag = Object.keys(newVal).length == 0 ? true : false
      },
      deep: true
    }
  },
  mounted() {
    this.searchExcelList()
  },
  methods: {
    openExcel() {
      console.log('打开excel')
      console.log(this.excelInfo)
      this.$router.push({
        path: '/excel',
        query: { excelId: this.excelInfo.id }
      })
    },
    //选中的行发生变化
    rowChange(val) {
      //当前选中excel赋值
      this.excelInfo = val ? val : {}
    },
    //打开修改excel信息的dialog
    openUpdateExcelInfoDialog() {
      this.updateExcelDialogFlag = true
    },
    // 删除excel
    deleteExcel() {
      this.$confirm('此操作将删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        remove({ 'excelId': this.excelInfo.id }).then(response => {
          this.searchExcelList(1)
          this.$message({
            type: 'success',
            message: '删除成功'
          })
        })

      })
    },
    // 页显示个数变化
    handleSizeChange(val) {
      console.log(this.openExcelListForm)
      this.openExcelListForm.pageSize = val
      // 改变页显示个数时默认搜索第一页
      this.searchExcelList(1)
    },
    // 当前页变化
    handleCurrentChange(val) {
      console.log(this.openExcelListForm)
      this.openExcelListForm.pageNum = val
      this.searchExcelList()
    },

    searchExcelList(e) {
      if (e) {
        this.openExcelListForm.pageNum = 1
      }

      list(this.openExcelListForm)
        .then(response => {
          if (response.data.code === 200) {
            this.excelList = response.data.data.list
            this.total = response.data.data.total
          } else {
            alert(response.data.msg)
          }
        })
    },
    // 使用验证码打开
    openExcelByCode() {
      this.$prompt('请输入邀请码', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        checkAndBindExcel({ 'excelId': value })
          .then(response => {
            if (response.data.code === 200) {
              window.console.log('打开' + value)
              this.$router.push({
                path: '/excel',
                query: {
                  excelId: value
                }
              })
            } else {
              alert(response.data.msg)
            }
          }).catch(error => {
            window.console.log(error)
            this.$router.push({
              path: '/error'
            })
          })
      }).catch(error => {
        console.log('取消输入')

      })


    }
  }
}
</script>
