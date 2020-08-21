<template>
  <div class="search">
    <div class="search-box">
      <i class="fas fa-search"></i>
      <input type="text" :value="this.$route.query.search" />
    </div>
    <div class="search-count">
      <p>총 {{ articles.length }}개의 포스트를 찾았습니다.</p>
    </div>
    <div class="search-item" v-if="!!articles.length">
      <div class="search-list" v-for="article in articles" :key="article.pid">
        <div class="list-user">
          <img
            class="user-img"
            src="https://cdn0.iconfinder.com/data/icons/set-ui-app-android/32/8-512.png"
            alt
          />
          <router-link
            v-if="article.author"
            :to="{
              name: constants.URL_TYPE.POST.BLOG,
              params: { id: article.author },
            }"
          >{{ article.author }}</router-link>
        </div>
        <div class="list-title">
          <router-link
            v-if="article.pid"
            :to="{
              name: constants.URL_TYPE.POST.POST,
              params: { id: article.author, pid: article.pid },
            }"
          >{{ article.title }}</router-link>
        </div>
        <div class="list-content">
          <router-link
            v-if="article.pid"
            :to="{
              name: constants.URL_TYPE.POST.POST,
              params: { id: article.author, pid: article.pid },
            }"
          >{{ article.content.slice(0, 300) }} ...</router-link>
        </div>
        <div class="list-data">
          <span class="date">
            {{ article.createDate.slice(0, 4) }}년
            {{ article.createDate.slice(5, 7) }}월
            {{ article.createDate.slice(8, 10) }}일
          </span>
        </div>
      </div>
      <div id="bottomSensor" style="height:10px;"></div>
    </div>
    <div v-else class="search-item">
      <h1>
        죄송합니다. {{ this.$route.query.search }}과(와) 일치하는 결과가
        없습니다.
      </h1>
      <li>철자가 정확한지 확인하세요.</li>
      <li>좀 더 간단하게 입력하여 검색해 보세요.</li>
    </div>
    <div id="bottomSensor" style="height:10px;"></div>
  </div>
</template>

<script>
import { mapState, mapActions, mapGetters } from "vuex";
import "@/assets/css/post.scss";
import constants from "@/lib/constants";
export default {
  name: "search",
  data() {
    return {
      options: {
        text: "제목+내용",
        tag: "태그",
        title: "제목",
        content: "내용",
      },
      location: {
        text: "text",
        tag: "tag",
        title: "title",
        content: "content",
      },
      constants,
    };
  },
  watch: {
    $route(to, from) {
      const target = this.$route.query.type;
      this.getArticles({
        location: `post/search/${this.$route.query.type}`,
        query: { [target]: this.$route.query.search },
      });
    },
  },
  computed: {
    ...mapState(["articles", "nextPage", "pageLimit", "receiveArticleList"]),
    ...mapGetters(["isreceived"]),
  },
  methods: {
    ...mapActions(["getArticles", "attachArticles"]),
    addScrollMonitor() {
      const bottomSensor = document.querySelector("#bottomSensor");
      const watcher = scrollMonitor.create(bottomSensor);

      if (!!this.receiveArticleList.length) {
        watcher.enterViewport(() => {
          if (this.isreceived) {
            setTimeout(() => {
              this.attachArticles();
            }, 1000);
          }
        });
      }
    },
    headerInit() {
      this.$store.commit("SET_header", "welog"),
        this.$store.commit("SET_headerPath", {
          PathName: constants.URL_TYPE.MAIN.MAIN,
          PathParams: null,
        });
    },
  },
  mounted() {
    this.addScrollMonitor();
  },
  created() {
    this.headerInit();
    const target = this.$route.query.type;
    this.getArticles({
      location: `post/search/${this.$route.query.type}`,
      query: { [target]: this.$route.query.search },
    });
  },
};
</script>

<style></style>
