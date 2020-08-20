<template>
  <div id="detail" class="mt-5">
    <div class="post-title" v-if="state">
      <div class="title">
        <h2>{{ articleDetail.title }}</h2>
        <div class="title-item">
          <p class="author">{{ articleDetail.author }}</p>
          <p class="written-date">
            • 작성일 {{ articleDetail.createDate.slice(0, 4) }}년
            {{ articleDetail.createDate.slice(5, 7) }}월
            {{ articleDetail.createDate.slice(8, 10) }}일
            {{articleDetail.createDate.slice(11,13)}}시
          </p>
          <div class="written-tag">
            <span v-for="(tag, index) in articleDetail.tags" :key="index">
              <router-link
                :to="{
                  name: constants.URL_TYPE.MAIN.SEARCH,
                  query: { type: 'tag', search: tag },
                }"
              >{{ tag }}</router-link>
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- 좋아요, 다운로드 -->
    <aside class="aside-left">
      <div class="left-side">
        <div class="floating-bar">
          <div class="like">
            <i
              class="fas fa-heart"
              :class="{ likeBtn: isLikeuser, unlikeBtn: !isLikeuser }"
              @click="likePost"
            ></i>
            <p>{{ likecount }}</p>
          </div>
          <div class="download" @click="markdownfile">
            <i class="fas fa-download"></i>
            <p>저장</p>
          </div>
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
      style="position:relative;"
      @copy-code-success="handleCopyCodeSuccess"
    />
    <!-- 수정 삭제 버튼 -->
    <div class="post-update">
      <button
        class="update-button"
        v-if="checkAuthorLogin"
        data-toggle="modal"
        data-target="#update"
      >수정</button>
      <button class="update-button" v-if="checkAuthorLogin" @click="confirmDelete">삭제</button>
      <userPostUpdate />
    </div>

    <div class="post-userBar">
      <div class="userBar">
        <img class="userImage" :src="articleDetail.profileUrl" alt="profileImage" />
        <div class="userIntro">
          <h2 class="box">{{ articleDetail.author }}</h2>
          <p class="box">{{ articleDetail.lineintro }}</p>
        </div>
      </div>
    </div>

    <div id="context-menu" class="context-menu">
      <div class="item" data-toggle="modal" data-target="#example" @click="closeCofirmmenu">수정요청</div>
      <div class="item" @click="copyString">복사하기</div>
      <div class="item" @click="closeCofirmmenu">나가기</div>
    </div>

    <div class="commentBox">
      <div class="commentBox2">
        <div class="comment-label">
          <p>{{ commentNumber }}개의 댓글</p>
        </div>
        <div class="comment">
          <textarea placeholder="댓글을 남겨주세요" v-model="commentContents"></textarea>
          <div class="comment-underbar">
            <span>비밀글</span>
            <div class="secret-button">
              <i class="fas fa-lock" v-if="isSecret" @click="isSecret = !isSecret"></i>
              <i class="fas fa-unlock" v-else @click="isSecret = !isSecret"></i>
            </div>
            <button @click="commentSubmit">댓글 작성</button>
          </div>
        </div>
        <commentListItems :wauthor="articleDetail.author" />
      </div>
    </div>
    <!-- 수정요청 -->
    <div
      class="modal fade request-modal"
      id="example"
      tabindex="-1"
      role="dialog"
      aria-labelledby="exampleModalLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <p class="modal-title" id="exampleModalLabel">수정요청</p>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <p>
              <i class="far fa-edit"></i> 수정 요청 내용
            </p>
            <div class="request-content">{{ confirmText }}</div>
          </div>

          <textarea name="confirmComment" id="confirmComment" v-model="confirmComment"></textarea>
          <div class="modal-footer">
            <button type="button" data-dismiss="modal" @click="modifyrequestPost">수정요청</button>
            <button type="button" data-dismiss="modal" @click="returnconfirmComment">취소</button>
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
function getPosition(e) {
  var posx = 0;
  var posy = 0;

  if (!e) var e = window.event;

  if (e.pageX || e.pageY) {
    posx = e.pageX;
    posy = e.pageY;
  } else if (e.clientX || e.clientY) {
    posx =
      e.clientX +
      document.body.scrollLeft +
      document.documentElement.scrollLeft;
    posy =
      e.clientY + document.body.scrollTop + document.documentElement.scrollTop;
  }

  return {
    x: posx,
    y: posy,
  };
}
export default {
  name: "userPost",
  data() {
    return {
      confirmText: "",
      confirmComment: "",
      titles: [],
      commentContents: "",
      constants,
      isSecret: false,
      isLikeuser: false,
      likecount: 0,
      state: false,
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
    markdownfile() {
      var sourcefile = this.articleDetail.content;
      var source =
        "data:application/vnd.ms-word;charset=utf-8," +
        encodeURIComponent(sourcefile);
      var fileDownload = document.createElement("a");
      document.body.appendChild(fileDownload);
      fileDownload.href = source;
      fileDownload.download = `${this.articleDetail.title}.md`;
      fileDownload.click();
      document.body.removeChild(fileDownload);
    },

    //  오른쪽 버튼 Custom
    contextmenu(event) {
      const creatediv = document.querySelector(".v-md-editor__preview-wrapper");
      creatediv.addEventListener("contextmenu", (event) => {
        event.preventDefault();
        var contextElement = document.getElementById("context-menu");
        var clickCoords = getPosition(event);
        var clickCoordsX = clickCoords.x;
        var clickCoordsY = clickCoords.y;
        contextElement.style.left = clickCoordsX + "px";

        contextElement.style.top = clickCoordsY + "px";

        this.confirmText = document.getSelection().toString();
        contextElement.classList.add("active");
      });
      creatediv.addEventListener("click", function () {
        var contextElement = document.getElementById("context-menu");
        contextElement.classList.remove("active");
      });
    },
    copyToClipboard(val) {
      var t = document.createElement("textarea");
      document.body.appendChild(t);
      t.value = val;
      t.select();
      document.execCommand("copy");
      document.body.removeChild(t);
    },
    copyString(event) {
      this.copyToClipboard(this.confirmText);
      alert("복사되었습니다.");
      var contextElement = document.getElementById("context-menu");
      contextElement.classList.remove("active");
    },
    closeCofirmmenu() {
      var contextElement = document.getElementById("context-menu");
      contextElement.classList.remove("active");
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
              this.totalCreate();
              alert("댓글이 작성됬습니다.");
              this.commentContents = "";
            })
            .catch((err) => {
              console.log(err.response);
            });
        }
      }
    },
    /// 수정요청 보내기 자기글에 수정요청..못 보내기 ㅋ///
    modifyrequestPost() {
      if (this.username == this.articleDetail.author) {
        alert("자기글에는 수정요청을 보낼수 없습니다.");
      } else {
        const totalData = {
          comment: this.confirmComment,
          requiredString: this.confirmText,
          postTitle: this.articleDetail.title,
          Authorization: this.authToken,
        };

        axios
          .post(
            constants.baseUrl + `modfrequest/post/${this.articleDetail.pid}`,
            totalData
          )
          .then((res) => {
            alert("수정요청이 성공적으로 보내졌습니다.");
            $("#example").modal("hide");
          })
          .catch((err) => {
            console.log(err.response);
          });
      }
    },
    returnconfirmComment() {
      this.confirmComment = "";
    },
    /// 앵커 만드는 거
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
      this.state = true;
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
          top: 90,
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
    if (this.isLogined) {
      this.contextmenu();
    }
    this.totalCreate();
  },
};
</script>

<style scoped>
.likeBtn {
  color: #f06292;
  transition: 0.3s ease;
}
.unlikeBtn {
  color: black;
  transition: 0.1s ease;
}
</style>
