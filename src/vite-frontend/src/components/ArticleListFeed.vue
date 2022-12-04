<template>
  <div class="article-preview">
    <div class="article-meta">
      <a href="javascript:(0)" @click="showProfile(article.author.username)"><img :src="article.author.image"/></a>
      <div class="info">
        <a class="author"
           href="javascript:void(0)"
           @click="showProfile(article.author.username)">{{article.author.username}}</a>
        <span class="date">{{convertDate(article.createdAt)}}</span>
      </div>
      <button class="btn btn-outline-primary btn-sm pull-xs-right">
        <i class="ion-heart" @click="changeFavorite(article.slug, article.favorited)"></i> {{article.favoritesCount}}
      </button>
    </div>
    <a href="javascript:(0)"
       class="preview-link"
       @click="showArticle(article.slug)">
      <h1>{{article.title}}</h1>
      <p>{{article.description}}</p>
      <span>Read more...</span>
    </a>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";

import convertDate from "@/ts/common";
import { favoriteArticle, unFavoriteArticle } from "@/api";
import router from "@/router";
import {useStore} from "vuex";

export default defineComponent({
  name: "ArticleListFeed",
  props: {
    article: {
      type: Object,
      default: () => {
        return {
          slug: "",
          title: "",
          description: "",
          favorited: false,
          favoritesCount: 0,
          createdAt: "",
          author: {
            username: "",
            image: ""
          }
        }
      }
    }
  },
  setup(props){
    const store = useStore();
    const token = store.state.token;

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

    const changeFavorite = async (slug: string, favorite : boolean) => {
      if(token == ''){
        await router.push({name:"Login"});
        return;
      }else{
        if(favorite){
          const { data } = await unFavoriteArticle(slug);
          props.article.favoritesCount = data.article.favoritesCount;
          props.article.favorited = data.article.favorited;
        }else{
          const { data } = await favoriteArticle(slug);
          props.article.favoritesCount = data.article.favoritesCount;
          props.article.favorited = data.article.favorited;
        }
      }
    }

    return { convertDate, changeFavorite, showProfile, showArticle }

  }
})
</script>

<style scoped>

</style>