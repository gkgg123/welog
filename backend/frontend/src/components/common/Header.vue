<template>
  <div id="header" v-if="isHeader">
    <div class="header-logo">
      <router-link
        v-if="headerPathName !== constants.URL_TYPE.MAIN.MAIN"
        :to="{ name: constants.URL_TYPE.MAIN.MAIN }"
      >
        <img class="small-logo" src="/img/small_home_logo.png" />
      </router-link>
      <router-link
        v-if="headerPathName !== constants.URL_TYPE.MAIN.MAIN"
        :to="{ name: headerPathName, params: { id: headerPathParams } }"
      >
        <div class="header-name">{{ headerTitle }}</div>
      </router-link>

      <router-link v-else :to="{ name: headerPathName, params: { id: headerPathParams } }">
        <img class="home-logo" src="/img/home_logo.png" />
      </router-link>
    </div>
    <div class="right d-flex">
      <select class="select-box" v-model="selected">
        <option v-for="option in options" :value="option.value" :key="option.text">{{ option.text }}</option>
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
      <router-link v-else :to="{ name: constants.URL_TYPE.USER.LOGOUT }" class="login-btn">로그아웃</router-link>
      <div v-if="isLogined">
        <a
          class="nav-link dropdown-toggle"
          href="#"
          id="navbarDropdown"
          role="button"
          data-toggle="dropdown"
          aria-haspopup="true"
          aria-expanded="false"
        >Dropdown</a>
        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
          <router-link
            class="dropdown-item"
            :to="{
              name: constants.URL_TYPE.POST.BLOG,
              parmas: { id: username },
            }"
          >내 블로그 가기</router-link>
          <router-link class="dropdown-item" :to="{ name: constants.URL_TYPE.USER.LOGOUT }">로그아웃</router-link>
          <div class="dropdown-divider"></div>
          <a class="dropdown-item" href="#">Something else here</a>
        </div>
      </div>

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
    ...mapState([
      "headerTitle",
      "headerPathName",
      "headerPathParams",
      "username",
    ]),
  },
  watch: {},
  created() {},
  methods: {
    search() {
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
      selected: "title",
      options: [
        { text: "제목", value: "title" },
        { text: "내용", value: "content" },
        { text: "제목+내용", value: "text" },
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
