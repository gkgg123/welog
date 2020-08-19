<template>
  <div class="personal-intro">
    <p v-if="intro">
      <span>{{intro}}</span>
    </p>
    <p v-else>소개 페이지입니다.</p>
    <div class="button-layout">
      <button class="updateBtn" v-if="isCheckAuthor" @click="openTextarea">수정하기</button>
    </div>
    <div id="introConfirm" style="display:none;">
      <textarea class="intro-box" v-model="introConfirm"></textarea>
      <div class="btn-box">
        <button id="updateConfirmBtn" class="updateConfirmBtn" @click="updateIntro">수정완료</button>
        <button class="updateConfirmBtn" @click="closeTextarea">닫기</button>
      </div>
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
      var confirmUpdateButton = document.querySelector(".updateBtn");
      confirmTextarea.setAttribute("style", "display:block;  height:30vh;");
      confirmUpdateButton.setAttribute("style", "display:none;");
    },
    closeTextarea() {
      this.introConfirm = "";
      var confirmTextarea = document.querySelector("#introConfirm");
      var confirmUpdateButton = document.querySelector(".updateBtn");
      confirmTextarea.setAttribute("style", "display:none;");
      confirmUpdateButton.setAttribute("style", "display:block;");
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
      var confirmUpdateButton = document.querySelector(".updateBtn");
      confirmUpdateButton.setAttribute("style", "display:block;");
    },
  },
  created() {
    this.getIntro();
  },
  computed: {
    ...mapState(["authToken", "username"]),
    isCheckAuthor() {
      const author = this.$route.params.id;
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