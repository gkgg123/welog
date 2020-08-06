<template>
  <div id="app">
    <Header :isHeader="isHeader" :isLogined="isLogined" />
    <router-view />
  </div>
</template>

<script src="https://use.fontawesome.com/releases/v5.2.0/js/all.js"></script>
<script>
import "./assets/css/style.scss";
import Header from "./components/common/Header.vue";
import constants from "./lib/constants";
import { mapState, mapGetters, mapActions } from "vuex";

export default {
  name: "App",
  components: {
    Header,
  },
  created() {
    let url = this.$route.name;
    this.checkUrl(url);
    this.usernameCheck();
  },
  watch: {
    $route(to) {
      this.checkUrl(to.name);
    },
  },
  methods: {
    checkUrl(url) {
      let array = [
        constants.URL_TYPE.USER.LOGIN,
        constants.URL_TYPE.USER.JOIN,
        constants.URL_TYPE.POST.CREATE,
      ];

      let isHeader = true;
      array.map((path) => {
        if (url === path) isHeader = false;
      });
      this.isHeader = isHeader;
    },
    ...mapActions(["usernameCheck"]),
  },
  computed: {
    ...mapState(["username"]),
    ...mapGetters(["isLogined"]),
  },
  data: function () {
    return {
      isHeader: true,
      constants,
    };
  },
};
</script>

<style></style>
