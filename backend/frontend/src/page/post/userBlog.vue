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
            <li>tag1</li>
            <li>tag2</li>
            <li>tag3</li>
          </ul>
        </div>
        <div class="postBox">
          <div class="postBar">
            <router-link
              class="postLink"
              :to="{
                name: 'userPostItems',
                params: { id: $route.params.id },
              }"
            >글</router-link>
            <router-link
              class="postLink"
              :to="{
                name: 'userPersonalIntro',
                params: { id: $route.params.id },
              }"
            >소개글</router-link>
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

export default {
  name: "userBlog",
  methods: {
    headerChange() {
      const urlname = this.$route.params.id;
      console.log(urlname, "test");
      this.$store.commit("SET_header", urlname);
      this.$store.commit("SET_headerPath", {
        PathName: "userPostItems",
        PathParams: urlname,
      });
    },
  },
  created() {
    this.headerChange();
  },
};
</script>

<style></style>
