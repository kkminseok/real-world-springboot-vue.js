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
                  <a class="nav-link"
                     @click="feedSelect"
                  :class="{ active : feedActive}">Your Feed</a>
                </li>
                <li class="nav-item">
                  <a class="nav-link"
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
            <div v-if="isLogin && feedActive">
              <article-list
                  :value="isLoading"
                  :value2="isEmpty"
                  @loading="onChangeLoading"
                  @emptied="emptyCheck">
              </article-list>
            </div>
            <div v-else>
              <article-list-global
                :value="isLoading"
                :value2="isEmpty"
                @loading="onChangeLoading"
                @emptied="emptyCheck">

              </article-list-global>

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

import articleList from '@/components/ArticleListFeed.vue'
import articleListGlobal from "@/components/ArticleListGlobal.vue";
import {ref} from "vue";
import {useStore} from "vuex";
export default {
  name: "TheHome",
  components: {
    'article-list': articleList,
    'article-list-global': articleListGlobal},
  setup(){
    const isLoading = ref(true);
    const isEmpty = ref(false);
    const store = useStore();
    const isLogin =  store.state.token == '' ? false : true;
    const feedActive = ref(true);
    const globalActive = ref(false);

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
      feedActive.value = false;
      globalActive.value = true;
      isEmpty.value=false;
      isLoading.value=true;
    }

    return { isLoading, isEmpty, isLogin, feedActive, globalActive, onChangeLoading, emptyCheck, feedSelect, globalSelect };
  }
}
</script>

<style scoped>

</style>