import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from "@/store";
import './assets/global.css'
import request from "@/utils/request";

Vue.config.productionTip = false

//导入ElementUI
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
Vue.use(ElementUI,{size:"mini"});

Vue.prototype.request = request

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
