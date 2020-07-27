import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    headerTitle : '환상의 6조'
  },
  mutations: {
    SET_header(state,urlname){
      state.headerTitle = urlname
    }
  },
  actions: {
  },
  modules: {
  }
})
