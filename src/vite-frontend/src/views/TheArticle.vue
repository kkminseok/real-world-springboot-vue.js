<template>

  <div class="editor-page">
    <div class="container page">
      <div class="row">

        <div class="col-md-10 offset-md-1 col-xs-12">
          <form>
            <fieldset>
              <fieldset class="form-group">
                <input type="text" class="form-control form-control-lg" placeholder="Article Title" v-model="article.title">
              </fieldset>
              <fieldset class="form-group">
                <input type="text" class="form-control" placeholder="What's this article about?" v-model="article.description">
              </fieldset>
              <fieldset class="form-group">
                            <textarea class="form-control" rows="8"
                                      placeholder="Write your article (in markdown)" v-model="article.body"></textarea>
              </fieldset>
              <fieldset class="form-group">
                <input type="text" class="form-control" placeholder="Enter tags using ',' Add tag" v-model="tag">
                <div class="tag-list"></div>
              </fieldset>
              <button @click="addArticle" class="btn btn-lg pull-xs-right btn-primary" type="button">
                Publish Article
              </button>
            </fieldset>
          </form>
        </div>

      </div>
    </div>
  </div>


</template>

<script lang="ts">
import {reactive ,ref } from "vue";
import axios from "axios";
import { useStore } from "vuex";
import router from "@/router";

export default {
  name: "TheArticle",
  setup(){
    const url = import.meta.env.VITE_BASE_URL;
    const store = useStore();
    const token = store.state.token
    const tag = ref("");
    const article = reactive({
      title: "",
      description: "",
      body: "",
      tagList: new Array(),
    })

    const parsingTag = () => {
      let tags: string = tag.value;
      return tags.split(',');
    }

    const getSlug = (title:string) => {
      return title.replace(' ','-');
    }

    const addArticle = () => {
      let tags = parsingTag();
      let slug = getSlug(article.title);
      article.tagList = tags;
      axios.post(url+"/api/articles" ,JSON.stringify({article}) ,{headers:{
          Authorization : "TOKEN " + token,
          "Content-Type": `application/json`,
        }})
      .then(() => {
        router.push({
          name:"ArticleDetail",
          params: {slug}})
      })
    }

    return { article, tag, addArticle, parsingTag, getSlug }
  }
}
</script>

<style scoped>

</style>