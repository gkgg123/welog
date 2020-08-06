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

    <div id="context-menu" class="context-menu">
      <div class="item" data-toggle="modal" data-target="#exampleModal">수정요청</div>
      <div class="item">나가기</div>
    </div>
    <div class="comment">
      <textarea placeholder="댓글을 남겨주세요" name id></textarea>
      <button>작성</button>
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
import { mapActions } from "vuex";
import constants from "@/lib/constants.js";
export default {
  name: "userPost",
  data() {
    return {
      title: "",
      text: "",
      tagList: [],
      confirmText: "",
      titles: [],
      commentContents: "",
      constants,
    };
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

    ///  댓글 작성
    commentSubmit() {
      const commentContent = this.commentContents;
      axios.post(constants.baseUrl + `post/${pid}/comment/create/`);
    },

    /// 가져오기 ///
    carryText() {
      const author = this.$route.params.id;
      const pid = this.$route.params.pid;
      axios
        .get(constants.baseUrl + `post/${author}/${pid}/`)
        .then((res) => {
          this.title = res.data.object.title;
          this.text = res.data.object.content;
        })
        .then(() => {
          const anchors = this.$refs.editor.$el.querySelectorAll(
            ".v-md-editor-preview h1,h2,h3,h4,h5,h6"
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
