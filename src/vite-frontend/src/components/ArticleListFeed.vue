<template>
  <div v-for = "art in articles.article">
    <div class="article-preview">
      <div class="article-meta">
        <a href="profile.html"><img :src="art.author.image"/></a>
        <div class="info">
          <a href="" class="author">{{art.author.username}}</a>
          <span class="date">{{convertDate(art.createdAt)}}</span>
        </div>
        <button class="btn btn-outline-primary btn-sm pull-xs-right">
          <i class="ion-heart"></i> {{art.favoritesCount}}
        </button>
      </div>
      <a href="" class="preview-link">
        <h1>{{art.title}}</h1>
        <p>{{art.description}}</p>
        <span>Read more...</span>
      </a>
    </div>
  </div>
</template>

<script lang="ts">
import {onMounted, reactive, ref, defineComponent} from "vue";
import {useStore} from "vuex";
import axios from "axios";
import convertDate from "@/ts/common";
import {feedArticle} from "@/api";

export default defineComponent({
  name: "ArticleListFeed",
  props:{
    isEmpty: Boolean,
    isLoading: Boolean
  },
  setup(props,{ emit }){
    const articles = reactive({
      article:[
        {
          slug: "",
          title: "",
          description: "",
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

    onMounted(async () => {
      try {
        const { data } = await feedArticle();
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
    return { articles, convertDate }

  }
})
</script>

<style scoped>

</style>