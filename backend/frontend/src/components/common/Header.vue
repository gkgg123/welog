<template>
  <div id="header" v-if="isHeader">
    <h1>
      <router-link
        :to="{ name: headerPathName, params: { id: headerPathParams } }"
      >{{ headerTitle }}</router-link>
    </h1>
    <div class="right d-flex">
      <select v-model="selected" class="h-75 my-2">
        <option v-for="option in options" :value="option.value" :key="option.text">{{option.text}}</option>
      </select>
      <div class="search-input">
        <i class="fas fa-search"></i>
        <input v-model="keyword" type="text" @keyup.enter="search" />
      </div>

      <router-link
        v-if="!isLogined"
        :to="{ name: constants.URL_TYPE.USER.LOGIN }"
        class="login-btn"
      >로그인</router-link>
      <router-link
        v-else
        v-bind:to="{ name: constants.URL_TYPE.USER.LOGOUT }"
        class="login-btn"
      >로그아웃</router-link>
      <router-link v-if="isLogined" to="/create" class="login-btn">새 글쓰기</router-link>
    </div>
  </div>
</template>

<script>
import constants from "../../lib/constants";
import { mapState, mapGetters } from "vuex";
export default {
  name: "Header",
  components: {},
  props: ["isHeader", "isLogined"],
  computed: {
    ...mapState(["headerTitle", "headerPathName", "headerPathParams"]),
  },
  watch: {},
  created() {},
  methods: {
    search() {
      console.log(this.selected);
      console.log(this.keyword);
      this.$router.push({
        name: constants.URL_TYPE.MAIN.SEARCH,
        query: {
          type: this.selected,
          search: this.keyword,
        },
      });
    },
  },
  data: function () {
    return {
      constants,
      keyword: "",
      selected: "Both",
      options: [
        { text: "제목+내용", value: "Both" },
        { text: "태그", value: "tag" },
      ],
    };
  },
};
</script>

<style scoped>
#header {
  height: 10vh;
}
#header > h1 {
  font-size: 30px;
}
.search-input {
  margin: 7px;
}
</style>
