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
#app {
  font-family:'Arial',"PingFangSC", Helvetica,sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>
