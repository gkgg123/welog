<template>
  <div class="user" id="login">
    <div class="wrapC table">
      <img class="logo-welcome" src="img\Welcome.png" alt="welcome" />
      <img class="login-image" src="img\Logo.png" alt="logo-image" />
      <div class="middle">
        <div class="input-wrap">
          <i class="fas fa-user"></i>
          <input
            v-model="loginData.useremail"
            id="email"
            type="text"
            required
          />
          <label for="email">email을 입력해주세요</label>
        </div>
        <div class="input-wrap">
          <i class="fas fa-key"></i>
          <input
            v-model="loginData.password"
            type="password"
            id="password"
            required
          />
          <label for="password">비밀번호를 입력해주세요</label>
        </div>
        <button
          class="btn btn--back btn--login rounded-lg mb-2"
          @click="login(loginData)"
        >
          로그인 하기
        </button>
        <button class="btn btn--google-login rounded-lg mb-2">
          <i class="fab fa-google-plus-g fa-2x"></i>
          <span>구글 아이디로 로그인</span>
        </button>

        <button class="btn btn--naver-login rounded-lg mb-2">
          <img class="naver-icon" src="img\naver-icon.PNG" alt />
          <span>네이버 아이디로 로그인</span>
        </button>
        <button class="btn btn--facebook-login rounded-lg mb-2">
          <i class="fab fa-facebook-square fa-2x"></i>
          <span>페이스북 아이디로 로그인</span>
        </button>

        <div class="add-option">
          <div class="wrap">
            <p>아직 회원이 아니신가요?</p>
            <router-link
              v-bind:to="{ name: constants.URL_TYPE.USER.JOIN }"
              class="btn--text"
              >회원가입</router-link
            >
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import "../../assets/css/user.scss";
import constants from "../../lib/constants";
import axios from "axios";
import { mapActions } from "vuex";

export default {
  components: {},
  created() {},
  watch: {},
  methods: {
    ...mapActions(["login"]),
  },
  data: () => {
    return {
      constants,
      loginData: {
        useremail: null,
        password: null,
      },
    };
  },
};
</script>
