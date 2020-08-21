<template>
  <div class="profile-contents">
    <div class="user-info">
      <div class="user-img">
        <img :src="userprofile" alt />
        <div class="filebox">
          <label for="ex_file">이미지 수정</label>
          <input type="file" id="ex_file" ref="profileimg" @change="updateprofile" />
        </div>
        <button class="button-delete" @click="checkdeleteprofile">이미지 삭제</button>
      </div>
      <div class="user-intro">
        <div class="user-name">{{username}}</div>
        <textarea class="intro-content" v-model="lineintro"></textarea>
        <button @click="updateDescription">수정</button>
        <div class="password-update">
          <div>비밀번호 변경</div>
          <p
            class="password-button"
            type="button"
            data-toggle="modal"
            data-target="#staticBackdrop"
          >변경하기</p>
        </div>
      </div>
    </div>
    <div
      class="modal fade"
      id="staticBackdrop"
      data-backdrop="static"
      data-keyboard="false"
      tabindex="-1"
      aria-labelledby="staticBackdropLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <p class="modal-title" id="staticBackdropLabel">비밀번호 변경</p>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <div class="input-label">
              <label for="password-now">현재 비밀번호</label>
              <input
                class="label-input"
                id="password-now"
                v-model="currentPassword"
                type="password"
              />
            </div>
            <div class="input-label">
              <label for="password-new1">새 비밀번호</label>
              <input
                class="label-input"
                id="password-new1"
                @keyup="checkpassword1"
                v-model="newPassword1"
                type="password"
              />
              <span id="alertmessage1"></span>
            </div>
            <div class="input-label">
              <label for="password-new2">새 비밀번호 확인</label>
              <input
                class="label-input"
                id="password-new2"
                v-model="newPassword2"
                @keyup="checkpassword2"
                type="password"
              />
              <span id="alertmessage2"></span>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" @click="checkupdatePassword">변경</button>
            <button type="button" data-dismiss="modal">취소</button>
          </div>
        </div>
      </div>
    </div>
    <div class="user-active">
      <div class="user-award">
        <img src="img/trophy.png" alt />
        <div>나의 활동 내역</div>
      </div>
      <div class="active-item1">
        <i class="fas fa-pencil-alt"></i>
        내가 작성한 포스트 수
        <span>{{getAchivementData.postCount}}</span>
      </div>
      <div class="active-item2">
        <i class="fas fa-heart"></i>내가 받은 좋아요 수
        <span>{{getAchivementData.likemeCount}}</span>
      </div>
      <div class="active-item3">
        <i class="far fa-clipboard"></i>내가 좋아요 한 포스트 수
        <span>{{getAchivementData.likeCount}}</span>
      </div>
      <div class="active-item4">
        <img src="img/heart-icon.png" alt /> 가장 많은 좋아요 수
        <span>{{getAchivementData.mostLike}}</span>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapMutations } from "vuex";
