import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    headerTitle : '환상의 6조',
    headerPath : '/'
  },
  mutations: {
    SET_header(state,urlname){
      state.headerTitle = urlname
      
    },
    SET_headerPath(state,urlname){
      state.headerPath = '@'+urlname
    }
  },
  actions: {
  },
  modules: {
  }
})
