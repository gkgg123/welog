<template>
  <div class="user" id="login">
    <div class="wrapC table">
      <img class="logo-welcome" src="img\Welcome.png" alt="welcome" />
      <img class="login-image" src="img\home_logo.png" alt="logo-image" />
      <div class="middle">
        <div class="input-wrap">
          <i class="fas fa-user"></i>
          <input v-model="loginData.useremail" id="email" type="text" @keyup="checkemail" required />
          <label id="emaillabel" for="email">email을 입력해주세요</label>
        </div>
        <div class="input-wrap">
          <i class="fas fa-key"></i>
          <input
            v-model="loginData.password"
            type="password"
            id="password"
            @keyup="checkpassword"
            required
          />
          <label id="passwordlabel" for="password">비밀번호를 입력해주세요</label>
        </div>
        <button @click="checklogin">로그인 하기</button>
        <button class="btn btn--google-login mb-2" @click="willoffer">
          <i class="fab fa-google-plus-g fa-2x"></i>
          <span>구글 아이디로 로그인</span>
        </button>

        <button class="btn btn--naver-login rounded-lg mb-2" @click="willoffer">
          <img class="naver-icon" src="img\naver-icon.PNG" alt />
          <span>네이버 아이디로 로그인</span>
        </button>
        <button class="btn btn--facebook-login rounded-lg mb-2" @click="willoffer">
          <i class="fab fa-facebook-square fa-2x"></i>
          <span>페이스북 아이디로 로그인</span>
        </button>

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
  name: "Login",
  components: {},
  created() {},
  watch: {},
  methods: {
    ...mapActions(["login"]),
    willoffer() {
      alert("서비스 준비중입니다");
    },
    checklogin() {
      if (this.isemail && this.ispassword) {
        this.login(this.loginData);
      } else {
        alert("이메일이 잘못되거나 비밀번호가 너무 짧습니다.");
      }
    },
    checkemail() {
      const emaillabel = document.querySelector("#emaillabel");
      if (this.reg.test(this.loginData.useremail)) {
        emaillabel.setAttribute("style", "color:blue");
        emaillabel.innerText = "email형식에 맞습니다.";
        this.isemail = true;
      } else if (!this.loginData.useremail.length) {
        emaillabel.setAttribute("style", "color:#aaaaaa");
        emaillabel.innerText = "email을 입력해주세요";
        this.isemail = false;
      } else {
        emaillabel.setAttribute("style", "color:red");
        emaillabel.innerText = "email형식에 맞지 않습니다.";
        this.isemail = false;
      }
    },
    checkpassword() {
      const passwordlabel = document.querySelector("#passwordlabel");
      if (this.loginData.password.length > 8) {
        passwordlabel.setAttribute("style", "color:blue");
        passwordlabel.innerText = "비밀번호가 적절합니다.";
        this.ispassword = true;
      } else if (this.loginData.password.length === 0) {
        passwordlabel.setAttribute("style", "color:#aaaaaa");
        passwordlabel.innerText = "비밀번호를 입력해주세요.";
        this.ispassword = false;
      } else {
        passwordlabel.setAttribute("style", "color:red");
        passwordlabel.innerText = "비밀번호가 너무 짧습니다.";
        this.ispassword = false;
      }
    },
  },
  data: () => {
    return {
      constants,
      isemail: false,
      ispassword: false,
      loginData: {
        useremail: "",
        password: "",
      },
      reg: /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,24}))$/,
    };
  },
};
</script>
