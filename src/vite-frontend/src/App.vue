<template>
  <TheHeader :key="$route.fullPath"></TheHeader>
  <router-view></router-view>
  <TheFooter></TheFooter>
</template>

<script lang="ts">
import TheHeader from '@/components/TheHeader.vue'
import TheFooter from "@/components/TheFooter.vue";
import { useRoute } from 'vue-router';

export default {
  name: 'App',
  setup(){
    const route = useRoute();
    return route
  },
  components: {
    TheHeader,
    TheFooter
  },

}
</script>

<style>

</style>
