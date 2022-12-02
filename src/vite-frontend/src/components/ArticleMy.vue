<template>
  <div class="article-preview">
    <div class="article-meta">
      <a href=""><img src="http://i.imgur.com/Qr71crq.jpg"/></a>
      <a href="javascript:(0)" @click="showProfile(art.author.username)"><img :src="art.author.image"/></a>
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
</template>

<script lang="ts">
import {defineComponent} from "vue";
import router from "@/router";
export default defineComponent({
  name: "ArticleMy",
  props:{
    art: {
      type: Object,
      default: () =>{
        return {
          slug: "",
          title: "",
          description: "",
          body: "",
          tagList: new Array(),
          createdAt: "",
          favorited: false,
          favoritesCount: 0,
          author:{
            username:"",
            bio: "",
            image: "",
            following: false,
          }
        }
      }
    }
  },
  setup(props){
    const showProfile = (username: string) => {
      router.push({
        name: 'Profile',
        params: {username: username}
      })
    }

    return { showProfile, }
  }
})
</script>

<style scoped>

</style>