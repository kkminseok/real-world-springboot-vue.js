<template>

  <div class="auth-page">
    <div class="container page">
      <div class="row">

        <div class="col-md-6 offset-md-3 col-xs-12">
          <h1 class="text-xs-center">Sign up</h1>
          <p class="text-xs-center">
            <router-link to="/login">Have an account?</router-link>
          </p>

          <ul class="error-messages" v-if="emailDuplicate">
            <li align="left">email has already been taken</li>
          </ul>
          <ul class="error-messages" v-if="usernameDuplicate">
            <li align="left">username has already been taken</li>
          </ul>
          <form>
            <fieldset class="form-group">
              <input class="form-control form-control-lg" type="text" placeholder="Your Name" v-model="user.username">
            </fieldset>
            <fieldset class="form-group">
              <input class="form-control form-control-lg" type="text" placeholder="Email" v-model="user.email">
            </fieldset>
            <fieldset class="form-group">
              <input class="form-control form-control-lg" type="password" placeholder="Password" v-model="user.password">
            </fieldset>
            <button @click = "register"  class="btn btn-lg btn-primary pull-xs-right">
              Sign up
            </button>
          </form>
        </div>

      </div>
    </div>
  </div>
</template>

<script lang="ts">

import { signUp } from "@/api";
import router from "@/router";
import { reactive, ref } from "vue";
import { useStore } from "vuex";

export default {
  name: "TheRegister.vue",
  setup(){
    let emailDuplicate = ref(false);
    let usernameDuplicate = ref(false);

    const store = useStore();
    const user = reactive({
      username: "",
      email: "",
      password: "",
    })

    const showEmailUsernameError = () => {
      emailDuplicate.value = true;
      usernameDuplicate.value = true
    };

    const showEmailError = () => {
      emailDuplicate.value = true;
      usernameDuplicate.value = false;
    };
    const showUsernameError = () => {
      usernameDuplicate.value = true;
      emailDuplicate.value = false;
    };

    const register = async () => {
      try{
        const { data } = await signUp(user);
        store.dispatch("LOGIN",data.user);
        router.push({name:"Home"});
      }catch(error: any){
        const code = error.response.data.errors.code;
        if(code == "DUPLICATE_EMAIL_USERNAME"){
          showEmailUsernameError();
        }else if(code == "DUPLICATE_EMAIL"){
          showEmailError();
        }else if(code == "DUPLICATE_USERNAME"){
          showUsernameError();
        }
      }
    }

    return { user, emailDuplicate, usernameDuplicate,  register, showEmailUsernameError,showEmailError, showUsernameError }
  },

}
</script>

<style scoped>

</style>