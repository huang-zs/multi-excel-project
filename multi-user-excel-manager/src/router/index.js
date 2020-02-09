import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/',
      component: () => import('@/views/door')
    }
    
  ]
})
// localstore里面没token就跳登录页面
router.beforeEach((to, from, next) => {
  // window.console.log(to.path)
  if (to.path.startsWith('/user/') || to.path === '/'||to.path.startsWith('/test') ) {
    // window.console.log('请求路径' + to.path)
    next()
  } else {
    // 为什么不去store拿？
    let token = localStorage.getItem('SecurtToken')
    if (token === null || token === '') {
      next('/user/login')
    } else {
      next()
    }
  }
})
export default router
