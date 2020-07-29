<template>
  <div class="mt-5">
    <h1>{{$route.params.id}}</h1>
    <h2>{{$route.params.pid}}</h2>
    <h3>{{title}}</h3>
    <aside class="aside-left">왼쪽입니다.</aside>
    <div class="aside-right">오른쪽입니다.</div>
    <v-md-editor
        class="border border-primary col-8 mx-auto"
        style="margin:5vh 0px; height:300vh;"
        id="detail"
        mode="preview"
        v-model="content"
      />
      
    
    <textarea class="border border-primary" placeholder="댓글을 남겨주세요" name="" id="" cols="30" rows="10">

    </textarea>
  </div>
</template>

<script>
export default {
    name : 'userPersonalPost',
    data() {
        return {
            title : 'text 중입니다.',
            content : '### 안의 내용입니다.',
            tagList : [],

        }
    },
    methods : {
      headerChange(){
        const urlname = this.$route.params.id
        this.$store.commit('SET_header',urlname)
        this.$store.commit('SET_headerPath','@'+urlname)
      }
    },
    created(){
      this.headerChange()
    }

}
</script>

<style>
.aside-left {
    float: left;
    position: fixed;
}
.aside-right {
    left: 85%;
    float: right;
    position: fixed;
}
</style>