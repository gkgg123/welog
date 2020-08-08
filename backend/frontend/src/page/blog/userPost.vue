<template>
  <div id="detail" class="mt-5">
    <div class="post-title">
      <div class="title">
        <h2>제목: {{title}}</h2>
      </div>
    </div>

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
      class="col-8 mx-auto"
      style="margin:5vh 0px; height:100vh;"
      id="detail"
      mode="preview"
      v-model="text"
      ref="editor"
      @copy-code-success="handleCopyCodeSuccess"
    />
    <button
      v-if="checkAuthorLogin"
      class="btn btn-primary"
      data-toggle="modal"
      data-target="#exampleModal"
    >수정버튼입니다.</button>
    <div
      class="modal fade"
      id="exampleModal"
      tabindex="-1"
      role="dialog"
      aria-labelledby="exampleModalLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="exampleModalLabel">{{title}}</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <v-md-editor
              style="margin:5vh 0px; box-sizing: border-box;"
              id="create"
              placeholder="새 글을 작성해 보세요"
              v-model="text"
            />
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            <button type="button" class="btn btn-primary" @click="updatePost">Save changes</button>
          </div>
        </div>
      </div>
    </div>

    <button v-if="checkAuthorLogin" class="btn btn-primary" @click="confirmDelete">삭제버튼입니다.</button>

    <div id="context-menu" class="context-menu">
      <div class="item" data-toggle="modal" data-target="#exampleModal">수정요청</div>
      <div class="item">나가기</div>
    </div>

    <div class="comment">
      <textarea placeholder="댓글을 남겨주세요" v-model="commentContents"></textarea>
      <button @click="commentSubmit">작성</button>
    </div>

    <div
      class="modal fade"
      id="exampleModal"
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
export default {
  name: "userPost",
  data() {
    return {
      title: "",
      pid: null,
      author: "",
      text: "",
      tagList: [],
      confirmText: "",
      titles: [],
      commentContents: "",
      constants,
    };
  },
  computed: {
    ...mapState(["username"]),
    checkAuthorLogin() {
      if (this.username === this.author) {
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
        contextElement.style.top = event.offsetY + 270 + "px";
        contextElement.style.left = event.offsetX + 270 + "px";
        this.confirmText = document.getSelection().toString();
        contextElement.classList.add("active");
      });
      creatediv.addEventListener("click", function () {
        var contextElement = document.getElementById("context-menu");
        contextElement.classList.remove("active");
      });
    },

    updatePost() {
      const putData = {
        title: this.title,
        content: this.text,
      };
      console.log(
        "요청주소 : ",
        constants.baseUrl + `post/${this.author}/${this.pid}/`
      );
      console.log("전송데이터:", putData);
      axios
        .put(constants.baseUrl + `post/${this.author}/${this.pid}/`, putData)
        .then((res) => {
          console.log("수정결과 : ", res.data);
        });
    },

    ///  댓글 작성
    commentSubmit() {
      const postData = {
        content: this.commentContents,
      };
      console.log(this.commentContents);
      axios
        .post(
          constants.baseUrl + `post/${this.$route.params.pid}/comment/`,
          postData
        )
        .then((res) => {
          console.log(res.data);
        })
        .catch((err) => {
          console.log(err.response);
        });
    },

    /// 가져오기 ///
    carryText() {
      const author = this.$route.params.id;
      const pid = this.$route.params.pid;
      axios
        .get(constants.baseUrl + `post/${author}/${pid}/`)
        .then((res) => {
          console.log(res.data);
          this.title = res.data.object.title;
          this.text = res.data.object.content;
          this.author = res.data.object.author;
          this.pid = res.data.object.pid;
        })
        .then(() => {
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
        });
    },
    confirmDelete() {
      if (confirm("정말 지울실껀가요?")) {
        this.deletePost();
      }
    },

    deletePost() {
      axios
        .delete(constants.baseUrl + `post/${this.author}/${this.pid}`)
        .then((res) => {
          this.$router.push({
            name: constants.URL_TYPE.POST.BLOG,
            params: { id: this.author },
          });
        });
    },

    // codeblock 복사//
    handleCopyCodeSuccess(code) {
      alert("성공적으로 복사되었습니다.");
      console.log(code);
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
          top: 80,
        });
      }
    },
    ...mapActions(["headerChange"]),
  },
  created() {
    this.headerChange(this.$route.params.id);
    this.carryText();
    window.scrollTo({ top: 0, left: 0, behavior: "auto" });
  },
  mounted() {
    this.contextmenu();
  },
};
</script>

<style>
</style>
