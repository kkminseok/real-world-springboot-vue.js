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
                <li class="nav-item" v-if="isLogin">
                  <a href="javascript:(0)" class="nav-link"
                     @click="feedSelect"
                  :class="{ active : feedActive}">Your Feed</a>
                </li>
                <li class="nav-item">
                  <a href="javascript:(0)" class="nav-link"
                     @click="globalSelect"
                  :class="{ active : globalActive }">Global Feed</a>
                </li>
              </ul>
              <div v-if="listsAreLoading">
                Loading articles...
              </div>
              <div v-if="isEmpty">
                No articles are here... yet.
              </div>
            </div>
            <div v-if="feedActive && isLogin">
              <article-list-feed v-for="(article,index) in articleLists"
                                   :key="article.slug"
                                   :article="article">
              </article-list-feed>
            </div>
            <div v-else>
              <article-list-global v-for="(article,index) in articleLists"
                            :key="article.slug"
                            :article="article">
              </article-list-global>
            </div>
          </div>
        <div class="col-md-3">
          <div class="sidebar">
            <p>Popular Tags</p>
            <tag-lists></tag-lists>
          </div>
        </div>
      </div>
      <pagination-component v-model="currentPage" :numberOfPages="numberOfPages"></pagination-component>
    </div>
  </div>
</template>

<script lang="ts">

import articleList from '@/components/ArticleListFeed.vue'
import articleListGlobal from "@/components/ArticleListGlobal.vue";
import tagLists from "@/components/TagList.vue";
import pagination from "@/components/PaginationComponent.vue";
import { usePaginationApi } from "@/api/usePaginationAPI"
import {onMounted, ref} from "vue";
import { useStore } from "vuex";
export default {
  name: "TheHome",
  components: {
    'article-list-feed': articleList,
    'article-list-global': articleListGlobal,
    'tag-lists': tagLists,
    'pagination-component': pagination,
  },
  setup(){
    const store = useStore();
    const isLogin =  ref(false);
    const feedActive = ref(true);
    const globalActive = ref(false);

    const currentPage = ref(1);
    const rowsPerPage = ref(20);

    const { articleLists, listsAreLoading, isEmpty, loadLists, feedLists, numberOfPages } = usePaginationApi(currentPage, rowsPerPage);

    const feedSelect = async () => {
      feedActive.value=true;
      globalActive.value=false;
      await feedLists();
    }

    const globalSelect = async () => {
      feedActive.value=false;
      globalActive.value=true;
      await loadLists();
    }

    onMounted(async () => {
      isLogin.value = store.state.token ? true : false;
      if(isLogin.value == false) {
        await loadLists();
        globalActive.value = true;
      }else{
        await feedLists();
        feedActive.value = true;
      }
    })

    return { listsAreLoading, isEmpty, isLogin, currentPage, rowsPerPage, numberOfPages, feedActive, globalActive, articleLists, feedSelect, globalSelect };
  }
}
</script>

<style lang="scss">

</style>