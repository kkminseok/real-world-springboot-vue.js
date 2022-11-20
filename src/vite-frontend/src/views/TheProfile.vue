<template>

  <div class="profile-page">

    <div class="user-info">
      <div class="container">
        <div class="row">

          <div class="col-xs-12 col-md-10 offset-md-1">
            <img :src="profile.image" class="user-img"/>
            <h4>{{ profile.username }}</h4>
            <p>
              {{ profile.bio }}
            </p>
            <button class="btn btn-sm btn-outline-secondary action-btn" @click="stateUpdate">
              <div v-if="isMe">
                <i class="ion-gear-a"></i>
                &nbsp;
                Edit Profile Settings
              </div>
              <div v-else>
                <div v-if="profile.following">
                  <i class="ion-plus-round"></i>
                  &nbsp;
                  unFollow {{profile.username}}
                </div>
                <div v-else>
                  <i class="ion-plus-round"></i>
                  &nbsp;
                  Follow {{profile.username}}
                </div>
              </div>
            </button>
          </div>

        </div>
      </div>
    </div>

    <div class="container">
      <div class="row">

        <div class="col-xs-12 col-md-10 offset-md-1">
          <div class="articles-toggle">
            <ul class="nav nav-pills outline-active">
              <li class="nav-item">
                <a class="nav-link active" href="">My Articles</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="">Favorited Articles</a>
              </li>
            </ul>
          </div>

          <div class="article-preview">
            <div class="article-meta">
              <a href=""><img src="http://i.imgur.com/Qr71crq.jpg"/></a>
              <div class="info">
                <a href="" class="author">Eric Simons</a>
                <span class="date">January 20th</span>
              </div>
              <button class="btn btn-outline-primary btn-sm pull-xs-right">
                <i class="ion-heart"></i> 29
              </button>
            </div>
            <a href="" class="preview-link">
              <h1>How to build webapps that scale</h1>
              <p>This is the description for the post.</p>
              <span>Read more...</span>
            </a>
          </div>

          <div class="article-preview">
            <div class="article-meta">
              <a href=""><img src="http://i.imgur.com/N4VcUeJ.jpg"/></a>
              <div class="info">
                <a href="" class="author">Albert Pai</a>
                <span class="date">January 20th</span>
              </div>
              <button class="btn btn-outline-primary btn-sm pull-xs-right">
                <i class="ion-heart"></i> 32
              </button>
            </div>
            <a href="" class="preview-link">
              <h1>The song you won't ever stop singing. No matter how hard you try.</h1>
              <p>This is the description for the post.</p>
              <span>Read more...</span>
              <ul class="tag-list">
                <li class="tag-default tag-pill tag-outline">Music</li>
                <li class="tag-default tag-pill tag-outline">Song</li>
              </ul>
            </a>
          </div>


        </div>

      </div>
    </div>

  </div>
</template>

<script lang="ts">
import {onMounted, reactive, ref, UnwrapNestedRefs} from "vue";
import { useStore } from "vuex";
import axios from "axios";
import { defineComponent } from 'vue';
import router from "@/router";

export default defineComponent({
  name: "TheProfile.vue",
  props:{
    username: String,
  },
  setup(props){

    const url = import.meta.env.VITE_BASE_URL;
    const store = useStore();
    const token = store.state.token;

    const isMe = ref(false);
    const profile = reactive({
      image: "",
      username: "",
      bio: "",
      following: false,
    })



    const setProfile = ( data: any ) => {
      profile.image = data.image;
      profile.bio = data.bio;
      profile.following = data.following;
      profile.username = data.username;
    }

    const stateUpdate = () => {
       if(isMe.value){
         router.push({name:"Settings"});
       }else{
         if(profile.following){
            axios.delete(url + "/api/profiles/" + profile.username + "/follow",{
              headers:{
                Authorization : "TOKEN " + token,
                "Content-Type": `application/json`,
              }
            }).then(response => {
              setProfile(response.data.profile)
            })
         }else{
           axios.post(url + "/api/profiles/" + profile.username + "/follow",{
             headers:{
               Authorization : "TOKEN " + token,
               "Content-Type": `application/json`,
             }
           }).then(response => {
             setProfile(response.data.profile)
           })
         }
       }
    }


    onMounted(() =>{
        axios.get(url + "/api/profiles/" + props.username)
            .then(response => {
              const getProfile = response.data.profile;
              setProfile(getProfile)
              if(getProfile.username.localeCompare(store.state.username) == 0){
                isMe.value = true;
              }else{
                isMe.value = false;
              }
            })
    })
    return { url, isMe, profile, stateUpdate }
  }
})
</script>

<style scoped>

</style>