<template>
  <div class="article-preview">
    <div class="article-meta">
      <a href="javascript:void(0)" ><img :src="article.author.image"/></a>
      <div class="info">
        <a href="javascript:void(0)" class="author">{{article.author.username}}</a>
        <span class="date">{{convertDate(article.createdAt)}}</span>
      </div>
      <button class="btn btn-outline-primary btn-sm pull-xs-right">
        <i class="ion-heart"></i> {{article.favoritesCount}}
      </button>
    </div>
    <a href="javascript:(0)" class="preview-link"
       @click="showArticle(article.slug)">
      <h1>{{article.title}}</h1>
      <p>{{article.description}}</p>
      <span>Read more...</span>
    </a>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import router from "@/router";
import convertDate from "@/ts/common";
export default defineComponent({
  name: "ArticleMy",
  props:{
    article: {
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

    const showArticle = (slug: string) =>{
      router.push({
        name: 'ArticleDetail',
        params: {slug: slug}
      })
    }

    return { showProfile, convertDate, showArticle }
  }
})
</script>

<style scoped>

</style>