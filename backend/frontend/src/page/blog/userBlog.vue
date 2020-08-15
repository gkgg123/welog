<template>
  <div id="personal">
    <div id="personalUser">
      <div class="userInfo">
        <div class="userBar">
          <img
            class="userImage"
            src="https://cdn0.iconfinder.com/data/icons/set-ui-app-android/32/8-512.png"
            alt="profileImage"
          />
          <div class="userIntro">
            <h2 class="box">유저 아이디</h2>
            <p class="box">유저 한줄 소개</p>
          </div>
        </div>
      </div>
    </div>
    <div>
      <div id="personalPost">
        <div class="tagbar">
          <h2>태그목록</h2>
          <ul class="tagItem">
            <li>전체보기(13)</li>
            <li>tag1(6)</li>
            <li>tag2(5)</li>
            <li>tag3(2)</li>
          </ul>
        </div>
        <div class="postBox">
          <div class="postBar">
            <router-link
              class="postLink"
              :to="{
                name: constants.URL_TYPE.POST.POSTITEMS,
                params: { id: $route.params.id },
              }"
            >글</router-link>
            <router-link
              class="postLink"
              :to="{
                name: constants.URL_TYPE.POST.BLOGINTRO,
                params: { id: $route.params.id },
              }"
            >소개글</router-link>
          </div>
          <div class="smallTagbar">
            <span class="smallTagItem">전체보기(13)</span>
            <span class="smallTagItem">tag1(6)</span>
            <span class="smallTagItem">tag2(5)</span>
            <span class="smallTagItem">tag3(2)</span>
          </div>
          <div class="postView">
            <router-view></router-view>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import "../../assets/css/personal.scss";
import constants from "@/lib/constants.js";

const whiteboardUrl = "@/assets/img/whiteboard.png";

export default {
  name: "userBlog",
  methods: {},
  data() {
    return {
      constants,
      postImg: whiteboardUrl,
    };
  },
  created() {},
};
</script>

<style></style>
