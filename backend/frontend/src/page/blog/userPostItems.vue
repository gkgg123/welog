<template>
  <div id="post-items">
    <div v-if="articles.length" v-cloak>
      <div>
        <ul v-for="article in articles" :key="article.pid">
          <li>
            제목 :
            <router-link
              :to="{
              name: 'userPost',
              params: { id: $route.params.id, pid: article.pid },
            }"
            >{{ article.title }}</router-link>
          </li>
          <li>{{ article.createDate }}</li>
          <hr />
        </ul>
      </div>
    </div>
    <div class="post-item" v-else v-cloak>
      <h3>작성된 글이 없습니다.</h3>
      <img id="defalutimg" src="img/no post.jpg" />
    </div>
  </div>
</template>

<script>
import axios from "axios";
import constants from "@/lib/constants.js";
export default {
  name: "userPostItems",
  data() {
    return {
      articles: [],
    };
  },
  methods: {
    getArticles() {
      const username = this.$route.params.id;
      axios.get(constants.baseUrl + `post/${username}/`).then((res) => {
        this.articles = res.data;
      });
    },
  },
  created() {
    this.getArticles();
  },
};
</script>

<style></style>
