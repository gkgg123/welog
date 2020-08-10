<template>
  <div id="create" class="container">
    <div class="row">
      <div class="col-12 p-0" style="text-align:left; margin:5vh 0px 3vh 0px;">
        <p id="newPost">새 글 쓰기</p>
      </div>

      <input placeholder="제목을 입력하세요" class="titleInput" v-model="title" type="text" />

      <div id="tag" class="flex-column"></div>
      <input
        placeholder="Tag를 입력하고 Enter를 누르세요"
        @keyup.enter="tagEvent"
        @keyup.delete="deleteTag"
        class="taginput"
        v-model="inputTag"
        type="text"
      />
      <v-md-editor
        style="margin:5vh 0px; box-sizing: border-box;"
        id="create"
        placeholder="새 글을 작성해 보세요"
        v-model="text"
        @copy-code-success="handleCopyCodeSuccess"
      />
      <button @click="checkCreate">제출</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import constants from "@/lib/constants.js";
export default {
  name: "CreateView",
  data() {
    return {
      constants,
      title: "",
      text: "",
      inputTag: null,
      taglist: [],
      confirmText: "test중입니다.",
    };
  },
  methods: {
    checkCreate() {
      if (confirm("작성완료되었습니까?")) {
        this.createPost();
      }
    },
    // Tag 추가 이벤트
    tagEvent() {
      if (this.inputTag.length <= 15) {
        if (!this.taglist.includes(this.inputTag)) {
          const btn = document.createElement("button");
          const tagDiv = document.querySelector("#tag");
          btn.innerText = this.inputTag;
          btn.setAttribute(
            "style",
            "background-color:#e8eaf6; border: none; cursor:auto; border-radius: 16px; padding: 7px; margin:7px 3px; color:#0CA678"
          );
          tagDiv.append(btn);
          this.taglist.push(this.inputTag);
          this.inputTag = null;
        } else {
          this.inputTag = null;
          alert("이미 중복된 Tag가 있습니다.");
        }
      } else {
        this.inputTag = null;
        alert("Tag는 15자 이하로 입력해주세요");
      }
    },

    /// Tag 지우는 로직
    deleteTag() {
      if (
        this.taglist.length >= 1 &&
        (this.inputTag === null || this.inputTag === "")
      ) {
        if (
          confirm(
            this.taglist[this.taglist.length - 1] +
              " Tag를" +
              "정말 삭제하시겠습니까?"
          )
        ) {
          const tagDiv = document.querySelector("#tag");
          tagDiv.lastChild.remove();
          this.taglist.pop();
        }
      }
    },

    /// 코드 복사
    handleCopyCodeSuccess(code) {
      alert("성공적으로 복사되었습니다.");
      console.log(code);
    },
    changeHeight() {
      const mainMarkdownEditor = document.querySelector(".v-md-editor__main");
      mainMarkdownEditor.style.minHeight = "55vh";
    },
    createPost() {
      const configTag = "," + this.taglist.join(",") + ",";
      const postData = {
        title: this.title,
        content: this.text,
        tags: configTag,
      };

      axios
        .post(
          constants.baseUrl + `post/${this.$store.state.username}/`,
          postData
        )
        .then((res) => {
          console.log(res.data.object.pid);
          this.$router.push({
            name: constants.URL_TYPE.POST.POST,
            params: {
              id: this.$store.state.username,
              pid: res.data.object.pid,
            },
          });
        })
        .catch((err) => {
          console.log(err.response);
        });
    },
  },
  mounted() {
    this.changeHeight();
  },
};
</script>

<style scoped>
#newPost {
  font-size: 40px;
  border-bottom: #ddd solid 4px;
  display: inline;
}
.titleInput {
  font-size: 30px;
  width: 100%;
  margin-bottom: 17px;
}
.taginput {
  font-size: 17px;
  padding-bottom: 4px;
  padding-left: 6px;
  width: 30%;
  border-bottom: 1px solid;
  margin: 1vh 5px;
}
</style>
