<template>
  <div id="app">
  
    <index></index>
     <router-view v-if="isRouterAlive"></router-view>
  </div>
</template>

<script>
import Index from '@/views/Index.vue'
export default {
  name: 'app',
  //
  provide (){

     return {

       reload:this.reload

     }

  },

  data(){

    return {

       isRouterAlive:true

    }

  },

  methods:{

    reload (){

       this.isRouterAlive = false

       this.$nextTick(function(){

          this.isRouterAlive = true

       })

    }

  },
//
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
