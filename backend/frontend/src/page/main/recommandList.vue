<template>
  <section class="post-list">
    <div class="post-card-box" v-for="article in articles" :key="article.pid">
      <div class="post-card">
        <router-link
          :to="{name :constants.URL_TYPE.POST.POST, params :{id : article.author, pid:article.pid}}"
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
            :to="{name:constants.URL_TYPE.POST.BLOG , params : {id : article.author}}"
          >{{ article.author }}</router-link>
          <span>♥ 2</span>
        </div>
      </div>
    </div>
    <div id="bottomSensor"></div>
    <button @click="attachArticles">더보기</button>
  </section>
</template>

<script>
import "@/assets/css/post.scss";
import constants from "@/lib/constants";
import { mapState, mapActions } from "vuex";

export default {
  name: "recommandList",
  created() {
    this.getArticles({ location: "post/popularity" });
  },
  data: () => {
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
  mounted() {
    this.addScrollMonitor();
  },
};
</script>

<style>
</style>