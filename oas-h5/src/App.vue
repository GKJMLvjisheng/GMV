<template>
  <div id="app">
    <index></index>
  </div>
</template>

<script>
import Index from '@/views/Index.vue'//引用组件
export default {
  name: 'app',
  
  //把引用的组件给申明到 components 里面去
  components: {
    Index
  }
}
</script>

<style>
#app {
  font-family:'Arial',"PingFangSC", Helvetica,sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>
