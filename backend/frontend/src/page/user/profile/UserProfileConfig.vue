<template>
  <div class="profile-contents">
    <div class="user-info">
      <div class="user-img">
        <img src="https://cdn0.iconfinder.com/data/icons/set-ui-app-android/32/8-512.png" alt />
        <button class="button-update">이미지 변경</button>
        <button class="button-delete">이미지 삭제</button>
      </div>
      <div class="user-intro">
        <div class="user-name">{{username}}</div>
        <textarea class="intro-content">자기소개</textarea>
        <button>수정</button>
      </div>
    </div>
    <div class="user-active">
      <div class="user-award">
        <img src="../../../../public/img/trophy.png" alt />
        <div>나의 활동 내역</div>
      </div>
      <div class="active-item1">
        <i class="fas fa-pencil-alt"></i>
        내가 작성한 포스트 수
        <span>1</span>
      </div>
      <div class="active-item2">
        <i class="fas fa-heart"></i>내가 받은 좋아요 수
        <span>1</span>
      </div>
      <div class="active-item3">
        <i class="far fa-clipboard"></i>내가 좋아요 한 포스트 수
        <span>1</span>
      </div>
      <div class="active-item4">
        <img src="../../../../public/img/heart-icon.png" alt /> 가장 많은 좋아요 수
        <span>1</span>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
export default {
  name: "UserProfileConfig",
  computed: {
    ...mapState(["username"]),
  },
};
</script>

<style></style>
