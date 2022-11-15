<template>

  <div class="auth-page">
    <div class="container page">
      <div class="row">

        <div class="col-md-6 offset-md-3 col-xs-12">
          <h1 class="text-xs-center">Sign up</h1>
          <p class="text-xs-center">
            <a href="">Have an account?</a>
          </p>

          <ul class="error-messages" v-if="emailDuplicate">
            <li align="left">email has already been taken</li>
          </ul>
          <ul class="error-messages" v-if="usernameDuplicate">
            <li align="left">username has already been taken</li>
          </ul>


          <form>
            <fieldset class="form-group">
              <input class="form-control form-control-lg" type="text" placeholder="Your Name" id="username">
            </fieldset>
            <fieldset class="form-group">
              <input class="form-control form-control-lg" type="text" placeholder="Email" id="email">
            </fieldset>
            <fieldset class="form-group">
              <input class="form-control form-control-lg" type="password" placeholder="Password" id="password">
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

<script>

import axios from "axios";
import router from "@/router";
import {reactive} from "vue";

export default {
  name: "TheRegister.vue",
  setup(){
    const emailDuplicate = false;
    const usernameDuplicate = false;

    const form = reactive({
      username: "",
      email: "",
      password: "",
    })

    const showEmailUsernameError = () => {
      this.emailDuplicate = true;
      this.usernameDuplicate = true
    };

    const showEmailError = () => {
      this.emailDuplicate = true;
      this.usernameDuplicate = false;
    };
    const showUsernameError = () => {
      this.usernameDuplicate = true;
      this.emailDuplicate = false;
    };

    const signup = () => {
      axios.post('http://3.35.44.58:8080/api/users',{
        form
      })
          .then(response => {
            window.localStorage.setItem("token",response.data.user.token);
            this.emailDuplicate = false;
            this.usernameDuplicate = false;
            router.push("/");
          })
          .catch(error =>{
            console.log(error);
            const code = error.response.data.errors.code;
            if(code == "DUPLICATE_EMAIL_USERNAME"){
              this.showEmailUsernameError();
            }else if(code == "DUPLICATE_EMAIL"){
              this.showEmailError();
            }else if(code == "DUPLICATE_USERNAME"){
              this.showUsernameError();
            }
          })
    }

    return { emailDuplicate, usernameDuplicate, signup, showEmailUsernameError,showEmailError, showUsernameError }
  },

}
</script>

<style scoped>

</style>