<template>
  <div class="user" id="join">
    <div class="wrapC table">
      <img class="signup-image" src="img\signup.png" alt />
      <div class="middle">
        <div class="form-wrap">
          <div class="input-id">
            <input v-model="nickName" id="nickname" placeholder="닉네임을 입력해주세요" type="text" />
            <button class="check-nickname" @click="checkNickname" v-if="!ninknameoverlapcheck">중복확인</button>
            <button class="reset-nickname" @click="resetNickname" v-else>닉네임 변경</button>
            <p>* 닉네임 설정 후 변경불가</p>
          </div>

          <div class="input-wrap">
            <input v-model="email" id="email" placeholder="이메일을 입력해주세요" type="text" />
            <button class="check-email" @click="checkNickname" v-if="!ninknameoverlapcheck">중복확인</button>
            <button class="reset-email" @click="resetNickname" v-else>이메일 변경</button>
          </div>

          <div class="input-wrap password-wrap">
            <input v-model="password" id="password" :type="passwordType" placeholder="비밀번호를 입력해주세요" />
            <span :class="{ active: passwordType === 'text' }">
              <i class="fas fa-eye" @click="viewpassword"></i>
            </span>
          </div>

          <div class="input-wrap password-wrap">
            <input
              v-model="passwordConfirm"
              id="password-confirm"
              :type="passwordConfirmType"
              placeholder="비밀번호를 한번 더 입력해주세요"
            />
            <span :class="{ active: passwordConfirmType === 'text' }">
              <i class="fas fa-eye" @click="viewpasswordConfirm"></i>
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
                  v-on:click="isTerm = true"
                >동의함</button>
                <button
                  type="button"
                  class="modal-btn-primary rounded-lg"
                  data-dismiss="modal"
                  v-on:click="isTerm = false"
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
  nama: "Join",
  components: { TermsOfUse },
  created() {},
  methods: {
    signup() {
      const signupData = {
        useremail: this.email,
        username: this.nickName,
        password: this.password,
      };
      if (this.checkSingupData(signupData)) {
        if (this.ninknameoverlapcheck) {
          axios
            .post(constants.baseUrl + "user/signup", signupData)
            .then(() => {
              this.alertAfterpush();
            })
            .catch((err) => {
              console.log(err);
            });
        } else {
          alert("닉네임 중복체크를 해주세요");
        }
      }
    },
    alertAfterpush() {
      alert(
        "회원가입이 완료되었습니다. 확인을 누르시면 Login 화면으로 이동합니다."
      );
      this.$router.push({ name: constants.URL_TYPE.USER.LOGIN });
      window.close();
    },
    checkNickname(event) {
      if (!this.nickName.trim().length) {
        alert("공백을 입력하시면 안됩니다.");
      } else {
        axios
          .get(constants.baseUrl + "user/check/nickname", {
            params: {
              nickname: this.nickName,
            },
          })
          .then((res) => {
            if (res.data === false) {
              alert("닉네임이 중복됩니다.");
            } else {
              this.setnickname = this.nickName;
              this.ninknameoverlapcheck = true;
              const inputNickname = document.querySelector("#nickname");
              inputNickname.setAttribute("disabled", true);
              alert("사용할수 있는 닉네임입니다.");
            }
          });
      }
    },
    resetNickname() {
      this.setnickname = "";
      this.ninknameoverlapcheck = false;
      const inputNickname = document.querySelector("#nickname");
      inputNickname.removeAttribute("disabled");
      inputNickname.setAttribute("style", "background-color:white");
    },

    /// 비밀번호 표시
    viewpassword(event) {
      if (this.passwordType == "text") {
        this.passwordType = "password";
        event.target.classList.add("fa-eye");
        event.target.classList.remove("fa-eye-slash");
      } else {
        this.passwordType = "text";
        event.target.classList.add("fa-eye-slash");
        event.target.classList.remove("fa-eye");
      }
    },
    viewpasswordConfirm(event) {
      if (this.passwordConfirmType == "text") {
        this.passwordConfirmType = "password";
        event.target.classList.add("fa-eye");
        event.target.classList.remove("fa-eye-slash");
      } else {
        this.passwordConfirmType = "text";
        event.target.classList.add("fa-eye-slash");
        event.target.classList.remove("fa-eye");
      }
    },
    checkSingupData(signupData) {
      if (this.isTerm) {
        if (signupData.password === this.passwordConfirm) {
          if (signupData.password.length >= 8) {
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
      setnickname: "",
      ninknameoverlapcheck: false,
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
