<template>
  <div>
    <h1>여기는 프로필 수정을 만들 페이지입니다.</h1>
    <div class="col-12">
      <div class="row">
        <div class="col-3" style="height: 170px;">
          <div style="background-color:gray;height: 170px;">여기는 프로필 이미지</div>
          <button>프로필 수정</button>
        </div>
        <div class="col-9">
          <label for="nickname">닉네임 :</label>
          <input name="nickname" type="text" :value="username" disabled title="닉네임은 수정 불가입니다." />
          <div></div>
          <label for="email">이메일 :</label>
          <input name="email" type="text" :value="username" disabled title="닉네임은 수정 불가입니다." />
        </div>
      </div>
      <div class="col-12 mt-5">한줄 소개</div>

      <button class="btn btn-primary">수정</button>
      <button class="btn btn-primary">닫기</button>
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

<style>
</style>