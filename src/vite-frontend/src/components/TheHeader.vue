<template>
  <!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>KMS-real-world</title>
    <link
        href="//code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css"
        rel="stylesheet"
        type="text/css"
    />
  </head>

  <nav class="navbar navbar-light">
    <div class="container">
      <a class="navbar-brand" href="/">conduit</a>
      <ul class="nav navbar-nav pull-xs-right">
        <li class="nav-item">
          <!-- Add "active" class when you're on that page" -->
          <router-link to="/" class="nav-link"
                       @click="selected(home)"
                       :class="{ active : home}" active-class="active">Home</router-link>
        </li>
        <li class="nav-item" v-if="isLogin">
          <router-link to="/editor/" class="nav-link"
                       @click="selected(article)"
                       :class="{ active : article}" active-class="active">New Article</router-link>
        </li>
        <li class="nav-item" v-if="isLogin">
            <router-link to="/settings" class="nav-link"
                         @click="selected(setting)"
                         :class="{ active : setting}" active-class="active"><i class="ion-gear-a"></i>Settings</router-link>
        </li>
        <li class="nav-item" v-if="!isLogin">
          <router-link to="/login" class="nav-link"
                       @click="selected(signIn)"
                       :class="{ active : signIn}" active-class="active">Sign in</router-link>
        </li>
        <li class="nav-item" v-if="!isLogin">
          <router-link to="/register" class="nav-link"
                       @click="selected(signUp)"
                       :class="{ active : signUp}" active-class="active">Sign up</router-link>
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
    const home = ref(false);
    const article = ref(false);
    const setting = ref(false);
    const signIn = ref(false);
    const signUp = ref(false);
    const allHide = () => {
      home.value = false;
      article.value = false;
      setting.value = false;
      signUp.value = false;
      signIn.value = false;
    }

    const selected = (headerName : boolean) => {
      allHide();
      headerName = true;
    }

    onMounted(()=> {
        if(store.state.token == ""){
          isLogin.value = false;
        }else{
          isLogin.value = true;
        }
    })

    return { isLogin, home, article, setting, signIn, signUp, allHide, selected }
  }

}
</script>

<style scoped>

</style>