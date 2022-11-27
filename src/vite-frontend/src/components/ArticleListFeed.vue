<template>
  <div v-for = "art in articles.article">
    <div class="article-preview">
      <div class="article-meta">
        <a href="profile.html"><img :src="art.author.image"/></a>
        <div class="info">
          <a href="" class="author">{{art.author.username}}</a>
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
import {useStore} from "vuex";
import axios from "axios";

export default defineComponent({
  name: "ArticleListFeed",
  props:{
    isEmpty: Boolean,
    isLoading: Boolean
  },
  setup(props,{emit}){
    const url = import.meta.env.VITE_BASE_URL;
    const store = useStore();
    const token = store.state.token;

    const articles = reactive({
      article: {art:{
          slug: String,
          title: String,
          description: String,
          favoritesCount: Number,
          createdAt: String,
          author: {
            username: String,
            image: String
          }
        }},
      articlesCount: "",
    })

    onMounted(() => {

      axios.get(url + "/api/articles/feed",{
        headers:{
          Authorization : "TOKEN " + token
        }})
          .then(response => {
            articles.article = response.data.articles;
            articles.articlesCount = response.data.articlesCount;
            articles.article = JSON.parse(JSON.stringify(articles.article));
            emit("loading",false);
            if(parseInt(articles.articlesCount) == 0) {
              emit("emptied",true);
            }
          });
    })
    return { articles }

  }
})
</script>

<style scoped>

</style>