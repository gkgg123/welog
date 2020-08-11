<template>
  <div class="comment-list">
    <div class="comment-profile">
      <div class="profile-img">
        <i class="far fa-smile"></i>
      </div>
      <div class="writer-info">
        <p class="writer-id">작성자 아이디 {{comment.name}}</p>
        <p class="written-day">작성 날짜</p>
      </div>
    </div>
    <div class="comment-content">{{comment.content}}</div>
    <div v-if="checkAuthorLogin">
      <button class="btn" @click="popUp">수정</button>
      <button class="btn">삭제</button>
    </div>
    <div>
      <textarea
        :id="'commentUpdate'+ comment.cid"
        class="border commentUpdate"
        name="commentUpdate"
        cols="30"
        rows="10"
      ></textarea>
      <button class="btn close closedisplay" @click="closepop">수정완료</button>
      <button class="btn close closedisplay" @click="closepop">닫기</button>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
export default {
  name: "commentItem",
  props: {
    comment: Object,
  },
  computed: {
    ...mapState(["username"]),
    checkAuthorLogin() {
      if (this.username === this.comment.name) {
        return true;
      } else {
        return false;
      }
    },
  },
  methods: {
    popUp() {
      const updateBtn = document.querySelector(
        "#" + "commentUpdate" + this.comment.cid
      );
      const Btns = document.querySelectorAll(".close");
      Btns.forEach((Btn) => {
        Btn.classList.add("open");
        Btn.classList.remove("close");
        Btn.classList.remove("closedisplay");
      });
      console.log(updateBtn);
      updateBtn.classList.remove("commentUpdate");
      updateBtn.classList.add("popupComment");
    },
    closepop() {
      const updateBtn = document.querySelector(
        "#" + "commentUpdate" + this.comment.cid
      );
      const Btns = document.querySelectorAll(".open");
      setTimeout(function () {
        Btns.forEach((Btn) => {
          Btn.classList.add("close");
          Btn.classList.remove("open");
        });
      }, 300);
      setTimeout(function () {
        Btns.forEach((Btn) => {
          Btn.classList.add("closedisplay");
        });
      }, 200);
      updateBtn.classList.remove("popupComment");
      updateBtn.classList.add("commentUpdate");
    },
  },
};
</script>

<style scoped>
.commentUpdate {
  height: 0px;
  -webkit-transition: height 1s;

  transition: height 1s;
}
.popupComment {
  display: block;
  height: 300px;
  -webkit-transition: height 1s;

  transition: height 1s;
}
.close {
  opacity: 0;
  height: 0px;
  transition: height 0.5s;
}
.closedisplay {
  display: none;
}
.open {
  display: flex;
  height: 30px;
  opacity: 1;
  transition: opacity 0.5s;
}
</style>