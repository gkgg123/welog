import Vue from "vue";
import Router from "vue-router";

import constants from "../lib/constants";

// 유저
import Login from "../page/user/Login.vue";
import Join from "../page/user/Join.vue";
import Logout from "../page/user/Logout.vue";

// 메인화면
import Main from "../page/main/Main.vue";

// 개인블로그
import CreateView from "../page/blog/CreateView.vue";
import userBlog from "../page/blog/userBlog.vue";
import userPostItems from "../page/blog/userPostItems.vue";
import userPost from "../page/blog/userPost.vue";
import userPersonalIntro from "../page/blog/userPersonalIntro.vue";

import store from "../store/index.js";
Vue.use(Router);

export default new Router({
  routes: [
    // 로그인/가입
    {
      path: "/user/login",
      name: constants.URL_TYPE.USER.LOGIN,
      component: Login,
    },
    {
      path: "/user/join",
      name: constants.URL_TYPE.USER.JOIN,
      component: Join,
    },
    {
      path: "/user/logout",
      name: constants.URL_TYPE.USER.LOGOUT,
      component: Logout,
    },
    // 메인화면
    {
      path: "/",
      name: constants.URL_TYPE.MAIN.MAIN,
      component: Main,
    },
    // POST
    {
      path: "/create",
      name: constants.URL_TYPE.POST.CREATE,
      component: CreateView,
    },
    {
      path: "/@:id",
      name: constants.URL_TYPE.POST.BLOG,
      component: userBlog,
      beforeEnter: (to, from, next) => {
        store.dispatch("headerChange", to.params.id);
        next();
      },
      children: [
        {
          path: "",
          name: constants.URL_TYPE.POST.POSTITEMS,
          component: userPostItems,
        },
        {
          path: "intro",
          name: constants.URL_TYPE.POST.BLOGINTRO,
          component: userPersonalIntro,
          beforeEnter: (to, from, next) => {
            store.dispatch("headerChange", to.params.id);
            next();
          },
        },
      ],
    },
    {
      path: "/@:id/:pid",
      name: constants.URL_TYPE.POST.POST,
      component: userPost,
    },
  ],
});
