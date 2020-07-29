<template>
  <div>
      {{$route.params.id}}
      <div class="container">
        <div class="row">
          <div class="col-6 offset-3 mt-5" style="min-height: 30vh;">
            <div class="row h-100 border-bottom">
              <div class="col-4">
                유저 사진
              </div>
              <div class="col-8">
                <div class="col-12 h-50">
                  유저 아이디
                </div>
                <div class="col-12 h-50">
                  유저 한줄 소개
                </div>
              </div>
            </div>
          </div>
          <div class="col-12 my-5">
            <div class="row">
                <router-link  class="col-6" :to="{name:'userPersonalPosts',params:{id:$route.params.id}}" >글</router-link>
                <router-link  class="col-6" :to="{name:'userPersonalIntro',params:{id:$route.params.id}}">소개글</router-link>
            </div>
          </div>
          <router-view></router-view>
        </div>
      </div>
  </div>
</template>

<script>
export default {
    name: 'userPersonalPage',
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

</style>