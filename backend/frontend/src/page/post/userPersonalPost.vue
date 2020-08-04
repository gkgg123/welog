<template>
  <div class="mt-5">
    <h3>제목: {{ title }}</h3>
    <aside class="aside-left">왼쪽입니다.</aside>
    <div class="aside-right">
      오른쪽입니다.
      <div
        v-for="anchor in titles"
        :style="{ padding: `10px 0 10px ${anchor.indent * 20}px` }"
        @click="handleAnchorClick(anchor)"
        :key="anchor.lineIndex"
      >
        <a style="cursor: pointer">{{ anchor.title }}</a>
      </div>
    </div>
    <v-md-editor
      class="border border-primary col-8 mx-auto"
      style="margin:5vh 0px; height:300vh;"
      id="detail"
      mode="preview"
      v-model="text"
      ref="editor"
    />

    <div id="context-menu" class="context-menu">
      <div class="item" data-toggle="modal" data-target="#exampleModal">
        수정요청
      </div>
      <div class="item">나가기</div>
    </div>

    <textarea
      class="border border-primary"
      placeholder="댓글을 남겨주세요"
      name=""
      id=""
      cols="30"
      rows="10"
    >
    </textarea>

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
            <button
              type="button"
              class="close"
              data-dismiss="modal"
              aria-label="Close"
            >
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            {{ confirmText }}
          </div>
          <div class="modal-footer">
            <button
              type="button"
              class="btn btn-secondary"
              data-dismiss="modal"
            >
              Close
            </button>
            <button type="button" class="btn btn-primary">
              Save changes
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "userPersonalPost",
  data() {
    return {
      title: "",
      text: "",
      tagList: [],
      confirmText: "",
      titles: [],
      commentContents: "",
    };
  },
  methods: {
    headerChange() {
      const urlname = this.$route.params.id;
      this.$store.commit("SET_header", urlname);
      this.$store.commit("SET_headerPath", "@" + urlname);
    },
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
      creatediv.addEventListener("click", function() {
        var contextElement = document.getElementById("context-menu");
        contextElement.classList.remove("active");
      });
    },

    ///  댓글 작성
    commentSubmit() {
      const commentContent = this.commentContents;
      axios.post("http://localhost:8080/" + `post/${pid}/comment/create/`);
    },

    /// 가져오기 ///
    carryText() {
      const author = this.$route.params.id;
      const pid = this.$route.params.pid;
      console.log(author, pid);
      axios
        .get("http://localhost:8080/" + `post/${author}/${pid}/`)
        .then((res) => {
          console.log(res.data, "안녕하세요");
          this.title = res.data.title;
          this.text = res.data.content;
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
  },
  created() {
    this.headerChange();
    this.carryText();
  },
  mounted() {
    this.contextmenu();
  },
};
</script>

<style scoped>
.aside-left {
  float: left;
  position: fixed;
}
.aside-right {
  left: 85%;
  float: right;
  position: fixed;
}

#context-menu {
  position: absolute;
  z-index: 10000;
  width: 150px;
  background: #1b1a1a;
  border-radius: 5px;
  transform: scale(0);
  transform-origin: top left;
}
#context-menu.active {
  transform: scale(1);
  transition: transform 300ms ease-in-out;
}
#context-menu .item {
  padding: 8px 10px;
  font-size: 15px;
  color: #eee;
}
#context-menu .item:hover {
  background: #555;
}
#context-menu .item i {
  display: inline-block;
  margin-right: 5px;
}
#context-menu hr {
  margin: 2px 0px;
  border-color: #555;
}
</style>
