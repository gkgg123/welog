<template>
  <div>
      {{$route.params.id}}
      <h1> 안녕</h1>
  </div>
</template>

<script>
export default {
    name: 'userPersonalPage',
    methods : {
      headerChange(){
        const urlname = this.$route.params.id
        this.$store.commit('SET_header',urlname)
        this.$store.commit('SET_headerPath',urlname)

      }
    },
    created(){
      this.headerChange()
    }

}
</script>

<style>

</style>