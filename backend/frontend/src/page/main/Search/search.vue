<template>
  <div>
    <h1>안녕하세요 여기는 Search입니다.</h1>
    <h1>{{options[this.$route.query.type]}} : {{this.$route.query.search}}</h1>
  </div>
</template>

<script>
export default {
  name: "search",
  data() {
    return {
      options: {
        Both: "제목+내용",
        tag: "태그",
      },
    };
  },
};
</script>

<style>
</style>