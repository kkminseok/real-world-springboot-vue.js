<template>

  <div class="article-page">

    <div class="banner">
      <div class="container">
        <h1>{{ articleDetail.article.title }}</h1>

        <div class="article-meta">
          <div class="info">
            <a href="javascript:void(0)" class="author" @click="viewProfile">{{ articleDetail.article.author.username }}</a>
            <span class="date">{{convertDate(articleDetail.article.createdAt)}}</span>
          </div>
          <button class="btn btn-sm btn-outline-secondary" @click="followUpdate(articleDetail.article.author.following)">
            <div v-if="articleDetail.article.author.following">
              <i class="ion-minus-round"></i>
              unFollow {{articleDetail.article.author.username}}
            </div>
            <div v-else>
              <i class="ion-plus-round"></i>
              Follow {{articleDetail.article.author.username}}
            </div>
          </button>
          &nbsp;&nbsp;
          <button class="btn btn-sm btn-outline-primary" @click="favoriteUpdate()">
            <div v-if="articleDetail.article.favorited">
              <i class="ion-heart"></i>
              unFavorite Article (<span class="counter">{{articleDetail.article.favoritesCount}}</span>)
            </div>
            <div v-else>
              <i class="ion-heart"></i>
              Favorite Article (<span class="counter">{{articleDetail.article.favoritesCount}}</span>)
            </div>
          </button>
        </div>

      </div>
    </div>

    <div class="container page">

      <div class="row article-content">
        <div class="col-md-12">
          {{articleDetail.article.body}}
        </div>
      </div>

      <hr/>

      <div class="article-actions">
        <div class="article-meta">
          <a href="javascript:void(0)"><img :src="articleDetail.article.author.image"></a>
          <div class="info">
            <a href="javascript:void(0)" class="author" @click="viewProfile">{{ articleDetail.article.author.username }}</a>
            <span class="date">{{convertDate(articleDetail.article.createdAt)}}</span>
          </div>

          <button class="btn btn-sm btn-outline-secondary" @click="followUpdate(articleDetail.article.author.following)">
            <div v-if="articleDetail.article.author.following">
              <i class="ion-minus-round"></i>
              unFollow {{articleDetail.article.author.username}}
            </div>
            <div v-else>
              <i class="ion-plus-round"></i>
              Follow {{articleDetail.article.author.username}}
            </div>
          </button>
          &nbsp;
          <button class="btn btn-sm btn-outline-primary" @click="favoriteUpdate()">
            <div v-if="articleDetail.article.favorited">
              <i class="ion-heart"></i>
              unFavorite Article (<span class="counter">{{articleDetail.article.favoritesCount}}</span>)
            </div>
            &nbsp;<div v-else>
              <i class="ion-heart"></i>
              Favorite Article (<span class="counter">{{articleDetail.article.favoritesCount}}</span>)
            </div>
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
import router from "@/router";
import {useStore} from "vuex";
import convertDate from "@/ts/common";

export default defineComponent({
  name: "TheArticleDetail.vue",
  props:{
    slug: String,
  },
  setup(props){
    const url = import.meta.env.VITE_BASE_URL;
    const store = useStore();
    const token = store.state.token;

    const articleDetail = reactive({
      article: {
        slug: "",
        title: "",
        description: "",
        body: "",
        tagList: new Array(),
        createdAt: "",
        updatedAt: "",
        favorited: false,
        favoritesCount: 0,
        author: {
          username: "",
          bio: "",
          image: "",
          following: false
        }
      }
    })

    const viewProfile = () => {
      router.push({
        name: 'Profile',
        params: {username: articleDetail.article.author.username}})
    }

    const followUpdate = (followState : boolean) => {
      if(token == ''){
        router.push({name:"Login"});
      }
      if(followState){
        axios.delete(url + "/api/profiles/" + articleDetail.article.author.username + "/follow",{
          headers:{
            Authorization : "TOKEN " + token,
            "Content-Type": `application/json`,
          }
        }).then(response => {
          articleDetail.article.author.following = response.data.profile.following;
        })
      }else{
        axios.post(url + "/api/profiles/" + articleDetail.article.author.username + "/follow",{},{
          headers:{
            Authorization : "TOKEN " + token,
            "Content-Type": `application/json`,
          }
        }).then(response => {
          articleDetail.article.author.following = response.data.profile.following;
        })
      }
    }

    const favoriteUpdate = () => {
      if(token == ''){
        router.push({name:"Login"});
      }
      const favoriteState: boolean = articleDetail.article.favorited;
      if(favoriteState){
        axios.delete(url + "/api/articles/" + articleDetail.article.slug + "/favorite",{
          headers:{
            Authorization : "TOKEN " + token,
            "Content-Type": `application/json`,
          }
        }).then(response => {
          articleDetail.article.favorited = response.data.article.favorited;
          articleDetail.article.favoritesCount = response.data.article.favoritesCount;
        })
      }else{
        axios.post(url + "/api/articles/" + articleDetail.article.slug + "/favorite",{},{
          headers:{
            Authorization : "TOKEN " + token,
            "Content-Type": `application/json`,
          }
        }).then(response => {
          articleDetail.article.favorited = response.data.article.favorited;
          articleDetail.article.favoritesCount = response.data.article.favoritesCount;
        })
      }
    }


    onMounted(()=>{
      axios.get(url + "/api/articles/" + props.slug,)
          .then(response => {
        articleDetail.article = response.data.article;
      })
    })

    return { articleDetail, convertDate, viewProfile, followUpdate, favoriteUpdate }
  }
})
</script>

<style scoped>

</style>