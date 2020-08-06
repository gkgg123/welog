<template>
  <div class="post">
    <div class>
      <div class="main-post-bar">
        <div class="post-bar-item" @click="getArticles">
          <i class="fas fa-border-all"></i>
          <h2>전체 글</h2>
        </div>
        <div class="post-bar-item" @click="getArticlesOrderByPopularity">
          <i class="fas fa-chart-line"></i>
          <h2>인기 글</h2>
        </div>
      </div>
      <div>
        <section class="post-list col-12 col-md-9">
          <div
            class="w-100 col-xl-4 col-sm-6 col-12"
            v-for="article in articles"
            :key="article.pid"
          >
            <div class="post-card">
              <router-link
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
                  <div v-for="tag in article.tags" :key="tag">
                    <span>
                      <a href="#">{{ tag }}</a>
                    </span>
                  </div>
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
                  :to="{name:constants.URL_TYPE.POST.POSTITEMS , params : {id : article.author}}"
                >{{ article.author }}</router-link>
                <span>♥ 2</span>
              </div>
            </div>
          </div>
          <div id="bottomSensor"></div>
          <button @click="getArticles">더보기</button>
        </section>

        <div class="tag-list-wrap col-md-3 col-12">
          <h4>공지사항</h4>
          <ul class="tag-list mb-5">
            <li class="notice">
              <a href="#">공지사항1</a>
            </li>
            <div class="text-secondary mb-3">2020년 8월 4일</div>
            <li class="notice">
              <a href="#">공지사항2</a>
            </li>
            <div class="text-secondary mb-3">2020년 8월 4일</div>
            <li class="notice">
              <a href="#">제목이 긴 공지사항 예시입니다. 한 이정도는 될 것 같습니다.</a>
            </li>
            <div class="text-secondary mb-3">2020년 8월 4일</div>
            <a href="#" class="text-secondary">
              <u>더보기</u>
            </a>
          </ul>
          <h4>인기태그</h4>
          <ul class="tag-list">
            <li>#태그1 (8)</li>
            <li>#태그2 (5)</li>
            <li>#태그3 (2)</li>
            <a href="#" class="text-secondary">
              <u>더보기</u>
            </a>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<style>
ul {
  list-style: none;
}
</style>

<script>
import "@/assets/css/post.scss";
import constants from "@/lib/constants";
import axios from "axios";

export default {
  name: "Main",
  components: {},
  watch: {},
  created() {
    this.$store.commit("SET_header", "welog"),
      this.$store.commit("SET_headerPath", {
        PathName: "main",
        PathParams: null,
      });
    this.getTotalArticles();
  },
  data: () => {
    return {
      constants,
      totalarticles: [],
      articles: [],
      nextpage: 0,
      pageLimit: 0,
    };
  },
  methods: {
    getTotalArticles() {
      axios
        .get(constants.baseUrl + "post/latest/")
        .then((res) => {
          const temp = Object.values(res.data);
          temp.forEach((item) => {
            if (!!item.tags) {
              item.tags = item.tags.split(",").filter((tag) => !!tag);
            } else {
              item.tags = [];
            }
          });
          this.totalarticles = temp;
          this.pageLimit = parseInt(this.totalarticles.length / 10);
        })
        .then(() => {
          this.getArticles();
        });
    },
    getArticlesOrderByPopularity() {
      axios.get(constants.baseUrl + "post/popularity/").then((res) => {
        this.totalarticles = res.data;
      });
    },
    getArticles() {
      const currentpage = this.nextpage++;
      if (currentpage <= this.pageLimit - 1) {
        const nextArticles = this.totalarticles.slice(
          currentpage * 10,
          (currentpage + 1) * 10
        );
        this.articles = [...this.articles, ...nextArticles];
      } else {
        const nextArticles = this.totalarticles.slice(currentpage * 10);
        this.articles = [...this.articles, ...nextArticles];
      }
    },
    addScrollMonitor() {
      console.log("addScrollMonitor");
      const bottomSensor = document.querySelector("#bottomSensor");
      const watcher = scrollMonitor.create(bottomSensor);
      console.log(watcher.isFullyInViewport, "1");
      watcher.enterViewport(() => {
        setTimeout(() => {
          this.getArticles();
        }, 500);
      });
    },
  },
  mounted() {
    this.addScrollMonitor();
  },
};
</script>
