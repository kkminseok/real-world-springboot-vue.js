<template>
  <div class="card">
    <div class="card-block">
      <p class="card-text">{{comment.body}}</p>
    </div>
    <div class="card-footer">
      <a href="javascript:(0)" class="comment-author" @click="viewProfile">
        <img :src="comment.author.image" class="comment-author-img"/>
      </a>
      <a href="javascript:(0)" class="comment-author" @click="viewProfile">{{comment.author.username}}</a>
      <span class="date-posted">{{convertDate(comment.updatedAt)}}</span>
      <span v-if="isMe" class="mod-options" @click="deleteComment">
        <i class="ion-trash-a"></i>
      </span>
    </div>
  </div>
</template>

<script lang="ts">
import {defineComponent, defineEmits, onMounted, ref,} from "vue";
import convertDate from "@/ts/common";
import router from "@/router";

import {useStore} from "vuex";
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
  setup(props, {emit}){
    const store = useStore();

    const isMe = ref(false);

    const viewProfile = () => {
      router.push({
        name: 'Profile',
        params: {username: props.comment.author.username}})
    }

    const deleteComment = () => {
      emit('delete:comment', props.comment.id);
    }

    onMounted(async () => {
      if(store.state.username == props.comment.author.username){
        isMe.value = true;
      }else{
        isMe.value = false;
      }
    })

    return { isMe, convertDate, viewProfile, deleteComment}
  }

})
</script>

<style scoped>

</style>