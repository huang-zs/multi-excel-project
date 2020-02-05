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
      path: '/user/resetPassword',
      component: () => import('@/views/user/reset-password')
    },
    {
      path: '/home/updateInfo',
      component: () => import('@/views/user/update-info.vue')
    },
    {
      path: '/home',
      component: () => import('@/views/home'),
      children: [
        
        {
          path: 'openExcelList',
          component: () => import('@/views/excel/open-excel-list.vue')
        },
        {
          path: 'deletedExcelList',
          component: () => import('@/views/excel/deleted-excel-list.vue')
        },
        {
          path: 'importExcelInfo',
          component: () => import('@/views/excel/import-excel-info.vue')
        },
        {
          path: 'newExcelInfo',
          component: () => import('@/views/excel/new-excel-info.vue')
        }
      ]
    },{
      path: '/excel',
      component: () => import('@/views/excel/excel.vue')
    },
    {
      path: '/success',
      component: () => import('@/views/common/success')
    },
    {
      path: '/error',
      component: () => import('@/views/common/error')
    },
    {
      path: '/test1',
      component: () => import('@/views/test/test1')
    },
    {
      path: '/test2',
      component: () => import('@/views/test/test2')
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
