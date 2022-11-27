<template>
  <div class="tag-list">
    <a href="javascript:(0)" class="tag-pill tag-default"
       v-for = "tag in listTags.tags">{{tag}}</a>
  </div>
</template>

<script lang="ts">
import {onMounted, defineComponent, reactive} from "vue";
import axios from "axios";

export default defineComponent({
  name: "TagList",
  setup(){
    const url = import.meta.env.VITE_BASE_URL;
    const listTags = reactive({
      tags: new Array()
    })

    onMounted(() => {
      axios.get(url + "/api/tags")
          .then(response => {
            listTags.tags = response.data.tags
          });
    })

    return { listTags }
  }
})
</script>

<style scoped>

</style>