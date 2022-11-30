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
import { onMounted, reactive } from "vue";

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

    const getUser = (getuser: { bio: string; email: string; username: string; image: string; }) => {
      user.bio = getuser.bio
      user.email = getuser.email
      user.username = getuser.username
      user.image = getuser.image
    }

    const setUser = async ()  => {
      try{
        console.log(await updateUser(user));
        /*
        const { data } = await updateUser(user);
        store.dispatch("LOGIN", data.user);
        router.push({
          name: 'Profile',
          params: {username: data.user.username}
        })*/
      }catch(error:any){
        // Todo Error처리
        const code = error.response.data.errors.code;
        console.log(code);
      }
    }

    const logout = () =>{
      store.dispatch("LOGOUT").then(()=>{
        router.push({name: "Home"});
      })
    }


    onMounted(async () => {
      try {
        const { data } = await getCurrentUser();
        store.dispatch("LOGIN", data.user)
        getUser(data.user);
      } catch (error: any) {
        //TODO error처리
        console.log(error);
      }
    })

    return {user, password, url, getUser, setUser, logout};
  }
}
</script>

<style scoped>

</style>