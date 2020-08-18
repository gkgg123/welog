<template>
  <div id="user-profile">
    <div class="row">
      <div class="side-bar">
        <div class="side-item">
          <router-link
            :to="{
              name: constants.URL_TYPE.USER.PROFILECONFIG,
              params: { username: username },
            }"
          >
            <i class="fas fa-cog"></i> 프로필 설정
          </router-link>
        </div>
        <div class="side-item mt-5">
          <router-link
            :to="{
              name: constants.URL_TYPE.USER.RECEIVECONFIRM,
              params: { username: username },
            }"
          >
            <i class="fas fa-exclamation-circle"></i> 받은
            수정요청
          </router-link>
        </div>
        <div class="side-item mt-5">
          <i class="fas fa-paper-plane"></i>보낸 수정요청
        </div>
      </div>
      <!-- 모바일 사이즈에서 보이는 사이드(?)바 -->
      <div class="small-side-bar">
        <div class="small-side-item">
          <router-link
            :to="{
              name: constants.URL_TYPE.USER.PROFILECONFIG,
              params: { username: username },
            }"
          >
            <i class="fas fa-cog"></i>
          </router-link>
        </div>
        <div class="small-side-item">
          <router-link
            :to="{
              name: constants.URL_TYPE.USER.RECEIVECONFIRM,
              params: { username: username },
            }"
          >
            <i class="fas fa-exclamation-circle"></i>
          </router-link>
        </div>
        <div class="small-side-item">
          <i class="fas fa-paper-plane"></i>
        </div>
      </div>
      <div class="profile-view">
        <router-view />
      </div>
      <div class="profile-view">
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
