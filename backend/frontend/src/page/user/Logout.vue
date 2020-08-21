<template>
  <div></div>
</template>

<script>
import axios from "axios";
import constants from "@/lib/constants";
export default {
  name: "Logout",
  data() {
    return {
      constants,
    };
  },
  methods: {
    logout() {
      this.$store.dispatch("logout");
      alert("메인화면으로 이동합니다.");
      this.$router.push({ name: constants.URL_TYPE.MAIN.MAIN });
    },
  },
  created() {
    this.logout();
  },
};
</script>

<style></style>
