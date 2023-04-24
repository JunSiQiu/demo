<template>
    <div style="line-height: 60px; display: flex">
      <div style="flex: 1">
        <span :class="collapseBtnClass" style="cursor: pointer; font-size: 18px" @click="collapse"></span>

        <el-breadcrumb separator="/" style="display: inline-block; margin-left: 10px">
          <el-breadcrumb-item :to="'/home'">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{ currentPathName }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <el-dropdown style="width: 150px; cursor: pointer">
        <span style="display: flex;">
          <div style="margin-top: 8px; margin-right: 5px;">
            <img :src="user.avatarUrl" width="40" height="40" style="border-radius: 50%;">
          </div>
          <div>
            {{ user.nickname }}
            <i class="el-icon-arrow-down" style="margin-left: 5px"></i>
          </div>
        </span>
        <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
          <el-dropdown-item style="font-size: 14px;">
            <router-link style="text-decoration: none; color: black;" to="/person">个人信息</router-link>
          </el-dropdown-item>
          <el-dropdown-item style="font-size: 14px">
            <span style="text-decoration: none" @click="logout">退出</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
</template>

<script>
import store from '@/store'

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Header",
  props:{
    collapseBtnClass: String,
    collapse:Function,
    user:Object
  },
  watch:{ //监听路由变化
    '$route': function (){
      this.currentPathName = localStorage.getItem("currentPathName") //取出route里面设置的当前路由信息
    }
  },
  data() {
    return {
      currentPathName:'首页',
    }
  },
  methods: {
    logout() {
      store.commit("logout")
      this.$message.success("退出成功")
    }
  }
}
</script>

<style scoped>

</style>
