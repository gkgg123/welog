<template>
  <section class="post-list">
    <div class="post-card-box" v-for="article in articles" :key="article.pid">
      <div class="post-card">
        <router-link
          v-if="article.pid"
          :to="{name :constants.URL_TYPE.POST.POST, params :{id : article.author, pid:article.pid}}"
        >
          <div
            :style="{
                    backgroundImage:
                      'url(https://www.ipcc.ch/site/assets/uploads/sites/3/2019/10/img-placeholder.png)',
                  }"
            class="post-img"
          />

          <div class="contents">
            <h3>{{ article.title }}</h3>

            <hr />
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
  name: "recentList",
  created() {
    this.getArticles("post/latest");
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