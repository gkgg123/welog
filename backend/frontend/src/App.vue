<template>
  <div id="app">
    <Header :isHeader="isHeader" :isLogined="isLogined" />
    <router-view />
    {{username}}
  </div>
</template>

<script src="https://use.fontawesome.com/releases/v5.2.0/js/all.js"></script>
<script>
import "./assets/css/style.scss";
import Header from "./components/common/Header.vue";
import constants from "./lib/constants";
import { mapState, mapGetters } from "vuex";

export default {
  name: "App",
  components: {
    Header,
  },
  created() {
    let url = this.$route.name;
    this.checkUrl(url);
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
        "CreateView",
      ];

      let isHeader = true;
      array.map((path) => {
        if (url === path) isHeader = false;
      });
      this.isHeader = isHeader;
    },
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
