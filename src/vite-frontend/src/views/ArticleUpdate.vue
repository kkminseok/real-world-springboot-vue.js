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
                <input type="text" class="form-control" placeholder="Enter tags using ',' Add tag">
                <div class="tag-list"></div>
              </fieldset>
              <button @click="updateContent" class="btn btn-lg pull-xs-right btn-primary" type="button">
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
import { reactive, defineComponent } from "vue";
import { updateArticle } from "@/api/index.js";
import router from "@/router";

export default defineComponent({
  name: "ArticleUpdate",
  props:{
    slug: String,
  },
  setup(props){
    const article = reactive({
      title: "",
      description: "",
      body: "",
    })


    const updateContent = async () => {
      try{
        const { data } = await updateArticle(article, props.slug);
        await router.push({
          name:"ArticleDetail",
          params: {slug: data.article.slug}
        })
      }catch (error: any){
        alert(error);
      }
    }

    return { article, updateContent }
  }
})
</script>

<style scoped>

</style>