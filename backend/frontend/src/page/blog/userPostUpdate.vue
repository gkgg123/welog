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
          <input class="modal-title" v-model="updateArticle.title" id="exampleModalLabel" /> |||
          <input v-model="updateArticle.tags" placeholder="안녕" />
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
          <v-md-editor class="w-100" v-model="updateArticle.content" />
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
          <button type="button" class="btn btn-primary" @click="updatePost">Save changes</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import axios from "axios";
export default {
  computed: {
    ...mapState(["updateArticle", "articleDetail"]),
  },

  methods: {
    returnData() {
      this.$store.commit("SET_ARTICLEDETAIL", this.articleDetail);
    },
    updatePost() {
      const temp = this.updateArticle;
      console.log(temp);
      const tempTags = "," + this.updateArticle.tags.join(",") + ",";
      this.updateArticle.tags = tempTags;
      const putData = this.updateArticle;

      const totalData = {
        post: [putData],
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
          console.log("수정결과 : ", res.data);
        });
    },
  },
};
</script>

<style>
</style>