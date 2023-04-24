<!--侧边栏-->
<template>
  <div style="height: 100%;">
    <el-menu :default-openeds="opens" style="min-height: 100vh" background-color="rgb(48,65,86)" text-color="#fff"
      active-text-color="#ffd04b" :collapse-transition="false" :collapse="isCollapse" router>
      <div style="height: 60px;line-height: 60px; text-align: center">
        <img src="../assets/logo.png" alt="" style="width: 20px; position: relative; top: 5px; margin-right: 5px">
        <b style="color: white" v-show="logoTextShow">后台管理系统</b>
      </div>

      <div v-for="item in menus" :key="item.id">
        <div v-if="item.path">
          <el-menu-item :index="item.path">
            <i :class="item.icon"></i>
            <span slot="title">{{ item.name }}</span>
          </el-menu-item>
        </div>

        <div v-else>
          <el-submenu :index="item.id + ''">
            <template slot="title">
              <i :class="item.icon"></i>
              <span slot="title" v-show="logoTextShow">{{ item.name }}</span>
            </template>

            <div v-for="subItem in item.children" :key="subItem.id">
              <el-menu-item :index="subItem.path">
                <template slot="title">
                  <i :class="subItem.icon"></i>
                  <span slot="title">{{ subItem.name }}</span>
                </template>
              </el-menu-item>
            </div>

          </el-submenu>
        </div>
      </div>
    </el-menu>
  </div>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Aside",
  props: {
    isCollapse: Boolean,
    logoTextShow: Boolean,
  },
  data() {
    return {
      menus: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")) : [],
      opens: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")).map(v => v.id + '') : []
    }
  },
}
</script>

<style scoped></style>
