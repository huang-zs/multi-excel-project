import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    // 存储token
    SecurtToken: localStorage.getItem('SecurtToken') ? localStorage.getItem('SecurtToken') : '',
    ExcelUser: localStorage.getItem('ExcelUser') ? localStorage.getItem('ExcelUser') : {},
    Excel: {}
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
    saveExcel (state, excel) {
      state.Excel = excel
    },
    updateExcel (state, json) {
      state.Excel.json = json
    }
  }

})

export default store
