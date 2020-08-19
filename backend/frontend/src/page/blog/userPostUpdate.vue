<template>
  <div
    class="modal fade"
    id="update"
    tabindex="-1"
    role="dialog"
    aria-labelledby="exampleModalLabel"
    aria-hidden="true"
    @keydown.esc="returnData"
  >
    <div class="modal-dialog modal-fullsize" role="document">
      <div class="modal-content modal-fullsize">
        <div class="modal-header">
          <input class="modal-title" v-model="updateArticle.title" id="exampleModalLabel" />
          <button
            type="button"
            class="close"
            @click="returnData"
            data-dismiss="modal"
            aria-label="Close"
          >
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-tags">
          <div id="tag" class="flex-column">
            <button class="tagBtn" v-for="tag in this.tagList" :key="tag">{{tag}}</button>
          </div>
          <input
            v-model="inputTag"
            placeholder="Tag를 입력하고 Enter를 누르세요"
            @keyup.enter="tagEvent"
            @keyup.delete="deleteTag"
          />
        </div>
        <div class="modal-body">
          <v-md-editor
            class="w-100"
            v-model="updateArticle.content"
            left-toolbar="undo redo | clear h hr italic bold ol ul quote table strikethrough image code"
            :disabled-menus="[]"
            @upload-image="handleUploadImage"
          />
        </div>
        <div class="modal-footer">
          <button type="button" @click="returnData" data-dismiss="modal">취소</button>
          <button type="button" @click="updatePost">수정</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import axios from "axios";
import constants from "@/lib/constants.js";

export default {
  computed: {
    ...mapState(["updateArticle", "articleDetail", "authToken", "s3url"]),
  },
  data() {
    return {
      inputTag: "",
      tagList: [],
      imageList: [],
    };
  },
  mounted() {
    setTimeout(() => {
      this.tagList = Object.values(this.updateArticle.tags);
      this.imageList = this.updateArticle.imageList;
    }, 500);
  },
  methods: {
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

    returnData() {
      this.$store.commit("SET_ARTICLEDETAIL", this.articleDetail);
    },
    updatePost() {
      const tempTags = "," + this.tagList.join(",") + ",";
      this.updateArticle.tags = tempTags;
      const putData = this.updateArticle;
      var titleCheck = this.updateArticle.title;
      var contentCheck = this.updateArticle.content;
      if (!titleCheck.trim().length || !contentCheck.trim().length) {
        alert("제목과 내용을 빈칸으로 낼수 없습니다.");
      } else {
        this.imageList = this.imageList.filter((item) => {
          const imgageUrl = item.path.replace(this.s3url, constants.imageUrl);
          return this.updateArticle.content.includes(imgageUrl);
        });
        const images = this.imageList.map((image) => {
          const data = {};
          data.image = image.iid;
          return data;
        });

        const totalData = {
          post: [putData],
          images: images,
          token: this.authToken,
        };
        axios
          .put(
            constants.baseUrl +
              `post/${this.articleDetail.author}/${this.articleDetail.pid}/`,
            totalData
          )
          .then((res) => {
            console.log(res.data);
            // setTimeout(this.$router.go(0), 1000);
          })
          .catch((err) => {
            console.log("에러 : ", err);
          });
      }
    },
    handleUploadImage(event, insertImage, files) {
      const formData = new FormData();
      formData.append("files", files[0]);

      axios
        .post(constants.baseUrl + "file/upload/", formData)
        .then((res) => {
          console.log(res.data);
          this.imageList.push(res.data.object);
          const imageUrl = res.data.object.path.replace(
            this.s3url,
            constants.imageUrl
          );
          insertImage({
            // 고쳐야할 부분
            url: imageUrl,
            desc: res.data.object.iname,
          });
        })
        .catch((err) => {
          console.log(err.response);
        });
    },
  },
};
</script>

<style scoped>
.tagBtn {
  background-color: #e8eaf6;
  border: none;
  cursor: auto;
  border-radius: 16px;
  padding: 7px;
  margin: 0px 3px;
  color: #0ca678;
}
</style>