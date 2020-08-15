<template>
  <div>
    <div class="comment-list" v-if="checkSecret">
      <div class="comment-profile">
        <div class="profile-img">
          <i class="far fa-smile"></i>
        </div>
        <div class="writer-info">
          <p class="writer-id">{{ comment.name }}</p>
          <p class="written-day">작성 날짜</p>
        </div>
        <div class="comment-button" v-if="checkAuthorLogin">
          <p @click="popUp">수정</p>
          <p @click="deleteComment">삭제</p>
        </div>
      </div>
      <div class="comment-content">{{ comment.content }}</div>

      <div class="comment-update">
        <textarea
          :id="'commentUpdate' + comment.cid"
          class="commentUpdate"
          name="commentUpdate"
          v-model="cofirmComment"
        ></textarea>
        <div
          class="secretcheckboxclose"
          id="isSecret"
          :data-checkbox="comment.cid"
        >
          <i
            class="fas fa-lock"
            v-if="isSecret"
            id="isSecret"
            :data-checkbox="comment.cid"
            @click="isSecret = !isSecret"
          ></i>
          <i
            class="fas fa-unlock"
            v-else
            id="isSecret"
            :data-checkbox="comment.cid"
            @click="isSecret = !isSecret"
          ></i>
        </div>
        <button
          class="btn close closedisplay"
          :data-set="comment.cid"
          @click="confirmCMT"
        >
          수정
        </button>
        <button
          class="btn close closedisplay"
          :data-set="comment.cid"
          @click="closepop"
        >
          취소
        </button>
      </div>
    </div>
    <div v-if="!checkSecret">
      ---------------------------- &nbsp;&nbsp;&nbsp;비밀댓글입니다.
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
  created() {
    this.isSecret = !!this.comment.secret;
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
      setTimeout(this.popUpCheckbox, 0);
    },
    closepop() {
      const updateBtn = document.querySelector(
        "#" + "commentUpdate" + this.comment.cid
      );
      const Btns = document.querySelectorAll(
        `[data-set="${this.comment.cid}"]`
      );
      setTimeout(function() {
        Btns.forEach((Btn) => {
          Btn.classList.add("close");
          Btn.classList.remove("open");
        });
      }, 0);
      setTimeout(function() {
        Btns.forEach((Btn) => {
          Btn.classList.add("closedisplay");
        });
      }, 0);
      updateBtn.classList.remove("popupComment");
      updateBtn.classList.add("commentUpdate");
      this.isSecret = !!this.comment.secret;
      this.closeCheckbox();
    },
    confirmCMT() {
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
      if (confirm("정말 삭제하시겠습니까?")) {
        axios
          .delete(
            constants.baseUrl +
              `post/${this.$route.params.pid}/comment/${this.comment.cid}/`,
            {
              data: {
                token: this.authToken,
              },
            }
          )
          .then((res) => {
            this.$router.go();
          })
          .catch((err) => {
            console.log(err.response);
          });
      }
    },
  },
};
</script>

<style scoped>
.commentUpdate {
  display: none;
}
.popupComment {
  display: block;
  border-radius: 7px;
  padding: 5px 8px;
  margin-bottom: 15px;
  background-color: #f5f5f6;
  height: 100px;
}
.close {
  opacity: 0;
  height: 0px;
}
.closedisplay {
  display: none;
}
.open {
  float: right;
  font-size: 17px;
  background-color: #48a999;
  color: white;
  padding: 5px 8px;
  margin-left: 15px;
  border: none;
  border-radius: 7px;
}
.secretcheckboxclose {
  display: none;
}
.secretcheckboxopen {
  display: inline;
}
.fa-lock {
  cursor: pointer;
  margin-left: 81%;
  font-size: 32px;
}
.fa-unlock {
  cursor: pointer;
  margin-left: 81%;
  font-size: 32px;
  color: #aaaa;
}
</style>
