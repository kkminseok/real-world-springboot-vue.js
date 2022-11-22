<template>

  <div class="article-page">

    <div class="banner">
      <div class="container">

        <h1>{{ article.title }}</h1>

        <div class="article-meta">
          <a href=""><img :src="article.author.image"/></a>
          <div class="info">
            <a href="" class="author">{{ article.author.username }}</a>
            <span class="date">{{article.createdAt}}</span>
          </div>
          <button class="btn btn-sm btn-outline-secondary">
            <i class="ion-plus-round"></i>
            &nbsp;
            Follow Eric Simons <span class="counter">10</span>
          </button>
          &nbsp;&nbsp;
          <button class="btn btn-sm btn-outline-primary">
            <i class="ion-heart"></i>
            &nbsp;
            Favorite Post <span class="counter">{{article.favoritesCount}}</span>
          </button>
        </div>

      </div>
    </div>

    <div class="container page">

      <div class="row article-content">
        <div class="col-md-12">
          {{article.body}}
        </div>
      </div>

      <hr/>

      <div class="article-actions">
        <div class="article-meta">
          <a href="profile.html"><img src="http://i.imgur.com/Qr71crq.jpg"/></a>
          <div class="info">
            <a href="" class="author">Eric Simons</a>
            <span class="date">January 20th</span>
          </div>

          <button class="btn btn-sm btn-outline-secondary">
            <i class="ion-plus-round"></i>
            &nbsp;
            Follow Eric Simons
          </button>
          &nbsp;
          <button class="btn btn-sm btn-outline-primary">
            <i class="ion-heart"></i>
            &nbsp;
            Favorite Post <span class="counter">(29)</span>
          </button>
        </div>
      </div>

      <div class="row">

        <div class="col-xs-12 col-md-8 offset-md-2">

          <form class="card comment-form">
            <div class="card-block">
              <textarea class="form-control" placeholder="Write a comment..." rows="3"></textarea>
            </div>
            <div class="card-footer">
              <img src="http://i.imgur.com/Qr71crq.jpg" class="comment-author-img"/>
              <button class="btn btn-sm btn-primary">
                Post Comment
              </button>
            </div>
          </form>

          <div class="card">
            <div class="card-block">
              <p class="card-text">With supporting text below as a natural lead-in to additional content.</p>
            </div>
            <div class="card-footer">
              <a href="" class="comment-author">
                <img src="http://i.imgur.com/Qr71crq.jpg" class="comment-author-img"/>
              </a>
              &nbsp;
              <a href="" class="comment-author">Jacob Schmidt</a>
              <span class="date-posted">Dec 29th</span>
            </div>
          </div>

          <div class="card">
            <div class="card-block">
              <p class="card-text">With supporting text below as a natural lead-in to additional content.</p>
            </div>
            <div class="card-footer">
              <a href="" class="comment-author">
                <img src="http://i.imgur.com/Qr71crq.jpg" class="comment-author-img"/>
              </a>
              &nbsp;
              <a href="" class="comment-author">Jacob Schmidt</a>
              <span class="date-posted">Dec 29th</span>
              <span class="mod-options">
              <i class="ion-edit"></i>
              <i class="ion-trash-a"></i>
            </span>
            </div>
          </div>

        </div>

      </div>

    </div>

  </div>
</template>

<script lang="ts">
import {onMounted, defineComponent, reactive} from "vue";
import axios from "axios";
import {useStore} from "vuex";

export default defineComponent({
  name: "TheArticleDetail.vue",
  props:{
    slug: String,
  },
  setup(props){
    const url = import.meta.env.VITE_BASE_URL;
    const store = useStore();
    const token = store.state.token;
    const article = reactive({
       slug: "",
       title: "",
       description: "",
       body: "",
       tagList: new Array(),
       createdAt: "",
       updatedAt: "",
       favorited: Boolean,
       favoritesCount: 0,
       author: {
         username: "",
         bio: "",
         image: "",
         following: Boolean
      }
    })

    const setArticle = ( data: any ) => {
      article.slug = data.slug;
      article.title = data.title;
      article.description = data.description;
      article.body = data.body;
    }

    onMounted(()=>{
      axios.get(url + "/api/articles/" + props.slug,{
        headers:{
          Authorization : "TOKEN " + token,
        }
      }).then(response => {
          setArticle(response.data.article);
      })
    })

    return { article, }
  }
})
</script>

<style scoped>

</style>