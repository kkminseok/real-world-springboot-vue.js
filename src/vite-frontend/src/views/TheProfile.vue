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
                <a href="javascript:(0)" class="nav-link"
                   @click="myArticleSelect"
                   :class="{ active : myArticleActive}">My Articles</a>
              </li>
              <li class="nav-item">
                <a href="javascript:(0)" class="nav-link"
                   @click="favoriteArticleSelect"
                   :class="{ active : favoriteArticleActive}">Favorited Articles</a>
              </li>
            </ul>
            <div v-if="listsAreLoading">
              Loading articles...
            </div>
            <div v-if="isEmpty">
              No articles are here... yet.
            </div>
          </div>
            <article-my v-for="(article,index) in articleLists"
                          :key="article.slug"
                          :article="article">
            </article-my>
          <pagination-component v-model="currentPage" :numberOfPages="numberOfPages"></pagination-component>
        </div>
      </div>
    </div>

  </div>
</template>

<script lang="ts">
import { onMounted, reactive, ref } from "vue";
import { useStore } from "vuex";
import { defineComponent } from 'vue';
import pagination from "@/components/PaginationComponent.vue";
import { usePaginationApi } from "@/api/usePaginationAPI"
import router from "@/router";
import { followUser, getProfile, unfollowUser } from "@/api";
import ArticleMy from "@/components/ArticleMy.vue";

export default defineComponent({
  name: "TheProfile.vue",
  components: {
    "article-my": ArticleMy,
    'pagination-component': pagination,
  },
  props:{
    username: String,
  },
  setup(props){
    const store = useStore();
    const isMe = ref(false);
    const profile = reactive({
      image: "",
      username: "",
      bio: "",
      following: false,
    })
    const myArticleActive = ref(true);
    const favoriteArticleActive = ref(false);


    const currentPage = ref(1);
    const rowsPerPage = ref(5);

    const { articleLists, listsAreLoading, isEmpty,loadMyArticles, loadFavoriteArticles, numberOfPages } = usePaginationApi(currentPage, rowsPerPage);

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

    const myArticleSelect = async () => {
      myArticleActive.value=true;
      favoriteArticleActive.value=false;
      await loadMyArticles(profile.username);
    }

    const favoriteArticleSelect = async () => {
      myArticleActive.value=false;
      favoriteArticleActive.value=true;
      await loadFavoriteArticles(profile.username);
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
        await loadMyArticles(profile.username);
      }catch (error: any){
        alert(error);
      }
    })
    return { isMe, listsAreLoading, isEmpty, myArticleActive, favoriteArticleActive, profile, articleLists, currentPage, rowsPerPage, stateUpdate, myArticleSelect, favoriteArticleSelect, numberOfPages }
  }
})
</script>

<style scoped>

</style>