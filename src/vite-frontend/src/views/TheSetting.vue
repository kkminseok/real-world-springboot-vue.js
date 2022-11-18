<template>

  <div class="settings-page">
    <div class="container page">
      <div class="row">

        <div class="col-md-6 offset-md-3 col-xs-12">
          <h1 class="text-xs-center">Your Settings</h1>

          <form>
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
              <button @click = "updateUser" class="btn btn-lg btn-primary pull-xs-right">
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
import axios from "axios";
import router from "@/router";
import { useStore } from "vuex";
import { onMounted, reactive } from "vue";

export default {
  name: "TheSetting",
  setup() {
    const url = import.meta.env.VITE_BASE_URL;
    const token = localStorage.getItem("token");
    const user = reactive({
      bio: "",
      email: "",
      image: "",
      username: "",
      password: "",
    })
    const password = "";
    const store = useStore();

    const getUser = (getuser: { bio: string; email: string; username: string; image: string; }) => {
      user.bio = getuser.bio
      user.email = getuser.email
      user.username = getuser.username
      user.image = getuser.image
    }

    const updateUser = () => {
      axios.put(url+'/api/user',{user},{
        headers:{
          Authorization : "TOKEN " + token,
          "Content-Type": `application/json`,
        }})
          .then(response => {
            store.dispatch("LOGIN",response.data.user);
            router.push({
              name: 'Profile',
              params: {username: response.data.user.username}})

          })
          .catch(error =>{
            const code = error.response.data.errors.code;
            //TODO 예외처리
          })
    }

    const logout = () =>{
      store.dispatch("LOGOUT").then(()=>{
        router.push({name: "Home"});
      })
    }


    onMounted(() => {
      axios.get(url+'/api/user',{
        headers:{
          Authorization : "TOKEN " + token
        }
      })
          .then(response => {
            store.dispatch("LOGIN",response.data.user)
            getUser(response.data.user);
          })
          .catch(error =>{
            const code = error.response.data.errors.code;
            //TODO 예외처리
          })
    })
    return {user, password, url, token, getUser, updateUser, logout};
  }
}
</script>

<style scoped>

</style>