import axios from "axios";
import constants from "@/lib/constants.js";
export default {
  name: "UserProfileConfig",
  computed: {
    ...mapState([
      "username",
      "userintro",
      "userprofile",
      "defalutprofileimg",
      "userintro",
      "authToken",
      "s3url",
    ]),
  },
  methods: {
    ...mapMutations(["SET_USERPROFILE", "SET_USERDESCRIPTION"]),
    setIntro() {
      this.lineintro = this.userintro;
    },
    checkupdatePassword() {
      if (confirm("정말 비밀번호를 변경하시겠습니까?")) {
        this.updatePassword();
      }
    },
    checkpassword1() {
      const alertmessage = document.querySelector("#alertmessage1");
      if (this.newPassword1 == this.currentPassword) {
        alertmessage.setAttribute("style", "color:red");
        alertmessage.innerText = "기존 비밀번호와 같습니다.";
        this.ispasswordcheck1 = false;
      } else if (this.newPassword1.length < 8) {
        alertmessage.setAttribute("style", "color:red");
        alertmessage.innerText = "비밀번호가 너무 짧습니다.";
        this.ispasswordcheck1 = false;
      } else {
        alertmessage.setAttribute("style", "color:blue");
        alertmessage.innerText = "가능한 비밀번호입니다.";
        this.ispasswordcheck1 = true;
      }
    },
    checkpassword2() {
      const alertmessage = document.querySelector("#alertmessage2");
      if (this.newPassword1 == this.newPassword2) {
        alertmessage.setAttribute("style", "color:blue");
        alertmessage.innerText = "비밀번호가 같습니다.";
        this.ispasswordcheck2 = true;
      } else {
        alertmessage.setAttribute("style", "color:red");
        alertmessage.innerText = "비밀번호가 다릅니다.";
        this.ispasswordcheck2 = false;
      }
    },
    updatePassword() {
      const totalData = {
        originalPassword: this.currentPassword,
        changePassword: this.newPassword1,
      };
      if (this.ispasswordcheck1 && this.ispasswordcheck2) {
        axios
          .put(constants.baseUrl + `user/pwchange`, totalData, {
            headers: {
              Authorization: this.authToken,
            },
          })
          .then((res) => {
            alert("Password가 정상적으로 변경되었습니다.");
            $("#staticBackdrop").modal("hide");
          })
          .catch((err) => {
            alert("현재비밀번호가 다릅니다.");
          });
      } else {
        alert("새 비밀번호와 비밀번호 확인이 다르거나 너무 짧습니다.");
      }
    },
    updateprofile(event) {
      const formData = new FormData();
      formData.append("files", event.target.files[0]);
      axios
        .post(constants.baseUrl + `user/${this.username}/profile`, formData)
        .then((res) => {
          var receiveimg = res.data.object.path.replace(
            this.s3url,
            constants.imageUrl
          );
          alert("프로필이 수정되었습니다.");
          this.SET_USERPROFILE(receiveimg);
        })
        .catch((err) => {
          alert("프로필 수정에 실패했습니다.");
        });
    },
    checkdeleteprofile() {
      if (confirm("프로필 사진을 정말 초기화 시키겠습니까?")) {
        this.deleteprofile();
      }
    },
    deleteprofile() {
      axios
        .delete(constants.baseUrl + `user/${this.username}/profile`)
        .then((res) => {
          this.SET_USERPROFILE(this.defalutprofileimg);
        })
        .catch((err) => {
          alert("프로필삭제에 실패했습니다.");
        });
    },
    updateDescription() {
      const description = {
        description: this.lineintro,
      };
      axios
        .post(constants.baseUrl + `user/${this.username}/description`, {
          description: this.lineintro,
        })
        .then((res) => {
          this.lineintro = res.data.object.userDescription;
          alert("한줄소개가 수정되었습니다.");
          this.SET_USERDESCRIPTION(res.data.object.userDescription);
        })
        .catch((err) => {
          alert("한줄소개 수정에 실패했습니다.");
        });
    },
    getAchivement() {
      axios
        .get(constants.baseUrl + `post/${this.username}/achivement`)
        .then((res) => {
          this.getAchivementData = res.data;
        });
    },
  },
  data() {
    return {
      constants,
      lineintro: "",
      currentPassword: "",
      newPassword1: "",
      newPassword2: "",
      getAchivementData: "",
      ispasswordcheck1: false,
      ispasswordcheck2: false,
    };
  },
  mounted() {
    this.setIntro();
  },
  created() {
    this.getAchivement();
  },
};
</script>

<style scoped>
.filebox label {
  display: inline-block;
  padding: 0.5em 0.75em;
  color: #999;
  font-size: inherit;
  line-height: normal;
  vertical-align: middle;
  background-color: #fdfdfd;
  cursor: pointer;
  border: 1px solid #ebebeb;
  border-bottom-color: #e2e2e2;
  border-radius: 0.25em;
}
.filebox input[type="file"] {
  /* 파일 필드 숨기기 */
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  border: 0;
}
</style>
