<template>

  <div class="auth-page">
    <div class="container page">
      <div class="row">

        <div class="col-md-6 offset-md-3 col-xs-12">
          <h1 class="text-xs-center">Sign in</h1>
          <p class="text-xs-center">
            <router-link to="/register">Need an account?</router-link>
          </p>

          <ul class="error-messages" v-if="loginValidation">
            <li align="left">email or password is invalid</li>
          </ul>

          <form>
            <fieldset class="form-group">
              <input class="form-control form-control-lg" type="email" placeholder="Email" v-model="user.email">
            </fieldset>
            <fieldset class="form-group">
              <input class="form-control form-control-lg" type="password" placeholder="Password" v-model="user.password">
            </fieldset>
            <button @click = "Login" class="btn btn-lg btn-primary pull-xs-right">
              Sign in
            </button>
          </form>
        </div>

      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { reactive, ref } from "vue";
import { signIn } from "@/api";
import router from "@/router";
import { useStore } from "vuex";

export default {
  name: "TheLogin",

  setup() {
    const user = reactive({
      email: "",
      password: "",
    })

    const store = useStore();

    let loginValidation = ref(false);

    const Login = async () => {
      try{
        const { data } = await signIn(user);
        store.dispatch("LOGIN",data.user);
        router.push({name:"Home"});
      }catch(error: any){
        const code = error.response.data.errors.code;
        if(code == "EMAIL_NULL_OR_INVALID"){
          loginValidation.value = true;
        }else if(code == "PASSWORD_WRONG"){
          loginValidation.value = true;
        }
      };
    }
    return {user, loginValidation, Login};
  }


}
</script>

<style scoped>

</style>