<template>
  <div class="profile-contents">
    <div class="user-info">
      <div class="user-img">
        <img src="https://cdn0.iconfinder.com/data/icons/set-ui-app-android/32/8-512.png" alt />
        <div class="filebox">
          <label for="ex_file">이미지 수정</label>
          <input type="file" id="ex_file" ref="profileimg" @change="updateprofile" />
        </div>
        <button class="button-delete" @click="deleteprofile">이미지 삭제</button>
      </div>
      <div class="user-intro">
        <div class="user-name">{{username}}</div>
        <textarea class="intro-content" v-model="lineintro"></textarea>
        <button @click="updateDescription">수정</button>
      </div>
    </div>
    <div class="user-active">
      <div class="user-award">
        <img src="../../../../public/img/trophy.png" alt />
        <div>나의 활동 내역</div>
      </div>
      <div class="active-item1">
        <i class="fas fa-pencil-alt"></i>
        내가 작성한 포스트 수
        <span>1</span>
      </div>
      <div class="active-item2">
        <i class="fas fa-heart"></i>내가 받은 좋아요 수
        <span>1</span>
      </div>
      <div class="active-item3">
        <i class="far fa-clipboard"></i>내가 좋아요 한 포스트 수
        <span>1</span>
      </div>
      <div class="active-item4">
        <img src="../../../../public/img/heart-icon.png" alt /> 가장 많은 좋아요 수
        <span>1</span>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import axios from "axios";
import constants from "@/lib/constants.js";
export default {
  name: "UserProfileConfig",
  computed: {
    ...mapState(["username"]),
  },
  methods: {
    updateprofile(event) {
      const formData = new FormData();
      formData.append("files", event.target.files[0]);
      axios
        .post(constants.baseUrl + `user/${this.username}/profile`, formData)
        .then((res) => {
          console.log(res);
        })
        .catch((err) => {
          console.log(err.response);
        });
    },
    deleteprofile() {
      axios
        .delete(constants.baseUrl + `user/${this.username}/profile`)
        .then((res) => {
          console.log(res);
        })
        .catch((err) => {
          console.log(err.response);
        });
    },
    updateDescription() {
      const description = {
        description: this.lineintro,
      };
      axios
        .post(constants.baseUrl + `user/${this.username}/`)
        .then((res) => {
          console.log(res.data);
        })
        .catch((err) => {
          console.log(err.response);
        });
    },
  },
  data() {
    return {
      constants,
      lineintro: "",
    };
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
