<template>
  <div class="personal-intro">
    소개 페이지입니다.
    <span>{{intro}}</span>
    <button class="btn btn-primary" v-if="isCheckAuthor" @click="openTextarea">수정</button>

    <div id="introConfirm" style="display:none;" class="border border-primary">
      <textarea
        class="border border-primary"
        v-model="introConfirm"
        name
        id
        cols="20"
        rows="10"
        style="width:35vw; height:25vh"
      ></textarea>
      <button class="btn btn-primary" @click="updateIntro">수정완료</button>
      <button class="btn btn-primary" @click="closeTextarea">닫기</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import constants from "@/lib/constants.js";
import { mapState } from "vuex";
export default {
  name: "userPersonalIntro",
  data() {
    return {
      constants,
      intro: "",
      introConfirm: "",
    };
  },
  methods: {
    getIntro() {
      const author = this.$route.params.id;
      axios.get(constants.baseUrl + `post/${author}/intro`).then((res) => {
        if (res.data === "") {
          this.intro = res.data;
        } else {
          this.intro = res.data.content;
        }
      });
    },
    openTextarea() {
      this.introConfirm = this.intro;
      var confirmTextarea = document.querySelector("#introConfirm");
      confirmTextarea.setAttribute("style", "display:block;  height:30vh;");
    },
    closeTextarea() {
      this.introConfirm = "";
      var confirmTextarea = document.querySelector("#introConfirm");
      confirmTextarea.setAttribute("style", "display:none;");
    },
    updateIntro() {
      const author = this.$route.params.id;
      const intro = {
        content: this.introConfirm,
      };
      const totalData = {
        intro: [intro],
        token: this.authToken,
      };
      console.log(totalData);
      axios
        .post(constants.baseUrl + `post/${author}/intro/`, totalData)
        .then((res) => {
          this.intro = res.data.content;
          var introConfirm = document.querySelector("#introConfirm");
          introConfirm.setAttribute("style", "display:none");
        })
        .catch((err) => {
          console.log(err.response);
        });
    },
  },
  created() {
    this.getIntro();
  },
  computed: {
    ...mapState(["authToken", "username"]),
    isCheckAuthor() {
      const author = this.$route.params.id;
      console.log(author);
      console.log(this.username);
      if (author === this.username) {
        return true;
      } else {
        return false;
      }
    },
  },
};
</script>

<style>
</style>