<template>
  <div id="detail" class="mt-5">
    <div class="post-title">
      <div class="title">
        <h2>{{articleDetail.title}}</h2>
        <div class="written">
          <p>작성자{{articleDetail.title}} 작성일 {{articleDetail.title}}</p>
          <div class="written-tag">
            <span>태그</span>
            <span>태그</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 좋아요, 공유하기 -->
    <aside class="aside-left">
      <div class="left-side">
        <div class="floating-bar">
          <div class="like">
            <i class="far fa-heart"></i>
            <p>like</p>
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

    <!-- 수정 삭제 버튼 -->
    <div class="post-update">
      <v-md-editor
        id="detail"
        mode="preview"
        v-model="articleDetail.content"
        ref="editor"
        @copy-code-success="handleCopyCodeSuccess"
      />
      <button
        v-if="checkAuthorLogin"
        class="btn btn-primary"
        data-toggle="modal"
        data-target="#update"
      >수정버튼입니다.</button>

      <userPostUpdate />
      <button v-if="checkAuthorLogin" class="btn btn-primary" @click="confirmDelete">삭제버튼입니다.</button>
    </div>
    <div id="context-menu" class="context-menu">
      <div class="item" data-toggle="modal" data-target="#example">수정요청</div>
      <div class="item">나가기</div>
    </div>

    <!-- 댓글 -->
    <div class="comment">
      <div class="comment-label">
        <p>0개의 댓글</p>
      </div>
      <div class="comment-input">
        <textarea placeholder="댓글을 남겨주세요" v-model="commentContents"></textarea>
        <button @click="commentSubmit">댓글 작성</button>
      </div>
      <commentListItems />
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
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">{{ confirmText }}</div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
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
import { mapState, mapActions } from "vuex";
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
    };
  },
  components: {
    userPostUpdate,
    commentListItems,
  },
  computed: {
    ...mapState(["username", "authToken", "articleDetail"]),
    checkAuthorLogin() {
      if (this.username === this.articleDetail.author) {
        return true;
      } else {
        return false;
      }
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
      creatediv.addEventListener("click", function () {
        var contextElement = document.getElementById("context-menu");
        contextElement.classList.remove("active");
      });
    },

    ///  댓글 작성
    commentSubmit() {
      const postData = {
        content: this.commentContents,
        secret: "0",
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
          console.log(res.data);
        })
        .catch((err) => {
          console.log(err.response);
        });
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
    },

    // 삭제 확인
    confirmDelete() {
      if (confirm("정말 지울실껀가요?")) {
        this.deletePost();
      }
    },
    // 삭제 method
    deletePost() {
      if (this.checkAuthorLogin) {
        axios
          .delete(constants.baseUrl + `post/${this.author}/${this.pid}`)
          .then((res) => {
            this.$router.push({
              name: constants.URL_TYPE.POST.BLOG,
              params: { id: this.author },
            });
          });
      }
    },
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
    ...mapActions(["headerChange", "carryArticle", "carryComment"]),
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

<style>
</style>
