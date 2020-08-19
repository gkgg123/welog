<template>
  <section class="post-list">
    <section class="spin-box" v-if="state === '1'">
      <div class="spinner-border" role="status">
        <span class="sr-only">Loading...</span>
      </div>
    </section>
    <div
      v-else-if="state === true"
      class="post-card-box"
      v-for="article in articles"
      :key="article.pid"
    >
      <div class="post-card">
        <router-link
          :to="{
            name: constants.URL_TYPE.POST.POST,
            params: { id: article.author, pid: article.pid },
          }"
        >
          <div
            v-if="!article.imageList.length"
            :class="{ 'post-img': !article.imageList.length }"
          />
          <div
            v-else
            :style="{
              'background-image': `url(
                ${constants.imageUrl + article.imageList[0].iid}
              )`,
            }"
            style="background-size:100% 170px;
                  background-repeat : no-repeat;
                  height : 170px;"
          ></div>

          <div class="contents">
            <h3>{{ article.title }}</h3>
            <span class="date">
              {{ article.createDate.slice(0, 4) }}년
              {{ article.createDate.slice(5, 7) }}월
              {{ article.createDate.slice(8, 10) }}일ㆍ
            </span>
            <span class="comment">댓글 {{ article.commentCount }}개</span>
          </div>
        </router-link>

        <div class="writer-wrap">
          <router-link
            :to="{
              name: constants.URL_TYPE.POST.BLOG,
              params: { id: article.author },
            }"
            >{{ article.author }}</router-link
          >
          <span>♥ {{ article.likeCount }}</span>
        </div>
      </div>
    </div>
    <div id="bottomSensor" style="height:10px"></div>
  </section>
</template>

<script>
import "@/assets/css/post.scss";
import constants from "@/lib/constants";
import { mapState, mapGetters, mapActions } from "vuex";

export default {
  name: "recommandList",
  created() {
    this.totalCreate();
  },
  data: () => {
    return {
      constants,
      state: "1",
    };
  },
  computed: {
    ...mapState(["articles", "nextPage", "pageLimit", "receiveArticleList"]),
    ...mapGetters(["isreceived"]),
  },
  methods: {
    ...mapActions(["getArticles", "attachArticles"]),
    async totalCreate() {
      await this.getArticles({ location: "post/popularity" });
      this.state = !!this.receiveArticleList;
    },
    addScrollMonitor() {
      const bottomSensor = document.querySelector("#bottomSensor");
      const watcher = scrollMonitor.create(bottomSensor);
      watcher.enterViewport(() => {
        setTimeout(() => {
          if (this.isreceived) {
            this.attachArticles();
          }
        }, 500);
      });
    },
    loadUntilVieportIsFull() {
      const bottomSensor = document.querySelector("#bottomSensor");
      const watcher = scrollMonitor.create(bottomSensor);
      if (watcher.isFullyInViewport) {
        setTimeout(() => {
          if (this.isreceived) {
            this.attachArticles();
          }
        });
      }
    },
  },
  mounted() {
    this.addScrollMonitor();
  },
  updated() {
    this.loadUntilVieportIsFull();
  },
};
</script>

<style></style>
