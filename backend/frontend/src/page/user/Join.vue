<template>
  <div class="user" id="join">
    <div class="wrapC table">
      <div class="middle">
        <h1>회원가입</h1>
        <div class="form-wrap">
          <div class="input-wrap">
            <input v-model="nickName" id="nickname" placeholder="닉네임을 입력해주세요" type="text" />
          </div>

          <div class="input-wrap">
            <input v-model="email" id="email" placeholder="이메일을 입력해주세요" type="text" />
          </div>

          <div class="input-wrap password-wrap">
            <input v-model="password" id="password" :type="passwordType" placeholder="비밀번호를 입력해주세요" />
            <span :class="{active : passwordType==='text'}">
              <i class="fas fa-eye"></i>
            </span>
          </div>

          <div class="input-wrap password-wrap">
            <input
              v-model="passwordConfirm"
              id="password-confirm"
              :type="passwordConfirmType"
              placeholder="비밀번호를 한번 더 입력해주세요"
            />
            <span :class="{active : passwordConfirmType==='text'}">
              <i class="fas fa-eye"></i>
            </span>
          </div>
        </div>

        <label>
          <input v-model="isTerm" type="checkbox" id="term" />
          <span>약관에 동의합니다</span>
        </label>

        <span class="go-term">약관 보기</span>

        <button class="btn" @click="signup">
          <span>작성완료</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import "../../assets/css/user.scss";
import constants from "../../lib/constants";
import axios from "axios";
export default {
  components: {},
  created() {},
  methods: {
    signup() {
      const signupData = {
        useremail: this.email,
        username: this.nickName,
        password: this.password,
      };
      console.log(signupData);
      axios
        .post(constants.baseUrl + "user/signup", signupData)
        .then((res) => {
          console.log(res);
        })
        .catch((err) => {
          console.log("여기냐?");
          console.log(err);
        });

      // if (this.isTerm) {
      //   if (this.password === this.passwordConfirm) {
      //     if (this.password.length >= 8) {
      //       if (this.email !== "" && this.reg.test(this.email)) {
      //         const signupData = {
      //           email: this.email,
      //           password1: this.password,
      //           password2: this.passwordConfirm,
      //           nickname: this.nickName,
      //         };
      //         axios
      //           .post(constants.baseUrl + "rest-auth/signup/", signupData)
      //           .then((res) => {
      //             alert("인증 이메일을 보냈습니다. 확인해주세요.");
      //             this.$router.push({ name: constants.URL_TYPE.POST.MAIN });
      //           })
      //           .catch((err) => {
      //             if (err.response.data.hasOwnProperty("password1")) {
      //               alert(err.response.data.password1[0]);
      //             } else if (err.response.data.hasOwnProperty("nickname")) {
      //               alert("닉네임이 중복됩니다. 다른 닉네임을 입력해주세요");
      //             } else if (err.response.data.hasOwnProperty("email")) {
      //               alert("이메일이 중복됩니다. 다른 이메일을 작성해주세요.");
      //             }
      //           });
      //       } else {
      //         alert("이메일 형식이 아닙니다.");
      //       }
      //     } else {
      //       alert("비밀번호가 너무 짧습니다.");
      //     }
      //   } else {
      //     alert("비밀번호와 비밀번호 확인이 서로 다릅니다.");
      //   }
      // } else {
      //   alert("약관에 동의를 눌러주세요");
      // }
    },
  },
  watch: {},
  data: () => {
    return {
      constants,
      email: "",
      nickName: "",
      password: "",
      passwordConfirm: "",
      isTerm: false,
      passwordType: "password",
      passwordConfirmType: "password",
      reg: /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,24}))$/,
    };
  },
};
</script>


