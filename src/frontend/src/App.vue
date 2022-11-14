<template>
  <TheHeader></TheHeader>
  <TheHome></TheHome>
  <TheFooter></TheFooter>
</template>

<script>
import TheHome from '@/views/TheHome'
import TheHeader from '@/components/TheHeader.vue'
import TheFooter from "@/components/TheFooter";

export default {
  name: 'App',
  components: {
    TheHeader,
    TheHome,
    TheFooter
  },

}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
