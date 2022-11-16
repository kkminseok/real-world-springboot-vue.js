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
            <button @click = "signup"  class="btn btn-lg btn-primary pull-xs-right">
              Sign up
            </button>
          </form>
        </div>

      </div>
    </div>
  </div>
</template>

<script lang="ts">

import axios from "axios";
import router from "@/router";
import {reactive, ref} from "vue";

export default {
  name: "TheRegister.vue",
  setup(){
    let emailDuplicate = ref(false);
    let usernameDuplicate = ref(false);

    const user = reactive({
      username: "",
      email: "",
      password: "",
    })

    const allHideError = () => {
      emailDuplicate.value = false;
      usernameDuplicate.value = false;
    }

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

    const signup = () => {
      axios.post('http://localhost:8080/api/users',{
        user
      })
          .then(response => {
            window.localStorage.setItem("token",response.data.user.token);
            allHideError();
            router.push("/");
          })
          .catch(error =>{
            console.log(error);
            const code = error.response.data.errors.code;
            if(code == "DUPLICATE_EMAIL_USERNAME"){
              showEmailUsernameError();
            }else if(code == "DUPLICATE_EMAIL"){
              showEmailError();
            }else if(code == "DUPLICATE_USERNAME"){
              showUsernameError();
            }
          })
    }

    return { user, emailDuplicate, usernameDuplicate,  signup, showEmailUsernameError,showEmailError, showUsernameError, allHideError }
  },

}
</script>

<style scoped>

</style>