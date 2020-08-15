import Vue from "vue";
import Vuex from "vuex";
import axios from "axios";
import constants from "@/lib/constants";
import router from "@/router";
import _ from "lodash";
Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    headerTitle: "welog",
    headerPathName: constants.URL_TYPE.MAIN.MAIN,
    headerPathParams: null,
    authToken: sessionStorage.getItem("auth-token"),
    username: null,
    receiveArticleList: [],
    articles: [],
    articleDetail: [],
    updateArticle: [],
    commentList: [],
    nextPage: 0,
    pageLimit: 0,
  },
  getters: {
    isLogined: (state) => !!state.authToken,
    pageLimitcalc: (state) => parseInt(state.receiveArticleList.length / 10),
    isreceived: (state) => !!state.receiveArticleList.length,
  },
  mutations: {
    // Header의 이름을 바꿔주는 mutaion
    SET_header(state, urlname) {
      state.headerTitle = urlname;
    },
    // Header의 링크 Path를 바꿔주는 mutation
    SET_headerPath(state, payload) {
      state.headerPathName = payload.PathName;
      state.headerPathParams = payload.PathParams;
    },
    // TOKEN을 session에 적어주는 mutaiton
    SET_TOKEN(state, token) {
      state.authToken = token;
      sessionStorage.setItem("auth-token", token);
    },
    // state의 username을 변경해주는 mutation
    SET_USERNAME(state, username) {
      state.username = username;
    },
    // RECEVIE_ARTICLES를 변경하는 함수
    SET_RECEIVEARTICLES(state, reciveArticleList) {
      state.receiveArticleList = reciveArticleList;
    },
    // Articles를 변경하는 mutation
    SET_ARTICLES(state, articles) {
      state.articles = [...state.articles, ...articles];
    },
    // PAGELIMIT를 변경하는 mutation
    SET_PAGELIMIT(state, pageLimit) {
      state.pageLimit = pageLimit;
    },
    // nextPage의 값을 늘려주는 mutation
    INCREASE_NEXTPAGE(state) {
      state.nextPage += 1;
    },
    // PAGE 관련 변수를 초기화 시켜주는 mutation
    RESET_PAGINATION(state) {
      state.pageLimit = 0;
      state.nextPage = 0;
    },
    // ARTICLES와 관련된 변수를 초기화 시켜주는 mutations
    RESET_ARTICLES(state) {
      state.receiveArticleList = [];
      state.articles = [];
    },
    SET_ARTICLEDETAIL(state, article) {
      state.articleDetail = _.cloneDeep(article);
      state.updateArticle = _.cloneDeep(article);
    },
    SET_COMMENTLIST(state, comment) {
      state.commentList = comment;
    },
  },
  actions: {
    // 로그인 Axios 요청하는 과정
    postAuthData({ commit }, info) {
      axios
        .post(constants.baseUrl + info.location, info.data)

        .then((res) => {
          const token = res.headers.authorization.replace("Bearer", "");
          const data = JSON.parse(atob(token.split(".")[1]));
          commit("SET_USERNAME", data.username);
          commit("SET_TOKEN", token);
          router.push({
            name: constants.URL_TYPE.POST.POSTITEMS,
            params: { id: data.username },
          });
        })
        .catch((err) => {
          alert("아이디가 없거나 비밀번호가 틀렸습니다.");
        });
    },
    //Login 하는 함수
    login({ dispatch }, loginData) {
      const info = {
        data: loginData,
        location: "user/login",
      };
      dispatch("postAuthData", info);
    },
    //Logout 하는 함수
    logout({ commit }) {
      commit("SET_TOKEN", null);
      sessionStorage.removeItem("auth-token");
      commit("SET_USERNAME", null);
    },
    //UserName을 JWT TOKEN에서 decoding 하는 함수
    usernameCheck({ state, commit }) {
      if (!!state.authToken) {
        const data = JSON.parse(atob(state.authToken.split(".")[1]));
        commit("SET_USERNAME", data.username);
      } else {
        commit("SET_USERNAME", null);
      }
    },
    // Headers의 Path와 Name을 바꿔주는 곳.
    headerChange({ commit }, urlname) {
      commit("SET_header", urlname);
      commit("SET_headerPath", {
        PathName: constants.URL_TYPE.POST.POSTITEMS,
        PathParams: urlname,
      });
    },
    // Articles의 List를 불러오는 함수 //
    async getArticles({ state, commit, getters, dispatch }, payload) {
      commit("RESET_PAGINATION");
      commit("RESET_ARTICLES");
      await axios
        .get(constants.baseUrl + payload.location, { params: payload.query })
        .then((res) => {
          var receive = Object.values(res.data);
          const temp = receive.map((item) => {
            if (!!item.post.tags) {
              item.post.tags = item.post.tags.split(",").filter((tag) => !!tag);
            } else {
              item.post.tags = [];
            }
            item.post.likeCount = item.likeCount;
            item.post.liseuserlist = item.userlist;
            item.post.imageList = item.images;
            return item.post;
          });
          commit("SET_RECEIVEARTICLES", temp);
          if (getters.isreceived) {
            commit("SET_PAGELIMIT", getters.pageLimitcalc);
            dispatch("attachArticles");
          }
        })
        .catch((err) => {
          console.log(err.respnose);
        });
    },
    // ReceiveArticles에서 필요한만큼 더 붙이는 과정.
    attachArticles({ state, commit }) {
      const currentPage = state.nextPage;
      commit("INCREASE_NEXTPAGE");
      if (currentPage <= state.pageLimit - 1) {
        const nextArticles = state.receiveArticleList.slice(
          currentPage * 10,
          (currentPage + 1) * 10
        );
        console.log(nextArticles, currentPage, "최초");
        commit("SET_ARTICLES", nextArticles);
      } else if (currentPage === state.pageLimit) {
        const nextArticles = state.receiveArticleList.slice(currentPage * 10);
        commit("SET_ARTICLES", nextArticles);
      }
    },
    /// ArticleDetail 정보를 불러오는 곳
    async carryArticle({ commit }, location) {
      return await axios.get(constants.baseUrl + location).then((res) => {
        const receive = [res.data];
        const temp = receive.map((item) => {
          if (!!item.post.tags) {
            item.post.tags = item.post.tags.split(",").filter((tag) => !!tag);
          } else {
            item.post.tags = [];
          }
          item.post.likeCount = item.likeCount;
          item.post.likeuserlist = item.userlist;
          item.post.imageList = item.images;
          return item.post;
        });
        commit("SET_ARTICLEDETAIL", temp[0]);
      });
    },

    ///삭제하는 곳
    deletePost({ state }) {
      if (state.username === state.articleDetail.author) {
        axios
          .delete(
            constants.baseUrl +
              `post/${state.articleDetail.author}/${state.articleDetail.pid}`,
            {
              data: {
                token: state.authToken,
              },
            }
          )
          .then((res) => {
            router.push({
              name: constants.URL_TYPE.POST.BLOG,
              params: { id: state.articleDetail.author },
            });
          });
      }
    },

    //CommentList 가져오는 방법
    carryComment({ commit }, location) {
      return axios.get(constants.baseUrl + location).then((res) => {
        commit("SET_COMMENTLIST", res.data);
      });
    },
  },
  modules: {},
});
