import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    // 存储token
    SecurtToken: localStorage.getItem('SecurtToken') ? localStorage.getItem('SecurtToken') : null,
    ExcelUser: localStorage.getItem('ExcelUser') ? localStorage.getItem('ExcelUser') : null,
    Excel: null,
    test:null
  },
  mutations: {
    saveToken (state, token) {
      state.SecurtToken = token
      localStorage.setItem('SecurtToken', token)
    },
    saveUserInfo (state, data) {
      state.SecurtToken = data.token
      state.ExcelUser = JSON.stringify(data.user)
      localStorage.setItem('SecurtToken', data.token)
      localStorage.setItem('ExcelUser', JSON.stringify(data.user))
    },
    deleteUserInfo(state){
      state.SecurtToken = null
      state.ExcelUser = null
      localStorage.removeItem('SecurtToken')
      localStorage.removeItem('ExcelUser')
    },
    
    saveExcel (state, excel) {
      state.Excel = excel
    },
    updateExcel (state, json) {
      state.Excel.json = json
    },
    saveTest(state,data){
      state.test=data
    },
    deleteTest(state){
      state.test=null
    }
  }

})

export default store
