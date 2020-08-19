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

      <router-link
        v-else
        :to="{ name: headerPathName, params: { id: headerPathParams } }"
      >
        <img class="home-logo" src="/img/home_logo.png" />
      </router-link>
    </div>
    <div class="right d-flex">
      <select class="select-box" v-model="selected">
        <option
          v-for="option in options"
          :value="option.value"
          :key="option.text"
          >{{ option.text }}</option
        >
      </select>
      <div class="search-input">
        <i class="fas fa-search"></i>
        <input v-model="keyword" type="text" @keyup.enter="search" />
      </div>

      <router-link
        v-if="!isLogined"
        :to="{ name: constants.URL_TYPE.USER.LOGIN }"
        class="login-btn"
        >로그인</router-link
      >

      <router-link v-if="isLogined" to="/create" class="create-btn"
        >새 글쓰기</router-link
      >
      <div class="request-alert" v-if="isLogined">
        <div v-if="1">
          <i class="far fa-bell"></i>
        </div>
        <div v-else>
          <i class="fas fa-bell"></i>
        </div>
      </div>
      <div class="dropdown-box" v-if="isLogined">
        <a
          class="nav-link dropdown-toggle"
          href="#"
          id="navbarDropdown"
          role="button"
          data-toggle="dropdown"
          aria-haspopup="true"
          aria-expanded="false"
        >
          <img :src="userprofile" />
        </a>
        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
          <router-link
            class="dropdown-item"
            :to="{
              name: constants.URL_TYPE.USER.PROFILECONFIG,
              params: {
                username: username,
              },
            }"
            >내 프로필</router-link
          >
          <router-link
            class="dropdown-item"
            :to="{
              name: constants.URL_TYPE.POST.POSTITEMS,
              params: { id: username },
            }"
            >내 블로그</router-link
          >
          <router-link
            class="dropdown-item"
            :to="{ name: constants.URL_TYPE.USER.LOGOUT }"
            >로그아웃</router-link
          >
        </div>
      </div>
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
      "userprofile",
    ]),
  },
  watch: {},
  created() {},
  methods: {
    search() {
      const keyword = this.keyword;
      const selected = this.selected;
      this.keyword = "";
      this.selected = "title";
      this.$router.push({
        name: constants.URL_TYPE.MAIN.SEARCH,
        query: {
          type: selected,
          search: keyword,
        },
      });
    },
  },
  data: function() {
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
.dropdown-menu {
  padding: 12px 0px;
}
.dropdown-item {
  text-align: center;
  font-size: 18px;
  padding: 8px 12px;
  margin: 8px 0px;
}
.dropdown-item:hover {
  background-color: #00796b;
  color: white;
}
</style>
