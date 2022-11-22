<template>

  <div class="home-page">

    <div class="banner">
      <div class="container">
        <h1 class="logo-font">conduit</h1>
        <p>A place to share your knowledge.</p>
      </div>
    </div>

    <div class="container page">
      <div class="row">

        <div class="col-md-9">
          <div class="feed-toggle">
            <ul class="nav nav-pills outline-active">
              <li class="nav-item">
                <a class="nav-link disabled" href="">Your Feed</a>
              </li>
              <li class="nav-item">
                <a class="nav-link active" href="">Global Feed</a>
              </li>
            </ul>
          </div>
          <div v-for="(article, index) in articles" >
            {{article}} : {{index}}

            <!--
            <div class="article-preview">
              <div class="article-meta">
                <a href="profile.html"><img :src="data.author.image"/></a>
                <div class="info">
                  <a href="" class="author">{{data.author.username}}</a>
                  <span class="date">202202020</span>
                </div>
                <button class="btn btn-outline-primary btn-sm pull-xs-right">
                  <i class="ion-heart"></i> {{data.favoritesCount}}
                </button>
              </div>
              <a href="" class="preview-link">
                {{data.description}}
                <span>Read more...</span>
              </a>
            </div>
            -->

          </div>

        </div>

        <div class="col-md-3">
          <div class="sidebar">
            <p>Popular Tags</p>

            <div class="tag-list">
              <a href="" class="tag-pill tag-default">programming</a>
              <a href="" class="tag-pill tag-default">javascript</a>
              <a href="" class="tag-pill tag-default">emberjs</a>
              <a href="" class="tag-pill tag-default">angularjs</a>
              <a href="" class="tag-pill tag-default">react</a>
              <a href="" class="tag-pill tag-default">mean</a>
              <a href="" class="tag-pill tag-default">node</a>
              <a href="" class="tag-pill tag-default">rails</a>
            </div>
          </div>
        </div>

      </div>
    </div>

  </div>
</template>

<script lang="ts">
import {onMounted, reactive} from "vue";
import axios from "axios";

export default {
  name: "TheHome",
  setup(){
    const url = import.meta.env.VITE_BASE_URL;
    const articles = reactive({
      article:{},
    })

    onMounted(() => {
        axios.get(url + "/api/articles")
            .then(response => {
              console.log(response);
              articles.article = response.data.articles;
              console.log(articles.article);
            });
    })

    return { articles }
  }
}
</script>

<style scoped>

</style>