import Vue from 'vue'
import App from './App.vue'
import 'lib-flexible' // rem适配方案
import 'normalize.css' // 去除默认样式
import './assets/css/common.css' // 动画css
import 'animate.css'
import Axios from 'axios'
import VueScroller from 'vue-scroller'
//import router from 'vue-router' 
Vue.use(VueScroller)
/*var token = null
if(window.Android) {
  token = window.Android.getToken()
}*/

//Axios.defaults.baseURL = process.env.VUE_APP_BASE_URL
Axios.interceptors.request.use(config => {
  config.headers['Content-type'] = 'application/json;charset=UTF-8'
  //config.headers['dataType']= 'json'
  //config.headers['token'] = token || '99e04e02-fb5c-4f65-a6cf-a93006c351b6'
   /*config.headers = {
    
     'Content-Type': 'application/json;charset=utf-8',
     'token': token || 'de97ef47-0ce5-4af0-985a-6703718d50e9',
   }*/
  return config
})


Vue.prototype.$axios = Axios
Vue.config.productionTip = false
let startApp = function () {
  Axios.get('/static/config.json').then((res) => {
    // 基础地址
    //Vue.prototype.BASE_URL = res.data.BASE_URL;
   console.log(res.data.BASE_URL)
   Axios.defaults.baseURL = res.data.BASE_URL
new Vue({
  //router ,
  render: h => h(App)
}).$mount('#app')
 })
}
startApp()