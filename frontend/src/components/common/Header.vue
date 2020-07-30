<template>
  <div id="header" v-if="isHeader">
    <h1>
      <router-link :to="headerPath">
        {{ headerTitle }}
      </router-link>
    </h1>
    <div class="right">
      <div class="search-input">
        <i class="fas fa-search"></i>
        <input v-model="keyword" type="text" />
      </div>

      <router-link
        v-if="!isLogined"
        :to="{ name: constants.URL_TYPE.USER.LOGIN }"
        class="login-btn"
      >
        로그인
      </router-link>
      <router-link
        v-else
        v-bind:to="{ name: constants.URL_TYPE.USER.LOGOUT }"
        class="login-btn"
      >
        로그아웃
      </router-link>
      <router-link v-if="isLogined" to="/create" class="login-btn">새 글쓰기</router-link>
    </div>
  </div>
</template>

<script>
import constants from "../../lib/constants";
import { mapState,mapGetters } from "vuex";
export default {
  name: "Header",
  components: {},
  props: ["isHeader"],
  computed: {
    ...mapState(["headerTitle", "headerPath"]),
    ...mapGetters(["isLogined"])
  },
  watch: {},
  created() {},
  methods: {},
  data: function() {
    return {
      constants,
      keyword: "",
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
