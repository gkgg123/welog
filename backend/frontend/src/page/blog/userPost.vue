<template>
  <div id="detail" class="mt-5">
    <div class="post-title">
      <div class="title">
        <h2>{{ articleDetail.title }}</h2>
        <div class="written">
          <p>
            작성자 : {{ articleDetail.author }} 작성일 {{ articleDetail.title }}
          </p>
          <div class="written-tag">
            <span v-for="(tag, index) in articleDetail.tags" :key="index">{{
              tag
            }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 좋아요, 공유하기 -->
    <aside class="aside-left">
      <div class="left-side">
        <div class="floating-bar">
          <div class="like">
            <i
              class="far fa-heart"
              :class="{ likeBtn: isLikeuser, unlikeBtn: !isLikeuser }"
              @click="likePost"
            ></i>
            <p>{{ likecount }}</p>
          </div>
          <i class="fas fa-share-alt"></i>
        </div>
      </div>
    </aside>

    <!-- 앵커 -->
    <div class="aside-right">
      <div class="title-anchor">
        <div
          v-for="anchor in titles"
          :style="{ padding: `10px 0 10px ${anchor.indent * 20}px` }"
          @click="handleAnchorClick(anchor)"
          :key="anchor.lineIndex"
        >
          <a style="cursor: pointer">{{ anchor.title }}</a>
        </div>
      </div>
    </div>

    <v-md-editor
      id="detail"
      mode="preview"
      v-model="articleDetail.content"
      ref="editor"
      @copy-code-success="handleCopyCodeSuccess"
    />
    <!-- 수정 삭제 버튼 -->
    <div class="post-update">
      <button
        class="update-button"
        v-if="checkAuthorLogin"
        data-toggle="modal"
        data-target="#update"
      >
        수정
      </button>
      <button
        class="update-button"
        v-if="checkAuthorLogin"
        @click="confirmDelete"
      >
        삭제
      </button>
    </div>
    <userPostUpdate />
    <div id="context-menu" class="context-menu">
      <div class="item" data-toggle="modal" data-target="#example">
        수정요청
      </div>
      <div class="item">나가기</div>
    </div>
    <div class="commentBox">
      <div class="commentBox2">
        <div class="comment-label">
          <p>{{ commentNumber }}개의 댓글</p>
        </div>
        <div class="comment">
          <textarea
            placeholder="댓글을 남겨주세요"
            v-model="commentContents"
          ></textarea>
          <div class="comment-underbar">
            <span>비밀글</span>
            <div class="secret-button">
              <i
                class="fas fa-lock"
                v-if="isSecret"
                @click="isSecret = !isSecret"
              ></i>
              <i class="fas fa-unlock" v-else @click="isSecret = !isSecret"></i>
            </div>
            <button @click="commentSubmit">
              댓글 작성
            </button>
          </div>
        </div>
        <commentListItems />
      </div>
    </div>
    <!-- 수정요청 -->
    <div
      class="modal fade"
      id="example"
      tabindex="-1"
      role="dialog"
      aria-labelledby="exampleModalLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="exampleModalLabel">Modal title</h5>
            <button
              type="button"
              class="close"
              data-dismiss="modal"
              aria-label="Close"
            >
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">{{ confirmText }}</div>
          <div class="modal-footer">
            <button
              type="button"
              class="btn btn-secondary"
              data-dismiss="modal"
            >
              Close
            </button>
            <button type="button" class="btn btn-primary">Save changes</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import "../../assets/css/personal.scss";
import { mapState, mapActions, mapGetters } from "vuex";
import constants from "@/lib/constants.js";
import userPostUpdate from "@/page/blog/userPostUpdate.vue";
import commentListItems from "@/page/blog/commentListItems.vue";
export default {
  name: "userPost",
  data() {
    return {
      confirmText: "",
      titles: [],
      commentContents: "",
      constants,
      isSecret: false,
      isLikeuser: false,
      likecount: 0,
    };
  },
  components: {
    userPostUpdate,
    commentListItems,
  },
  computed: {
    ...mapState(["username", "authToken", "articleDetail", "commentList"]),
    ...mapGetters(["isLogined"]),
    checkAuthorLogin() {
      if (this.username === this.articleDetail.author) {
        return true;
      } else {
        return false;
      }
    },
    commentNumber() {
      return this.commentList.length;
    },
  },
  methods: {
    //  오른쪽 버튼 Custom
    contextmenu(event) {
      const creatediv = document.querySelector(".v-md-editor__preview-wrapper");
      this.confirmText = document.getSelection().toString();
      creatediv.addEventListener("contextmenu", (event) => {
        event.preventDefault();
        var contextElement = document.getElementById("context-menu");
        contextElement.style.top = event.offsetY + 450 + "px";
        contextElement.style.left = event.offsetX + 450 + "px";
        this.confirmText = document.getSelection().toString();
        contextElement.classList.add("active");
      });
      creatediv.addEventListener("click", function() {
        var contextElement = document.getElementById("context-menu");
        contextElement.classList.remove("active");
      });
    },

    ///  댓글 작성
    commentSubmit() {
      if (!this.isLogined) {
        alert("로그인을 하시고 댓글을 작성해주세요");
      } else {
        var text = this.commentContents.trim();
        var secret = "0";
        if (this.isSecret) {
          secret = "1";
        }

        if (!this.commentContents.trim().length) {
          alert("내용을 입력하세요");
        } else {
          const postData = {
            content: this.commentContents,
            secret: secret,
          };
          const totalData = {
            comment: [postData],
            token: this.authToken,
          };
          axios
            .post(
              constants.baseUrl + `post/${this.$route.params.pid}/comment/`,
              totalData
            )
            .then((res) => {
              this.$router.go();
            })
            .catch((err) => {
              console.log(err.response);
            });
        }
      }
    },
    anchorCreate() {
      const anchors = this.$refs.editor.$el.querySelectorAll(
        ".v-md-editor-preview h1,h2,h3,h4"
      );
      const titles = Array.from(anchors).filter(
        (title) => !!title.innerText.trim()
      );

      if (!titles.length) {
        this.titles = [];
        return;
      }

      const hTags = Array.from(
        new Set(titles.map((title) => title.tagName))
      ).sort();

      this.titles = titles.map((el) => ({
        title: el.innerText,
        lineIndex: el.getAttribute("data-v-md-line"),
        indent: hTags.indexOf(el.tagName),
      }));
    },

    async totalCreate() {
      await this.carryArticle(
        `post/${this.$route.params.id}/${this.$route.params.pid}/`
      );
      await this.carryComment(`post/${this.$route.params.pid}/comment/`);
      this.anchorCreate();
      this.isLike();
      this.likecount = this.articleDetail.likeCount;
    },

    // 삭제 확인
    confirmDelete() {
      if (confirm("정말 지울실껀가요?")) {
        this.deletePost();
      }
    },
    // 삭제 method

    // codeblock 복사//
    handleCopyCodeSuccess(code) {
      alert("성공적으로 복사되었습니다.");
    },

    handleAnchorClick(anchor) {
      const { editor } = this.$refs;
      const { lineIndex } = anchor;

      const heading = editor.$el.querySelector(
        `.v-md-editor-preview [data-v-md-line="${lineIndex}"]`
      );

      if (heading) {
        editor.previewScrollToTarget({
          target: heading,
          scrollContainer: window,
          top: 70,
        });
      }
    },
    // 좋아요 로직
    likePost() {
      const userData = {
        token: this.authToken,
      };
      if (!this.isLogined) {
        this.$router.push({ name: constants.URL_TYPE.USER.LOGIN });
      }
      if (this.username === this.articleDetail.author) {
        alert("자기글에 추천을 누를수없습니다.");
      } else {
        axios
          .post(
            constants.baseUrl +
              `post/${this.articleDetail.author}/${this.articleDetail.pid}/likeit/`,
            userData
          )
          .then((res) => {
            if (res.data.object.likeit) {
              this.isLikeuser = true;
              this.likecount += 1;
            } else {
              this.isLikeuser = false;
              this.likecount -= 1;
            }
          })
          .catch((err) => {
            console.log(err.response);
          });
      }
    },
    isLike() {
      const temp = Object.values(this.articleDetail.likeuserlist);
      if (temp.includes(this.username)) {
        this.isLikeuser = true;
      } else {
        this.isLikeuser = false;
      }
    },
    ...mapActions([
      "headerChange",
      "carryArticle",
      "deletePost",
      "carryComment",
    ]),
  },
  created() {
    this.headerChange(this.$route.params.id);
    window.scrollTo({ top: 0, left: 0, behavior: "auto" });
  },
  mounted() {
    this.contextmenu();
    this.totalCreate();
  },
};
</script>

<style scoped>
.likeBtn {
  color: red;
}
.unlikeBtn {
  color: black;
}
</style>
