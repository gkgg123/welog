<template>
  <div>
    <h1>안녕하세요 여기는 Search입니다.</h1>
    <h1>{{ options[this.$route.query.type] }} : {{ this.$route.query.search }}</h1>
    <div v-if="!!articles.length">
      <div class="post-card-box" v-for="article in articles" :key="article.pid">
        <div class="post-card">
          <router-link
            v-if="article.pid"
            :to="{
            name: constants.URL_TYPE.POST.POST,
            params: { id: article.author, pid: article.pid },
          }"
          >
            <div class="post-img" />

            <div class="contents">
              <h3>{{ article.title }}</h3>

              <span class="date">
                {{ article.createDate.slice(0, 4) }}년
                {{ article.createDate.slice(5, 7) }}월
                {{ article.createDate.slice(8, 10) }}일ㆍ
              </span>
              <span class="comment">댓글 0개</span>
            </div>
          </router-link>

          <div class="writer-wrap">
            <router-link
              v-if="article.author"
              :to="{
              name: constants.URL_TYPE.POST.BLOG,
              params: { id: article.author },
            }"
            >{{ article.author }}</router-link>
            <span>♥ 2</span>
          </div>
        </div>
      </div>
    </div>
    <div v-else>
      <h1>검색결과가 없습니다.</h1>
    </div>
    <div id="bottomSensor"></div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
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
  },
  methods: {
    ...mapActions(["getArticles", "attachArticles"]),
    addScrollMonitor() {
      const bottomSensor = document.querySelector("#bottomSensor");
      const watcher = scrollMonitor.create(bottomSensor);

      if (!!this.receiveArticleList.length) {
        watcher.enterViewport(() => {
          setTimeout(() => {
            this.attachArticles();
          }, 1000);
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
