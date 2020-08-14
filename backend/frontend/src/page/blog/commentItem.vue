<template>
  <div>
    <div class="comment-list" v-if="checkSecret">
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
      <div class="comment-update" v-if="checkAuthorLogin">
        <button class="btn" @click="popUp">수정</button>
        <button class="btn" @click="deleteComment">삭제</button>
      </div>
      <div>
        <textarea
          :id="'commentUpdate'+ comment.cid"
          class="border commentUpdate"
          name="commentUpdate"
          cols="100"
          rows="3"
          v-model="cofirmComment"
        ></textarea>
        <div class="comment-update-confirm">
          <div>
            <label class="secretcheckboxclose" :data-checkbox="comment.cid" for="isSecret">비밀댓글</label>
            <input
              class="secretcheckboxclose"
              type="checkbox"
              name="isSecret"
              id="isSecret"
              :data-checkbox="comment.cid"
              v-model="isSecret"
            />
          </div>
          <button class="btn close closedisplay" :data-set="comment.cid" @click="confirm">수정완료</button>
          <button class="btn close closedisplay" :data-set="comment.cid" @click="closepop">닫기</button>
        </div>
      </div>
    </div>
    <div v-if="!checkSecret">
      ----------------------------
      비밀댓글입니다.
      -----------------------------
    </div>
  </div>
</template>

<script>
import axios from "axios";
import constants from "@/lib/constants.js";
import { mapState } from "vuex";
export default {
  name: "commentItem",
  data() {
    return {
      cofirmComment: "",
      isSecret: false,
      constants,
    };
  },
  props: {
    comment: Object,
  },
  computed: {
    ...mapState(["username", "articleDetail", "authToken"]),
    checkAuthorLogin() {
      if (this.username === this.comment.name) {
        return true;
      } else {
        return false;
      }
    },
    checkSecret() {
      if (this.comment.secret === 1) {
        if (this.username === this.comment.name) {
          return true;
        }
        if (this.uesername === this.articleDetail.author) {
          return true;
        }
        return false;
      } else {
        return true;
      }
    },
  },
  methods: {
    popUpCheckbox() {
      const checkboxs = document.querySelectorAll(
        `[data-checkbox="${this.comment.cid}"`
      );
      checkboxs.forEach((checkbox) => {
        checkbox.classList.remove("secretcheckboxclose");
        checkbox.classList.add("secretcheckboxopen");
      });
    },
    closeCheckbox() {
      const checkboxs = document.querySelectorAll(
        `[data-checkbox="${this.comment.cid}"`
      );
      checkboxs.forEach((checkbox) => {
        checkbox.classList.add("secretcheckboxclose");
        checkbox.classList.remove("secretcheckboxopen");
      });
    },
    popUp() {
      const updateBtn = document.querySelector(
        "#" + "commentUpdate" + this.comment.cid
      );
      const Btns = document.querySelectorAll(
        `[data-set="${this.comment.cid}"]`
      );

      Btns.forEach((Btn) => {
        Btn.classList.add("open");
        Btn.classList.remove("close");
        Btn.classList.remove("closedisplay");
      });
      updateBtn.classList.remove("commentUpdate");
      updateBtn.classList.add("popupComment");
      this.cofirmComment = this.comment.content;
      setTimeout(this.popUpCheckbox, 500);
    },
    closepop() {
      const updateBtn = document.querySelector(
        "#" + "commentUpdate" + this.comment.cid
      );
      const Btns = document.querySelectorAll(
        `[data-set="${this.comment.cid}"]`
      );
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
      this.closeCheckbox();
    },
    confirm() {
      var secret = "0";
      if (this.isSecret) {
        secret = "1";
      }

      if (!this.cofirmComment.trim().length) {
        alert("내용을 입력하세요");
      } else {
        const postData = {
          content: this.cofirmComment,
          secret: secret,
        };
        const totalData = {
          comment: [postData],
          token: this.authToken,
        };
        console.log(totalData);
        axios
          .put(
            constants.baseUrl +
              `post/${this.$route.params.pid}/comment/${this.comment.cid}/`,
            totalData
          )
          .then((res) => {
            this.$router.go();
          })
          .catch((err) => {
            console.log("댓글 수정실패");
          });
      }
    },
    deleteComment() {
      axios
        .delete(
          constants.baseUrl +
            `post/${this.$route.params.pid}/comment/${this.comment.cid}/`
        )
        .then((res) => {
          console.log(res);
        });
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
  height: 100px;
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
.secretcheckboxclose {
  display: none;
}
.secretcheckboxopen {
  display: inline;
}
</style>