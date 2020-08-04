<template>
  <div id="post-items">
    <div class="post-item" v-if="!articles.length">
      <h3>작성된 글이 없습니다.</h3>
      <img src="img/no post.jpg" />
    </div>
    <div v-else>
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
