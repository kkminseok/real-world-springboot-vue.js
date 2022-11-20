<template>
  <!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>Conduit</title>
    <!-- Import Ionicon icons & Google Fonts our Bootstrap theme relies on -->
    <link href="//code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css" rel="stylesheet" type="text/css">
    <link href="//fonts.googleapis.com/css?family=Titillium+Web:700|Source+Serif+Pro:400,700|Merriweather+Sans:400,700|Source+Sans+Pro:400,300,600,700,300italic,400italic,600italic,700italic"
          rel="stylesheet" type="text/css">
    <!-- Import the custom Bootstrap 4 theme from our hosted CDN -->
    <link rel="stylesheet" href="//demo.productionready.io/main.css">
  </head>

  <nav class="navbar navbar-light">
    <div class="container">
      <a class="navbar-brand" href="/">conduit</a>
      <ul class="nav navbar-nav pull-xs-right">
        <li class="nav-item">
          <!-- Add "active" class when you're on that page" -->
          <router-link to="/" class="nav-link active" active-class="active">Home</router-link>
        </li>
        <li class="nav-item" v-if="isLogin">
          <router-link to="/editor/" class="nav-link" active-class="active">New Article</router-link>
        </li>
        <li class="nav-item" v-if="isLogin">
            <router-link to="/settings" class="nav-link" active-class="active"><i class="ion-gear-a"></i>Settings</router-link>
        </li>
        <li class="nav-item" v-if="!isLogin">
          <router-link to="/login" class="nav-link" active-class="active">Sign in</router-link>
        </li>
        <li class="nav-item" v-if="!isLogin">
          <router-link to="/register" class="nav-link" active-class="active">Sign up</router-link>
        </li>
      </ul>
    </div>
  </nav>


</template>

<script lang="ts">
import { onMounted, ref } from "vue";
import { useStore } from "vuex";

export default {
  name: "TheHeader",
  setup(){
    const store = useStore();
    const isLogin = ref(false);

    onMounted(()=> {
        if(store.state.token == ""){
          isLogin.value = false;
        }else{
          isLogin.value = true;
        }
    })

    return { isLogin}
  }

}
</script>

<style scoped>

</style>