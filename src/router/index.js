import store from '@/store'
import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/404',
    name: '404',
    component: () => import('../views/404.vue')
  },
]

const router = new VueRouter({
  routes
})

// ！注意，刷新页面会导致页面路由重置
export const setRouters = () => {
  const storeMenus = localStorage.getItem("menus");
  if (storeMenus) {
    // 拼装动态路由
    const manageRouter = { path: '/', name: 'Manage', component: () => import('../views/Manage.vue'), redirect: '/home', children: [
      {path:'person', name:"个人信息", component: () => import("../views/Person.vue")}
    ] }
    const menus = JSON.parse(storeMenus)
    menus.forEach(item => {
      if (item.path) {  // 当且仅当path不为空的时候才设置路由
        let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue') }
        manageRouter.children.push(itemMenu)
      } else if (item.children.length) {
        item.children.forEach(item => {
          if (item.path) {
            let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue') }
            manageRouter.children.push(itemMenu)
          }
        })
      }
    })
    // 获取当前的路由对象名称数组
    const currentRouterNames = router.getRoutes().map(v => v.name)
    if (!currentRouterNames.includes('Manage')) {
      // 动态添加到现在的路由对象
      router.addRoute(manageRouter)
    }
  }
}

setRouters()

router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name) // 设置当前路由名称
  store.commit("setPath")

  // 未找到路由的情况
  if (!to.matched.length) {
    const storeMenus = localStorage.getItem("menus")
    if (toreMenus) {
      next("/404")
    } else {
      //跳回登录页面
      next("/login")
    }
  }
  // 其他的情况都放行
  next()
})

export default router
