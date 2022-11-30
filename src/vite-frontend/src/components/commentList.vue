<template>
  <div class="card">
    <div class="card-block">
      <p class="card-text">{{comment.body}}</p>
    </div>
    <div class="card-footer">
      <a href="javascript:(0)" class="comment-author" @click="viewProfile">
        <img :src="comment.author.image" class="comment-author-img"/>
      </a>
      &nbsp;
      <a href="javascript:(0)" class="comment-author" @click="viewProfile">{{comment.author.username}}</a>
      <span class="date-posted">{{convertDate(comment.updatedAt)}}</span>
    </div>
  </div>
</template>

<script lang="ts">
import {defineComponent,} from "vue";
import convertDate from "@/ts/common";
import router from "@/router";
export default defineComponent({
  name: "commentList",
  props:{
    comment: {
      type: Object,
      default: () =>{
        return {
          id: 0,
          body: "",
          updatedAt: "",
          author:{
            username:"",
            image: "",
          }
        }
      }
    }
  },
  setup(props){

    const viewProfile = () => {
      router.push({
        name: 'Profile',
        params: {username: props.comment.author.username}})
    }

    return { convertDate, viewProfile}
  }

})
</script>

<style scoped>

</style>