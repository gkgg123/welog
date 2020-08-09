<template>
  <div class="post-items">
    <div class="post-item" v-if="!articles.length">
      <h3>작성된 글이 없습니다.</h3>
      <img src="img/no post.jpg" />
    </div>
    <div v-else class="post-items">
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
          <li class="article-content">{{ article.content }}</li>
          <div v-for="tag in articles.tags" :key="tag">
            <span class="article-tag">{{ tag }}</span>
          </div>
          <li class="article-day">
            {{ article.createDate.slice(0, 4) }}년
            {{ article.createDate.slice(5, 7) }}월
            {{ article.createDate.slice(8, 10) }}일
          </li>
          <li class="article-comment">0개의 댓글</li>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "userPostItems",
  data() {
    return {
      articles: [],
    };
  },
  methods: {
    test() {
      const username = this.$route.params.id;
      axios.get("http://localhost:8080/" + `post/${username}/`).then((res) => {
        this.articles = res.data;
        console.log(res);
      });
    },
  },
  mounted() {
    this.test();
  },
};
</script>

<style></style>
