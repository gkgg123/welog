<template>
  <div>
    <commentItem
      :wauthor="wauthor"
      :comment="comment"
      v-for="comment in commentList"
      :key="comment.cid"
    />
  </div>
</template>

<script>
import commentItem from "@/page/blog/commentItem.vue";
import { mapState } from "vuex";
export default {
  name: "commentListitems",
  computed: {
    ...mapState(["commentList"]),
  },
  props: {
    wauthor: String,
  },
  components: {
    commentItem,
  },
};
</script>

<style>
</style>