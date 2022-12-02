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
                Edit Profile Settings
              </div>
              <div v-else>
                <div v-if="profile.following">
                  <i class="ion-minus-round"></i>
                  unFollow {{profile.username}}
                </div>
                <div v-else>
                  <i class="ion-plus-round"></i>
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
                <a class="nav-link active" href="javascript">My Articles</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="">Favorited Articles</a>
              </li>
            </ul>
          </div>

          <article-my v-for="(art,index) in articles.article"
                        :article="art">
          </article-my>
        </div>

      </div>
    </div>

  </div>
</template>

<script lang="ts">
import { onMounted, reactive, ref } from "vue";
import { useStore } from "vuex";
import { defineComponent } from 'vue';
import router from "@/router";
import { followUser, getProfile, listArticlesByUsername, unfollowUser } from "@/api";
import ArticleMy from "@/components/ArticleMy.vue";

export default defineComponent({
  name: "TheProfile.vue",
  components: {
    "article-my": ArticleMy,
  },
  props:{
    username: String,
  },
  setup(props){

    const url = import.meta.env.VITE_BASE_URL;
    const store = useStore();

    const isMe = ref(false);
    const profile = reactive({
      image: "",
      username: "",
      bio: "",
      following: false,
    })

    const articles = reactive({
      article: reactive([{
        slug: "",
        title: "",
        description: "",
        body: "",
        tagList: new Array(),
        createdAt: "",
        updatedAt: "",
        favorited: false,
        favoritesCount: 0,
        author: {
          username: "",
          bio: "",
          image: "",
          following: false
        }
      }])
    })

    const setProfile = async ( data: any ) => {
      profile.image = data.image;
      profile.bio = data.bio;
      profile.following = data.following;
      profile.username = data.username;
    }

    const stateUpdate = async () => {
       if(isMe.value){
         await router.push({name:"Settings"});
       }else{
         if(profile.following){
           try {
             const { data } = await unfollowUser(profile.username);
             await setProfile(data.profile);
           }catch (error: any){
             alert("error");
           }
         }else{
           try {
             const { data } = await followUser(profile.username);
             await setProfile(data.profile);
           }catch (error: any){
             alert("error");
           }
         }
       }
    }


    onMounted(async () =>{
      try {
       const { data } =  await getProfile(props.username)
        await setProfile(data.profile);
        if(data.profile.username.localeCompare(store.state.username) == 0){
          isMe.value = true;
        }else{
          isMe.value = false;
        }
      }catch (error: any){
        const code = error.response.data.errors.code;
        if(code == "USER_NOT_FOUND")
          await router.push({name:"home"});
      }

      try{
        const { data } = await listArticlesByUsername(profile.username);
        articles.article = data.articles;
      }catch (error: any){
        alert(error);
      }
    })
    return { url, isMe, profile, articles, stateUpdate }
  }
})
</script>

<style scoped>

</style>