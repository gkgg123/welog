<template>
  <section class="post-list">
    <section class="loading-box" v-if="state === '1'">
      <img src="img/loading.gif" alt />
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
          <div v-if="!article.imageList.length" :class="{ 'post-img': !article.imageList.length }" />
          <div
            v-else
            :style="{
              'background-image': `url(
                ${article.imageList[0].path.replace(s3url, constants.imageUrl)}
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
          >작성자 : {{ article.author }}</router-link>
          <span>♥ {{ article.likeCount }}</span>
        </div>
      </div>
    </div>
    <div
      role="status"
      v-if="loading"
      style="width:100%; text-align:center; height:0vh; padding:0px; margin:0px"
    >
      <img src="/img/pageloading.gif" alt />
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
      loading: false,
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
    ...mapGetters(["isreceived"]),
  },
  methods: {
    ...mapActions(["getArticles", "attachArticles"]),
    async totalCreate() {
      await this.getArticles({ location: "post/popularity" });
      this.state = !!this.receiveArticleList.length;
    },
    addScrollMonitor() {
      const bottomSensor = document.querySelector("#bottomSensor");
      const watcher = scrollMonitor.create(bottomSensor);
      watcher.enterViewport(() => {
        if (this.nextPage <= this.pageLimit) {
          if (this.isreceived) {
            this.loading = true;
          }
          setTimeout(() => {
            if (this.isreceived) {
              this.attachArticles().then(() => {
                this.loading = false;
              });
            }
          }, 1000);
        }
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
