import Vue from 'vue'
import App from './App.vue'
import 'lib-flexible' // rem适配方案
import 'normalize.css' // 去除默认样式
import './assets/css/common.css' // 动画css
import 'animate.css'
import Axios from 'axios'
var token = sessionStorage.getItem('token')
Axios.defaults.baseURL = process.env.VUE_APP_BASE_URL
Axios.interceptors.request.use(config => {
  // config.headers['Content-type'] = 'application/json;charset=UTF-8'
  // config.headers['token'] = 'b8e3fdbe-8ce5-4688-af6f-c5daac056c5b'
  config.headers = {
    'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8',
    'token': token || '11b63c85-55cf-4aab-9c89-b87572c6c326',
  }
  return config
})

Vue.prototype.$axios = Axios
Vue.config.productionTip = false

new Vue({
  render: h => h(App)
}).$mount('#app')
