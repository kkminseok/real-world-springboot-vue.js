<template>
  <div class="tag-list">
    <a href="javascript:(0)" class="tag-pill tag-default"
       v-for = "tag in listTags.tags">{{tag}}</a>
  </div>
</template>

<script lang="ts">
import {onMounted, defineComponent, reactive} from "vue";
import axios from "axios";
import {getTags} from "@/api";

export default defineComponent({
  name: "TagList",
  setup(){
    const listTags = reactive({
      tags: new Array()
    })

    onMounted(async () => {
      try{
        const { data } = await getTags();
        listTags.tags = data.tags;
      }catch (error: any){
        alert(error);
      }
    })

    return { listTags }
  }
})
</script>

<style scoped>

</style>