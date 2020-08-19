<template>
  <div id="create" class="container">
    <div class="row">
      <div class="col-12 p-0" style="text-align:left; margin:3vh 0px 3vh 0px;">
        <p id="newPost">새 글 쓰기</p>
      </div>

      <input placeholder="제목을 입력하세요" class="titleInput" v-model="title" type="text" />

      <div id="tag" class="flex-column">
        <button class="tagBtn" v-for="tag in this.tagList" :key="tag">{{ tag }}</button>
      </div>
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
        left-toolbar="undo redo | clear h hr italic bold ol ul quote table strikethrough image code"
        :disabled-menus="[]"
        @upload-image="handleUploadImage"
        @copy-code-success="handleCopyCodeSuccess"
      />
      <button class="submit" @click="checkCreate">제출</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import constants from "@/lib/constants.js";
import { mapState } from "vuex";
export default {
  name: "CreateView",
  data() {
    return {
      constants,
      title: "",
      text: constants.defalutText,
      inputTag: null,
      tagList: [],
      imageList: [],
    };
  },
  computed: {
    ...mapState(["authToken", "s3url"]),
  },
  methods: {
    checkCreate() {
      if (confirm("작성완료되었습니까?")) {
        this.createPost();
      }
    },
    // Tag 추가 이벤트
    tagEvent() {
      if (this.inputTag.length <= 15 && !!this.inputTag.trim().length) {
        if (!this.tagList.includes(this.inputTag)) {
          this.tagList.push(this.inputTag.trim());
          this.inputTag = null;
        } else {
          this.inputTag = null;
          alert("이미 중복된 Tag가 있습니다.");
        }
      } else {
        if (this.inputTag.length > 15) {
          alert("Tag는 15자 이하로 입력해주세요");
        } else {
          alert("Tag를 입력해주세요.");
        }
        this.inputTag = null;
      }
    },

    /// Tag 지우는 로직
    deleteTag() {
      if (
        this.tagList.length >= 1 &&
        (this.inputTag === null || this.inputTag === "")
      ) {
        if (
          confirm(
            this.tagList[this.tagList.length - 1] +
              " Tag를" +
              "정말 삭제하시겠습니까?"
          )
        ) {
          this.tagList.pop();
        }
      }
    },

    /// 코드 복사
    handleCopyCodeSuccess(code) {
      alert("성공적으로 복사되었습니다.");
    },
    changeHeight() {
      const mainMarkdownEditor = document.querySelector(".v-md-editor__main");
      mainMarkdownEditor.style.minHeight = "55vh";
    },
    createPost() {
      const configTag = "," + this.tagList.join(",") + ",";
      const postData = {
        title: this.title,
        content: this.text,
        tags: configTag,
      };
      var titleCheck = this.title;
      var contentCheck = this.text;
      if (!titleCheck.trim().length || !contentCheck.trim().length) {
        alert("제목과 내용을 빈칸으로 낼수 없습니다.");
      } else {
        this.imageList = this.imageList.filter((item) => {
          const imageUrl = item.path.replace(this.s3url, constants.imageUrl);
          return this.text.includes(imageUrl);
        });
        const images = this.imageList.map((image) => {
          const data = {};
          data.image = image.iid;
          return data;
        });

        const totalData = {
          post: [postData],
          images: images,
          token: this.authToken,
        };
        axios
          .post(
            constants.baseUrl + `post/${this.$store.state.username}/`,
            totalData
          )
          .then((res) => {
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
      }
    },
    handleUploadImage(event, insertImage, files) {
      const formData = new FormData();
      formData.append("files", files[0]);
      axios
        .post(constants.baseUrl + "file/upload/", formData)
        .then((res) => {
          this.imageList.push(res.data.object);
          const imageUrl = res.data.object.path.replace(
            this.s3url,
            constants.imageUrl
          );
          insertImage({
            url: imageUrl,
            desc: res.data.object.iname,
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
.container {
  max-width: 90%;
  margin-bottom: 50px;
}
#newPost {
  font-size: 40px;
  padding: 3px 8px 3px 5px;
  border-bottom: #48a999 solid 4px;
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
  width: 20%;
  border-bottom: 1px solid #aaaaaa;
  margin: 1vh 5px;
}
.tagBtn {
  background-color: #e8eaf6;
  border: none;
  cursor: auto;
  border-radius: 16px;
  padding: 7px;
  margin: 7px 3px;
  color: #0ca678;
}

.submit {
  font-size: 20px;
  margin: 5px 15px;
  padding: 8px 13px;
  background-color: #48a999;
  color: white;
  border-radius: 8px;
}
</style>
