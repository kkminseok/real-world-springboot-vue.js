<template>
  <div v-for = "art in articles.article">
    <div class="article-preview">
      <div class="article-meta">
        <a href="profile.html"><img :src="art.author.image"/></a>
        <div class="info">
          <a class="author"
             href="javascript:void(0)"
             @click="showProfile(art.author.username)">{{art.author.username}}</a>
          <span class="date">{{ art.createdAt }}</span>
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
import axios from "axios";
import router from "@/router";

export default defineComponent({
  name: "ArticleListGlobal",
  props:{
    isEmpty: Boolean,
    isLoading: Boolean
  },
  setup(props,{emit}) {
    const url = import.meta.env.VITE_BASE_URL;

    const articles = reactive({
      article: {
        art: {
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
      },
      articlesCount: "",
    })

    const showProfile = (username: string) => {
      router.push({
        name: 'Profile',
        params: {username: username}
      })
    }

    onMounted(() => {
      axios.get(url + "/api/articles")
          .then(response => {
            articles.article = response.data.articles;
            articles.articlesCount = response.data.articlesCount;
            articles.article = JSON.parse(JSON.stringify(articles.article));
            emit("loading", false);
            if (parseInt(articles.articlesCount) == 0) {
              emit("emptied", true);
            }
          });
    })
    return { articles, showProfile }
  }
})
</script>

<style scoped>

</style>