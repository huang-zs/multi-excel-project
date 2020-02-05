<template>
  <div>
    <h1>{{a}}</h1>
    <h1>{{b}}</h1>
    <h1>{{ab}}</h1>
    <button @click="b++">click</button>
    <h1>{{c}}</h1>
    <button @click="(c.c1)++">click</button>
    <button @click="testarray">test array</button>
    <get-email-code-button :email="c.c2" :disableFlag="flag"></get-email-code-button>
    <el-button @click="flag=!flag">click</el-button>
    <el-button :disabled="flag">click</el-button>

    <el-input @blur="check" v-model="inputData"></el-input>
    <el-input type="password" v-model="inputData"></el-input>
    {{inputData}}
    <email-input v-model="inputData1"></email-input>
    <el-input :disabled="flag"></el-input>

    <!-- <el-input type="text" :value="inputData1" @input="inputData1 = $event.target.value" /> -->
    {{inputData1}}
    <el-button type @click="guid">guid</el-button>
    <div id="abc">abc</div>

    <button @click="updateExcelDialogFlag=!updateExcelDialogFlag">{{updateExcelDialogFlag}}</button>
    <update-excel-dialog
      :excel="excel"
      :dialogVisible="updateExcelDialogFlag"
      @dialogVisibleClose="updateExcelDialogFlag=$event"
    ></update-excel-dialog>
    {{myTest}}
    <button @click="saveTest">saveTest</button>
    <button @click="deleteTest">deleteTest</button>
  </div>
</template>
<script>
import { emailVaildCheckm, guid } from '@/api/utils'
import getEmailCodeButton from '@/components/utils/get-email-code-button'
import emailInput from '@/components/inputs/email-input'
import updateExcelDialog from '@/components/dialogs/update-excel-dialog'
// 测试watch和computed
export default {
  name: 'test',
  components: {
    getEmailCodeButton, emailInput, updateExcelDialog
  },
  computed: {
    myTest() {
      return this.$store.state.test

    },
    ab() {
      return this.a + this.b
    }
  },
  data() {
    return {
      updateExcelDialogFlag: false,
      excel: {
        name: 'name',
        excelDescribe: 'desc'
      },
      flag: true,
      timer: null,
      a: 'av',
      b: 1,
      c: {
        c1: 0,
        c2: 'c2'
      },
      inputData: '',
      inputData1: '',
      myArray: ['12', '31', '3'],
      myArrayMap: {}
    }
  },
  beforeDestroy() {
    clearInterval(this.timer)
  },
  methods: {
    saveTest() {
      this.$store.commit('saveTest', { a: '11' })
      console.log(JSON.parse(null))
      console.log(this.$store.state)

    },
    deleteTest() {
      this.$store.commit('deleteTest')
      console.log(this.$store.state)

    },



    guid() {
      // let abc = document.getElementById('s-canvas')
      // console.log(abc)
      // console.log('#' + Math.floor(Math.random() * 0xffffff).toString(16))

      // console.log(guid(16))
      // this.myArray.push('2')
      // this.abc = '2'
      this.myArray.forEach(element => {
        this.$set(this.myArrayMap, element, element + '1')
      })
      console.log(this.myArrayMap)


    },
    check() {
      if (!emailVaildCheck(this.inputData)) {
        this.inputData = ''
        this.$message({
          message: '邮箱格式不对',
          type: 'warn',
          duration: 2000
        })

      }
    },
    testarray() {
      console.log(this.myArray)
      this.myArray.splice(this.myArray.indexOf('31'), 1)
      console.log(this.myArray)

    }
  },
  mounted() {
    this.timer = setInterval(() => {
      // console.log(Date())
      // Message.Message(Date())
      this.$message({
        message: '每30s自动保存',
        type: 'success',
        duration: 1000
      })
    }, 30000)
    var s = 'ValueChanged1'
    switch (s) {
      case 'ValueChanged':
        console.log('ValueChanged')
        break

      default:
        console.log('others')

        break
    }
  },
  watch: {
    c: {
      handler(newVal, oldVal) {
        console.log(oldVal)
        console.log('↓')
        console.log(newVal)
        console.log(this.c)
      },
      deep: true
    },
    myArray: {
      handler(oldVal, newVal) {

        console.log(newVal)
        console.log('myArray')
        console.log(oldVal)
      },
      deep: true
    },
    abc: {
      handler(oldVal, newVal) {
        console.log(oldVal)
        console.log('abc')
        console.log(newVal)

      }
    }


  }
}
</script>
