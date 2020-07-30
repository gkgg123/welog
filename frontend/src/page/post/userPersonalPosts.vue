<template>
  <div>
      안녕하세요 개인 글 페이지에요
      <div> 
      <ul v-for="article in articles">
              
          <li>제목 : <router-link :to="{ name: 'userPersonalPost', params:{id: $route.params.id,pid:article.pid} }" > {{article.title}} </router-link> </li>
          <li> {{article.createDate}} </li>
          <hr>
      </ul> 
      </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name : 'userPersonalPosts',
  data(){
    return {
      articles : []
    }
  },
  methods :{
    test(){
      const username = this.$route.params.id
      axios.get('http://localhost:8080/'+`post/${username}/`)
      .then((res)=>{
        this.articles = res.data
        console.log(res)
      })
    }
  },
  mounted(){
    this.test()
  }

}
</script>

<style>

</style>