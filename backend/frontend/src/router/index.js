import Vue from "vue";
import Router from "vue-router";

import constants from "../lib/constants";

// 유저
import Login from "../page/user/Login.vue";
import Join from "../page/user/Join.vue";
import Logout from "../page/user/Logout.vue";
import UserProfile from "../page/user/profile/UserProfile.vue";
import UserProfileConfig from "../page/user/profile/UserProfileConfig.vue";
import recevieConfirm from "../page/user/profile/recevieConfirm.vue";
import sendConfirm from "../page/user/profile/sendConfirm.vue";
// 메인화면
import Main from "../page/main/Main.vue";
import recentList from "../page/main/recentList.vue";
import recommandList from "../page/main/recommandList.vue";
import searchList from "../page/main/Search/search.vue";
// 개인블로그
import CreateView from "../page/blog/CreateView.vue";
import userBlog from "../page/blog/userBlog.vue";
import userPostItems from "../page/blog/userPostItems.vue";
import userPost from "../page/blog/userPost.vue";
import userPersonalIntro from "../page/blog/userPersonalIntro.vue";

import store from "../store/index.js";
Vue.use(Router);

const routes = [
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
  {
    path: "/:username/profile/",
    name: constants.URL_TYPE.USER.PROFILE,
    component: UserProfile,
    beforeEnter: (to, from, next) => {
      store.dispatch("headerChange", to.params.username);
      next();
    },
    redirect: {
      name: constants.URL_TYPE.USER.PROFILECONFIG,
    },
    children: [
      {
        path: "",
        name: constants.URL_TYPE.USER.PROFILECONFIG,
        component: UserProfileConfig,
      },
      {
        path: "receive",
        name: constants.URL_TYPE.USER.RECEIVECONFIRM,
        component: recevieConfirm,
      },
      {
        path: "send",
        name: constants.URL_TYPE.USER.SENDCONFIRM,
        component: sendConfirm,
      },
    ],
  },
  // 메인화면
  {
    path: "/",
    name: constants.URL_TYPE.MAIN.MAIN,
    component: Main,
    redirect: {
      name: constants.URL_TYPE.MAIN.LIST,
    },
    children: [
      {
        path: "",
        name: constants.URL_TYPE.MAIN.LIST,
        component: recentList,
      },
      {
        path: "recommand",
        name: constants.URL_TYPE.MAIN.RECOMMAND,
        component: recommandList,
      },
    ],
  },
  {
    path: "/search",
    name: constants.URL_TYPE.MAIN.SEARCH,
    component: searchList,
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
    redirect: {
      name: constants.URL_TYPE.POST.POSTITEMS,
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
];

const router = new Router({
  routes,
});
router.beforeEach((to, from, next) => {
  // publicPages 는 로그인이 없어도 되는 페이지들을 넣어주면 된다.
  const publicPages = [
    constants.URL_TYPE.POST.POST,
    constants.URL_TYPE.POST.BLOGINTRO,
    constants.URL_TYPE.POST.POSTITEMS,
    constants.URL_TYPE.POST.BLOG,
    constants.URL_TYPE.MAIN.RECOMMAND,
    constants.URL_TYPE.MAIN.MAIN,
    constants.URL_TYPE.MAIN.LIST,
    constants.URL_TYPE.USER.JOIN,
    constants.URL_TYPE.USER.LOGIN,
    constants.URL_TYPE.MAIN.SEARCH,
  ];
  // auth page 같은경우엔 로그인해서 접근하면 안되는 페이지들을 넣어주면 된다.
  const authPages = [
    constants.URL_TYPE.USER.JOIN,
    constants.URL_TYPE.USER.LOGIN,
  ];
  const authRequired = !publicPages.includes(to.name);
  const unauthRequired = authPages.includes(to.name);
  const isLoggedIn = store.getters.isLogined;

  // 로그인 했는데, 로그인페이지같은 곳에 들어가면 안되니 막아준다.
  if (unauthRequired && isLoggedIn) {
    next("/");
  }
  // 로그인이 필요한 페이지인데, 로그인이 안되어있으면 로그인 페이지로 바꿔준다.
  if (authRequired && !isLoggedIn) {
    next({ name: constants.URL_TYPE.USER.LOGIN });
  } else {
    next();
  }
});
export default router;
