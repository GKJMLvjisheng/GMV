import Vue from 'vue'
import App from './App.vue'
import 'lib-flexible' // rem适配方案
import 'normalize.css' // 去除默认样式
import './assets/css/common.css' // 动画css
import 'animate.css'
import Axios from 'axios'
var token = sessionStorage.getItem('token')
// Axios.defaults.baseURL='http://18.219.19.160:8080/api/v1/energyPoint' // 生产地址
Axios.defaults.baseURL = '/api' // 开发服务器代理
Axios.interceptors.request.use(config => {
  // config.headers['Content-type'] = 'application/json;charset=UTF-8'
  // config.headers['token'] = 'b8e3fdbe-8ce5-4688-af6f-c5daac056c5b'
  config.headers = {
    'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8',
    'token': token || '129edc65-6532-4b15-87bc-aaaef827ac6e',
  }
  return config
})

Vue.prototype.$axios = Axios
Vue.config.productionTip = false

new Vue({
  render: h => h(App)
}).$mount('#app')
