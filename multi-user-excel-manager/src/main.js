// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import './plugins/element.js'// 引用element
Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
// 防重复提交自定义指令,默认3s
Vue.directive('preventReClick', {
  inserted (el, binding) {
    el.addEventListener('click', () => {
      el.classList.add('is-disabled')
      el.disabled = true
      setTimeout(() => {
        el.disabled = false
        el.classList.remove('is-disabled')
      }, binding.value || 3000)
    })
  }
})
//返回上一页自定义指令
Vue.directive('goBack', {
  inserted (el) {
    el.addEventListener('click', () => {
      router.back()
    })
  }
})
