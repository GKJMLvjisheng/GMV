<template>
  <div id="app">
    <index></index>
  </div>
</template>

<script>
import Index from '@/views/Index.vue'
export default {
  name: 'app',
  components: {
    Index
  }
}
</script>

<style>
body,html {
  width: 100%;
  height: 100%;
}
#app {
  font-family:'Arial',"PingFangSC", Helvetica,sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>
