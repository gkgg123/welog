<template>
  <section class="post-list col-12 col-md-9">
    <div class="w-100 col-xl-4 col-sm-6 col-12" v-for="article in articles" :key="article.pid">
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
</template>

<script>
import "@/assets/css/post.scss";
import constants from "@/lib/constants";
import axios from "axios";
export default {
  name: "recentList",
  created() {
    this.$store.commit("SET_header", "welog"),
      this.$store.commit("SET_headerPath", {
        PathName: constants.URL_TYPE.MAIN.LIST,
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
      const bottomSensor = document.querySelector("#bottomSensor");
      const watcher = scrollMonitor.create(bottomSensor);
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

<style>
</style>