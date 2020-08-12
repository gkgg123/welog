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
            <span v-for="tag in article.tags" :key="tag">{{tag}}</span>
          </div>
          <li class="article-day">{{ article.createDate }}</li>
        </div>
      </div>
    </div>
    <div class="post-item" v-else v-cloak>
      <h3>작성된 글이 없습니다.</h3>
      <img id="defalutimg" src="img/no post.jpg" />
    </div>
    <div id="bottomSensor"></div>
  </div>
</template>

<script>
import axios from "axios";
import constants from "@/lib/constants.js";
import { mapState, mapActions } from "vuex";
export default {
  name: "userPostItems",
  data() {
    return {
      constants,
    };
  },
  computed: {
    ...mapState(["articles", "nextPage", "pageLimit"]),
  },
  methods: {
    ...mapActions(["getArticles", "attachArticles"]),
    addScrollMonitor() {
      const bottomSensor = document.querySelector("#bottomSensor");
      const watcher = scrollMonitor.create(bottomSensor);
      watcher.enterViewport(() => {
        setTimeout(() => {
          this.attachArticles();
        }, 500);
      });
    },
  },
  created() {
    this.getArticles(`post/${this.$route.params.id}/`);
  },
  mounted() {
    this.addScrollMonitor();
  },
};
</script>

<style></style>
