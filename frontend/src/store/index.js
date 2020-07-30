import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    headerTitle : 'develoger',
    headerPath : '/',
    username : sessionStorage.getItem('username'),
    articleList : [],
  },
  getters : {
    isLogined : state => !!state.username,
  },
  mutations: {
    SET_header(state,urlname){
      state.headerTitle = urlname
      
    },
    SET_headerPath(state,urlname){
      state.headerPath = '/'+urlname
    },
    SET_USERNAME(state,username){
      state.username = username
      sessionStorage.setItem('username',username)
    },
    SET_Articles(state,articles){
      state.articles = articles
    }  
  },
  actions: {
  },
  modules: {
  }
})
