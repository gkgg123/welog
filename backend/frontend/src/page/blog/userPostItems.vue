<template>
  <div id="post-items">
    <div v-if="articles.length" v-cloak>
      <div class="post-articles" v-for="article in articles" :key="article.pid">
        <div class="post-article">
          <li class="article-title">
            <router-link
              :to="{
              name: 'userPost',
              params: { id: $route.params.id, pid: article.pid },
            }"
            >{{ article.title }}</router-link>
          </li>
          <div class="article-content">{{ article.content }}</div>
          <!--  <div v-for="tag in articles.tags" :key="tag"> -->
          <div class="article-tag">
            <span>태그</span>
            <span>xoasd</span>
          </div>
          <li class="article-day">{{ article.createDate }}</li>
        </div>
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
        var receive = Object.values(res.data);
        const temp = receive.map((item) => {
          if (!!item.post.tags) {
            item.post.tags = item.post.tags.split(",").filter((tag) => !!tag);
          } else {
            item.post.tags = [];
          }
          item.post.likeCount = item.likeCount;

          return item.post;
        });
        console.log(temp);
        console.log(receive);
        this.articles = temp;
      });
    },
  },
  created() {
    this.getArticles();
  },
};
</script>

<style></style>
