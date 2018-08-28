<template>
  <div id="app">
    <index></index>
  </div>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'
import Index from '@/views/Index.vue'
export default {
  name: 'app',
  components: {
    HelloWorld,
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
