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
    <div class="modal-dialog modal-lg" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <input class="modal-title" v-model="updateArticle.title" id="exampleModalLabel" />
          <div id="tag" class="flex-column">
            <button class="tagBtn" v-for="tag in this.tagList" :key="tag">{{tag}}</button>
          </div>
          <input v-model="inputTag" placeholder="Tag를 입력하고 Enter를 누르세요" @keyup.enter="tagEvent" />
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
        <div class="modal-body">
          <v-md-editor class="w-100" v-model="updateArticle.content" />
        </div>
        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary"
            @click="returnData"
            data-dismiss="modal"
          >Close</button>
          <button type="button" class="btn btn-primary" @click="updatePost">Save changes</button>
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
    ...mapState(["updateArticle", "articleDetail", "authToken"]),
  },
  data() {
    return {
      inputTag: "",
      tagList: [],
    };
  },
  mounted() {
    this.tagList = this.updateArticle.tags;
  },
  methods: {
    tagEvent() {
      if (this.inputTag.length <= 15) {
        if (!this.tagList.includes(this.inputTag)) {
          this.tagList.push(this.inputTag);
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

    returnData() {
      this.$store.commit("SET_ARTICLEDETAIL", this.articleDetail);
    },
    updatePost() {
      const tempTags = "," + this.tagList.join(",") + ",";
      this.updateArticle.tags = tempTags;
      const putData = this.updateArticle;

      const totalData = {
        post: [putData],
        images: [],
        token: this.authToken,
      };
      console.log(
        "요청주소 : ",
        constants.baseUrl +
          `post/${this.articleDetail.author}/${this.articleDetail.pid}/`
      );
      console.log("전송데이터:", totalData);
      axios
        .put(
          constants.baseUrl +
            `post/${this.articleDetail.author}/${this.articleDetail.pid}/`,
          totalData
        )
        .then((res) => {
          setTimeout(this.$router.go(0), 1000);
        })
        .catch((err) => {
          console.log("에러 : ", err);
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
  margin: 7px 3px;
  color: #0ca678;
}
</style>