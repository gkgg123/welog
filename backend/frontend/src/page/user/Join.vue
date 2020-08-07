<template>
  <div class="user" id="join">
    <div class="wrapC table">
      <img class="signup-image" src="img\signup.png" alt />
      <div class="middle">
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
          <input
            v-model="isTerm"
            data-toggle="modal"
            data-target="#staticBackdrop"
            type="checkbox"
            id="term"
          />
          <span class="term-span">약관에 동의합니다</span>
        </label>

        <span class="go-term" data-toggle="modal" data-target="#staticBackdrop">약관 보기</span>
        <div
          class="modal fade"
          id="staticBackdrop"
          data-backdrop="static"
          data-keyboard="false"
          tabindex="-1"
          role="dialog"
          aria-labelledby="staticBackdropLabel"
          aria-hidden="true"
        >
          <div class="modal-dialog modal-dialog-scrollable">
            <div class="modal-content">
              <div class="modal-header">
                <h3 class="modal-title" id="staticBackdropLabel">이용약관</h3>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div class="modal-body">
                <TermsOfUse />
              </div>
              <div class="modal-footer">
                <button
                  type="button"
                  class="modal-btn-secondary rounded-lg"
                  data-dismiss="modal"
                  v-on:click="isTerm=true"
                >동의함</button>
                <button
                  type="button"
                  class="modal-btn-primary rounded-lg"
                  data-dismiss="modal"
                  v-on:click="isTerm=false"
                >동의안함</button>
              </div>
            </div>
          </div>
        </div>
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
import TermsOfUse from "@/page/user/TermsOfUse";
export default {
  components: { TermsOfUse },
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


