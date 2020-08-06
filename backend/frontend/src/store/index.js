import Vue from "vue";
import Vuex from "vuex";
import axios from "axios";
import constants from "@/lib/constants";
import router from "@/router";
Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    headerTitle: "welog",
    headerPathName: "main",
    headerPathParams: null,
    authToken: sessionStorage.getItem("auth-token"),
    username: null,
    articleList: [],
  },
  getters: {
    isLogined: (state) => !!state.authToken,
  },
  mutations: {
    SET_header(state, urlname) {
      state.headerTitle = urlname;
    },
    SET_headerPath(state, payload) {
      state.headerPathName = payload.PathName;
      state.headerPathParams = payload.PathParams;
    },
    SET_TOKEN(state, token) {
      state.authToken = token;
      sessionStorage.setItem("auth-token", token);
    },
    SET_USERNAME(state, username) {
      state.username = username;
    },
    SET_Articles(state, articles) {
      state.articles = articles;
    },
  },
  actions: {
    postAuthData({ commit }, info) {
      console.log("store postAuthData");
      axios.post(constants.baseUrl + info.location, info.data).then((res) => {
        const token = res.headers.authorization.replace("Bearer", "");
        const data = JSON.parse(atob(token.split(".")[1]));
        commit("SET_USERNAME", data.username);
        commit("SET_TOKEN", token);
        router.push({ name: "userPostItems", params: { id: data.username } });
      });
    },
    login({ dispatch }, loginData) {
      const info = {
        data: loginData,
        location: "user/login",
      };
      dispatch("postAuthData", info);
    },
    logout({ commit }) {
      commit("SET_TOKEN", null);
      sessionStorage.removeItem("auth-token");
      commit("SET_USERNAME", null);
    },
    usernameCheck({ state, commit }) {
      const data = JSON.parse(atob(state.authToken.split(".")[1]));

      commit("SET_USERNAME", data.username);
    },
    headerChange({ commit }, urlname) {
      commit("SET_header", urlname);
      commit("SET_headerPath", {
        PathName: "userPostItems",
        PathParams: urlname,
      });
    },
  },
  modules: {},
});
