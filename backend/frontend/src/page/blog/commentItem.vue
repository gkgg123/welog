<template>
  <div class="comment-list">
    <div class="comment-profile">
      <div class="profile-img">
        <i class="far fa-smile"></i>
      </div>
      <div class="writer-info">
        <p class="writer-id">작성자 아이디 {{comment.name}}</p>
        <p class="written-day">작성 날짜</p>
      </div>
    </div>
    <div class="comment-content">{{comment.content}}</div>
  </div>
</template>

<script>
export default {
  name: "commentItem",
  props: {
    comment: Object,
  },
};
</script>

<style>
</style>