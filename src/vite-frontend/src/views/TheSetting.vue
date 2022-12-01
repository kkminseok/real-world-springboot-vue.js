<template>

  <div class="settings-page">
    <div class="container page">
      <div class="row">

        <div class="col-md-6 offset-md-3 col-xs-12">
          <h1 class="text-xs-center">Your Settings</h1>

          <form>
            <ul class="error-messages" v-if="emailDuplicate">
              <li align="left">email has already been taken</li>
            </ul>
            <ul class="error-messages" v-if="usernameDuplicate">
              <li align="left">username has already been taken</li>
            </ul>

            <fieldset>
              <fieldset class="form-group">
                <input class="form-control" type="text" placeholder="URL of profile picture" v-model="user.image">
              </fieldset>
              <fieldset class="form-group">
                <input class="form-control form-control-lg" type="text" placeholder="Your Name" v-model="user.username">
              </fieldset>
              <fieldset class="form-group">
                      <textarea class="form-control form-control-lg" rows="8" placeholder="Short bio about you" v-model="user.bio">
                      </textarea>
              </fieldset>
              <fieldset class="form-group">
                <input class="form-control form-control-lg" type="text" placeholder="Email" v-model="user.email">
              </fieldset>
              <fieldset class="form-group">
                <input class="form-control form-control-lg" type="password" placeholder="Password" v-model="password">
              </fieldset>
              <button @click="setUser" class="btn btn-lg btn-primary pull-xs-right">
                Update Settings
              </button>
            </fieldset>
          </form>
          <hr>
          <button class="btn btn-outline-danger" @click="logout">
            Or click here to logout.
          </button>
        </div>

      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { getCurrentUser, updateUser } from "@/api";
import { useStore } from "vuex";
import router from "@/router";
import {onMounted, reactive, ref} from "vue";

export default {
  name: "TheSetting",
  setup() {
    const url = import.meta.env.VITE_BASE_URL;
    const user = reactive({
      bio: "",
      email: "",
      image: "",
      username: "",
      password: "",
    })
    const password = "";
    const store = useStore();

    const emailDuplicate = ref(false);
    const usernameDuplicate = ref(false);

    const getUser = async (getuser: { bio: string; email: string; username: string; image: string; }) => {
      user.bio = getuser.bio
      user.email = getuser.email
      user.username = getuser.username
      user.image = getuser.image
    }

    const showEmailError = () => {
      emailDuplicate.value = true;
      usernameDuplicate.value = false;
    };
    const showUsernameError = () => {
      usernameDuplicate.value = true;
      emailDuplicate.value = false;
    };

    const setUser = async ()  => {
      try{
        const { data } = await updateUser(user);
        await store.dispatch("LOGIN", data.user);
        await router.push({
          name: 'Profile',
          params: {username: data.user.username}
        })
      }catch(error: any){
        const code = error.response.data.errors.code;
        if(code == "DUPLICATE_EMAIL"){
          showEmailError();
        }else if(code == "DUPLICATE_USERNAME"){
          showUsernameError();
        }
      }
    }

    const logout = async () =>{
      store.dispatch("LOGOUT").then(()=>{
        router.push({name: "Home"});
      })
    }


    onMounted(async () => {
      try {
        const { data } = await getCurrentUser();
        await store.dispatch("LOGIN", data.user)
        getUser(data.user);
      } catch (error: any) {
        const code = error.response.data.errors.code;
        if(code == "EMAIL_NULL_OR_INVALID"){
          await router.push({name:"home"});
        }
      }
    })

    return {user, password, url, emailDuplicate, usernameDuplicate, getUser, setUser, logout};
  }
}
</script>

<style scoped>

</style>