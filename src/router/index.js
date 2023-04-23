import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect:'/login'
  },
  {
    path: '/register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/home',
    redirect:'/home',
    name:'首页',
    component: () => import('../views/Manage.vue'),
    children:[
      {path: '/home', name: '首页', component: () => import('../views/Home.vue')},
      {path: '/user', name: '用户管理', component: () => import('../views/User.vue')},
      {path: '/person', name: '个人信息', component: () => import('../views/Person.vue')},
      {path: '/file', name: '文件管理', component: () => import('../views/File.vue')},
      {path: '/role', name: '角色管理', component: () => import('../views/Role.vue')},
      {path: '/menu', name: '菜单管理', component: () => import('../views/Menu.vue')},
    ]
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/biao',
    component: () => import('../views/echDemo.vue')
  },
]

const router = new VueRouter({
  routes
})

router.beforeEach((to, from, next)=>{
  localStorage.setItem("currentPathName", to.name) // 设置当前路由名称
  next()
})

export default router
