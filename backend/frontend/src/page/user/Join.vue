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
      if (this.checkSingupData(signupData)) {
        axios
          .post(constants.baseUrl + "user/signup", signupData)
          .then(() => {
            this.alertAfterpush();
          })
          .catch((err) => {
            console.log(err);
          });
      }
    },
    alertAfterpush() {
      alert(
        "회원가입이 완료되었습니다. 확인을 누르시면 Login 화면으로 이동합니다."
      );
      this.$router.push({ name: constants.URL_TYPE.USER.LOGIN });
      window.close();
    },
    checkSingupData(signupData) {
      if (this.isTerm) {
        console.log(signupData);
        if (signupData.password === this.passwordConfirm) {
          if (signupData.password.length >= 8) {
            console.log(this.reg.test(signupData.useremail), this.reg);
            if (
              signupData.email !== "" &&
              this.reg.test(signupData.useremail)
            ) {
              if (signupData.username.length >= 3) {
                return true;
              } else {
                alert("닉네임을 3글자 이상 입력해주세요");
              }
            } else {
              alert("이메일 형식이 아닙니다.");
            }
          } else {
            alert("비밀번호가 너무 짧습니다.");
          }
        } else {
          alert("비밀번호와 비밀번호 확인이 서로 다릅니다.");
        }
      } else {
        alert("약관에 동의를 눌러주세요");
      }
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


