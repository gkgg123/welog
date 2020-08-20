<template>
  <div id="personal">
    <div id="personalUser">
      <div class="userInfo">
        <div class="userBar">
          <img class="userImage" :src="blogprofile" alt="profileImage" />
          <div class="userIntro">
            <h2 class="box">{{blogusername}}</h2>
            <p class="box">{{bloglineintro}}</p>
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
import axios from "axios";
import { mapState } from "vuex";

const whiteboardUrl = "@/assets/img/whiteboard.png";

export default {
  name: "userBlog",
  methods: {},
  data() {
    return {
      constants,
      postImg: whiteboardUrl,
      blogusername: "",
      blogprofile: "",
      bloglineintro: "",
    };
  },
  methods: {
    getblogusername() {
      axios
        .get(constants.baseUrl + `user/${this.$route.params.id}`)
        .then((res) => {
          this.blogusername = res.data.username;
          this.bloglineintro = res.data.userDescription;
          if (
            res.data.profileUrl === "no_img" ||
            res.data.profileUrl === null
          ) {
            this.blogprofile = this.defalutprofileimg;
          } else {
            this.blogprofile = res.data.profileUrl.replace(
              this.s3url,
              constants.imageUrl
            );
          }
        });
      this.blogusername = this.$route.params.id;
    },
  },
  computed: {
    ...mapState(["defalutprofileimg", "s3url"]),
  },
  created() {
    this.getblogusername();
  },
  watch: {
    $route(to, from) {
      this.getblogusername();
    },
  },
};
</script>

<style></style>
