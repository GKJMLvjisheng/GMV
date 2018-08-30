import Vue from 'vue'
import App from './App.vue'
import 'lib-flexible' // rem适配方案
import 'normalize.css' // 去除默认样式
import './assets/css/common.css' // 动画css
import 'animate.css'
import Axios from 'axios'
var token = null
if(window.Android) {
  token = window.Android.getToken()
  alert(token)
}
console.log(token)
Axios.defaults.baseURL = process.env.VUE_APP_BASE_URL
Axios.interceptors.request.use(config => {
  config.headers['Content-type'] = 'application/json;charset=UTF-8'
  config.headers['token'] = 'de97ef47-0ce5-4af0-985a-6703718d50e9'
  // config.headers = {
  //   'Content-Type': 'application/json;charset=utf-8',
  //   'token': token || 'de97ef47-0ce5-4af0-985a-6703718d50e9',
  // }
  return config
})

Vue.prototype.$axios = Axios
Vue.config.productionTip = false

new Vue({
  render: h => h(App)
}).$mount('#app')
