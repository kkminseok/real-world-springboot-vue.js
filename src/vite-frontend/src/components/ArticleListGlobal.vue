<template>
  <div v-for = "art in articles.article">
    <div class="article-preview">
      <div class="article-meta">
        <a href="javascript:(0)" @click="showProfile(art.author.username)"><img :src="art.author.image"/></a>
        <div class="info">
          <a class="author"
             href="javascript:void(0)"
             @click="showProfile(art.author.username)">{{art.author.username}}</a>
          <span class="date">{{convertDate(art.createdAt)}}</span>
        </div>
        <button class="btn btn-outline-primary btn-sm pull-xs-right">
          <i class="ion-heart" @click="changeFavorite(art.slug, art.favorited)"></i> {{art.favoritesCount}}
        </button>
      </div>
      <a href="javascript:(0)"
         class="preview-link"
         @click="showArticle(art.slug)">
        <h1>{{art.title}}</h1>
        <p>{{art.description}}</p>
        <span>Read more...</span>
      </a>
    </div>
  </div>
</template>

<script lang="ts">
import {onMounted, reactive, defineComponent } from "vue";
import router from "@/router";
import convertDate from '@/ts/common';
import {favoriteArticle, listArticles, unFavoriteArticle} from "@/api";

export default defineComponent({
  name: "ArticleListGlobal",
  props:{
    isLoading: Boolean,
    isEmpty: Boolean,
    globalActive: Boolean,
  },
  setup(props,{emit}) {
    const articles = reactive({
      article:[
        {
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
      ],
      articlesCount: ""}
    )

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
      if(favorite){
        await unFavoriteArticle(slug);
      }else{
        await favoriteArticle(slug);
      }
    }


    onMounted(async () => {
      console.log(props.isEmpty,props.globalActive,props.isLoading);
      try {
        const { data } = await listArticles();
        articles.article = data.articles.slice();
        articles.articlesCount = data.articlesCount;
        emit("loading",false);
        if(parseInt(articles.articlesCount) == 0) {
          emit("emptied",true);
        }
      }catch (error: any){
        alert(error);
      }
    })
    return { articles, convertDate, changeFavorite, showProfile, showArticle }
  }
})
</script>

<style scoped>

</style>