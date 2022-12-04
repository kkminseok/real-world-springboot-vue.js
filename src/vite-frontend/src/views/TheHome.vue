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
              <div v-if="isLoading">
                Loading articles...
              </div>
              <div v-if="isEmpty">
                No articles are here... yet.
              </div>
            </div>
              <article-list
                  v-if="isLogin && feedActive"
                  :value="isLogin"
                  :value2="isEmpty"
                  @loading="onChangeLoading"
                  @emptied="emptyCheck">
              </article-list>
              <article-list-global
                v-else
                :isLoading="isLoading"
                :isEmpty="isEmpty"
                :globalActive="globalActive"
                @loading="onChangeLoading"
                @emptied="emptyCheck">
              </article-list-global>
          </div>
        <div class="col-md-3">
          <div class="sidebar">
            <p>Popular Tags</p>
            <tag-lists></tag-lists>
          </div>
        </div>
        <div v-for="test in articleLists">
          {{test}}
        </div>
      </div>
    </div>
    
    <pagination-component v-model="currentPage" :numberOfPages="rowsPerPage"></pagination-component>

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
    'article-list': articleList,
    'article-list-global': articleListGlobal,
    'tag-lists': tagLists,
    'pagination-component': pagination,
  },
  setup(){
    const isLoading = ref(true);
    const isEmpty = ref(false);
    const store = useStore();
    const isLogin =  store.state.token == '' ? false : true;
    const feedActive = ref(true);
    const globalActive = ref(false);

    const currentPage = ref(1);
    const rowsPerPage = ref(20);

    const { articleLists, listsAreLoading, loadLists, numberOfPages } = usePaginationApi(currentPage, rowsPerPage);

    const onChangeLoading = (val : boolean) => {
      isLoading.value = val;
    }
    const emptyCheck = (val: boolean) => {
      isEmpty.value = val;
    }

    const feedSelect = () => {
      feedActive.value=true;
      globalActive.value=false;
      isEmpty.value=false;
      isLoading.value=true;
    }

    const globalSelect = () => {
      feedActive.value=false;
      globalActive.value=true;
      isEmpty.value=false;
      isLoading.value=true;
    }

    onMounted(async () => loadLists())

    return { isLoading, isEmpty, isLogin, currentPage, rowsPerPage, numberOfPages, feedActive, globalActive, articleLists, onChangeLoading, emptyCheck, feedSelect, globalSelect };
  }
}
</script>

<style lang="scss">

</style>