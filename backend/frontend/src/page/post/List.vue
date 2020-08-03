<template>
  <div class="post">
    <div class>
      <h2>전체글</h2>
      <div>
        <section class="post-list col-12 col-md-9">
          <div class="w-100 col-xl-4 col-sm-6 col-12" v-for="article in articles">
            <div class="post-card">
              <a>
                <div
                  :style="{
                    backgroundImage:
                      'url(https://www.ipcc.ch/site/assets/uploads/sites/3/2019/10/img-placeholder.png)',
                  }"
                  class="post-img"
                />

                <div class="contents">
                  <h3>{{article.title}}</h3>
                  <p class="content">{{article.content}}</p>
                  <span
                    class="date"
                  >{{article.createDate[0]}}년 {{article.createDate[1]}}월 {{article.createDate[2]}}일ㆍ</span>
                  <span class="comment">댓글 0개</span>
                </div>
              </a>

              <div class="writer-wrap">
                <a>{{article.author}}</a>
                <span>♥ 2</span>
              </div>
            </div>
          </div>
        </section>

        <div class="tag-list-wrap col-md-3 col-12">
          <h4>공지사항</h4>
          <ul class="tag-list mb-5">
            <li>공지사항1</li>
            <li>공지사항2</li>
            <li>공지사항3</li>
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
import "../../assets/css/post.scss";
import axios from "axios";

export default {
  name: "Post",
  components: {},
  watch: {},
  created() {
    this.$store.commit("SET_header", "welog"),
      this.$store.commit("SET_headerPath", "/");
    this.getArticles();
  },
  data: () => {
    return {
      articles: [],
    };
  },
  methods: {
    getArticles() {
      axios.get("http://localhost:8080/" + "post/latest/").then((res) => {
        console.log(res);
        this.articles = res.data;
      });
    },
  },
};
</script>
