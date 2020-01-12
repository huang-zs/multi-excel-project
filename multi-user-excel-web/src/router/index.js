import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/',
      component: () => import('@/views/door')
    },
    {
      path: '/user/login',
      component: () => import('@/views/user/login')
    },
    {
      path: '/user/create',
      component: () => import('@/views/user/create')
    },
    {
      path: '/home',
      component: () => import('@/views/home'),
      children: [
        {
          path: 'newExcel',
          component: () => import('@/views/excel/new-excel.vue')
        },
        {
          path: 'openExcel',
          component: () => import('@/views/excel/open-excel.vue')
        },
        {
          path: 'openExcelList',
          component: () => import('@/views/excel/open-excel-list.vue')
        },
        {
          path: 'importExcel',
          component: () => import('@/views/excel/import-excel.vue')
        }
      ]
    },
    {
      path: '/success',
      component: () => import('@/views/common/success')
    },
    {
      path: '/error',
      component: () => import('@/views/common/error')
    }
  ]
})
// localstore里面没token就跳登录页面
router.beforeEach((to, from, next) => {
  window.console.log(to.path)
  if ((to.path.startsWith('/user/')) || (to.path === '/')) {
    window.console.log('请求路径' + to.path)
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
