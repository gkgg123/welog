<template>
  <div id="post-items">
    <div v-if="state === -100" class="spin-box">
      <div class="spinner-border" role="status">
        <span class="sr-only">Loading...</span>
      </div>
    </div>
    <div v-else-if="state === true" v-cloak>
      <div class="post-articles" v-for="article in articles" :key="article.pid">
        <div class="post-article">
          <div class="article-img">
            <router-link
              :to="{
                name: 'userPost',
                params: { id: $route.params.id, pid: article.pid },
              }"
            >
              <img v-if="!article.imageList.length" src="img/no_image.png" />
              <div
                v-else
                :style="{
              'background-image': `url(
                ${article.imageList[0].path.replace(s3url,constants.imageUrl)}
              )`,
            }"
                style="background-size: 80% 45vh;
                  background-repeat : no-repeat;
                  height : 45vh;"
              ></div>
            </router-link>
          </div>
          <li class="article-title">
            <router-link
              :to="{
                name: 'userPost',
                params: { id: $route.params.id, pid: article.pid },
              }"
            >{{ article.title }}</router-link>
          </li>
          <div class="article-tag">
            <span v-for="tag in article.tags" :key="tag">
              <router-link
                :to="{
                  name: constants.URL_TYPE.MAIN.SEARCH,
                  query: { type: 'tag', search: tag },
                }"
              >{{ tag }}</router-link>
            </span>
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
      state: -100,
    };
  },
  computed: {
    ...mapState([
      "articles",
      "nextPage",
      "pageLimit",
      "receiveArticleList",
      "s3url",
    ]),
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
    async totalCreate() {
      await this.getArticles({ location: `post/${this.$route.params.id}/` });
      this.state = !!this.receiveArticleList.length;
    },
  },
  created() {
    this.totalCreate();
  },
  mounted() {
    this.addScrollMonitor();
  },
  watch: {
    $route(to, from) {
      const target = this.$route.query.type;
      this.getArticles({ location: `post/${this.$route.params.id}/` });
    },
  },
};
</script>

<style></style>
