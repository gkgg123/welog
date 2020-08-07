<template>
  <div class="post">
    <div class>
      <div class="main-post-bar">
        <router-link :to="{ name : constants.URL_TYPE.MAIN.LIST}">
          <div class="post-bar-item">
            <i class="fas fa-border-all"></i>
            <h2>전체 글</h2>
          </div>
        </router-link>
        <router-link :to="{ name : constants.URL_TYPE.MAIN.RECOMMAND}">
          <div class="post-bar-item">
            <i class="fas fa-chart-line"></i>
            <h2>인기 글</h2>
          </div>
        </router-link>
      </div>
      <div>
        <router-view />
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
  data() {
    return {
      constants,
    };
  },
};
</script>
