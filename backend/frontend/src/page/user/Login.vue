<template>
  <div class="user" id="login">
    <div class="wrapC table">
      <div class="middle">
        <h1>SS_log</h1>
        <div class="input-wrap">
          <input v-model="loginData.useremail" id="email" placeholder="이메일을 입력해주세요" type="text" />
        </div>
        <div class="input-wrap">
          <input
            v-model="loginData.password"
            type="password"
            id="password"
            placeholder="영문, 숫자 혼용 8자 이상"
          />
        </div>
        <button class="btn btn--back btn--login" @click="login(loginData)">로그인 하기</button>
        <div class="add-option">
          <div class="wrap">
            <p>아직 회원이 아니신가요?</p>
            <router-link v-bind:to="{ name: constants.URL_TYPE.USER.JOIN }" class="btn--text">회원가입</router-link>
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
    // login() {
    //   const loginData = {
    //     useremail: this.email,
    //     password: this.password,
    //   };
    //   axios
    //     .post(constants.baseUrl + "user/login", loginData)
    //     .then((res) => {
    //       const token = res.headers.authorization.replace("Bearer", "");
    //       sessionStorage.setItem("userToken", token);
    //     })
    //     .catch((res) => {
    //       console.log(res.response);
    //     });
    // },
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
