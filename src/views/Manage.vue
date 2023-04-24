<template>
  <el-container style="min-height: 100%">
    <el-aside :width="sideWidth + 'px'" style="background-color: rgb(238, 241, 246);
    height: 100%; overflow-x: hidden; box-shadow: 2px 0 6px rgb(0 21 41 / 35%)">
      <!-- 侧边栏 -->
      <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow" :menus="user.menus"/>
    </el-aside>

    <el-container>
      <!--header-->
      <el-header style="font-size: 12px; border-bottom: 1px solid #ccc;">
        <Header :collapseBtnClass="collapseBtnClass" :collapse="collapse" :user="user"/>
      </el-header>

      <el-main>
        <!--表示当前页面的子路由会再 <router-view/> 里面展示-->
        <router-view @refreshUser="getUser"/>
      </el-main>

    </el-container>
  </el-container>
</template>

<style>

</style>

<script>
import Aside from '@/components/Aside';
import Header from "@/components/Header";

export default {
  name: 'HomeView',
  components: {
    Header, Aside
  },
  data() {
    return {
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 203,
      logoTextShow: true,
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  component: {
    Aside,
    Header
  },
  created() {
    // 从后台获取最新的数据
    this.getUser()
  },
  methods:{
    collapse(){ //点击收缩按钮触发
      this.isCollapse = !this.isCollapse
      if(this.isCollapse){
        this.sideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow = false
      }else{ //展开
        this.sideWidth = 203
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
    },

    getUser() {
      let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
      // 从后台获取数据
      this.request.get("/user/username/" + username).then(res => {
        // 重新赋值
        this.user = res.data
      })
    }
  }
}
</script>
