<template>
  <div class="user-profile">
    <div class="row">
      <div class="col-3" style="height:100vh">
        <div class="col-12">
          <router-link
            :to="{
              name: constants.URL_TYPE.USER.PROFILECONFIG,
              params: { username: username },
            }"
            >프로필 페이지</router-link
          >
        </div>
        <div class="col-12 mt-5">
          <router-link
            :to="{
              name: constants.URL_TYPE.USER.RECEIVECONFIRM,
              params: { username: username },
            }"
            >받은 수정요청</router-link
          >
        </div>
        <div class="col-12 mt-5">보낸 수정요청</div>
      </div>
      <div class="col-9">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script>
import constants from "../../../lib/constants";
import { mapState } from "vuex";
import "../../../assets/css/user.scss";
export default {
  name: "profile",
  data() {
    return {
      constants,
    };
  },
  computed: {
    ...mapState(["username"]),
  },
};
</script>

<style></style>
