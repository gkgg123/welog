import Vue from 'vue'
import Router from 'vue-router'

import constants from '../lib/constants'

// 유저
import Login from '../page/user/Login.vue'
import Join from '../page/user/Join.vue'
import Logout from '../page/user/Logout.vue'

// 포스트
import List from '../page/post/List.vue'
import CreateView from '../page/post/CreateView.vue'

import userPersonalPage from '../page/post/userPersonalPage.vue'
import userPersonalPosts from '../page/post/userPersonalPosts.vue'
import userPersonalPost from '../page/post/userPersonalPost.vue'
import userPersonalIntro from '../page/post/userPersonalIntro.vue'
// import { compile, component } from 'vue/types/umd'
Vue.use(Router) 
 
export default new Router({
  routes: [   
    // 로그인/가입
    { 
      path: '/user/login',
      name: constants.URL_TYPE.USER.LOGIN,
      component: Login
    },
    {
      path: '/user/join',
      name: constants.URL_TYPE.USER.JOIN,
      component: Join
    },
    // 포스트
    { 
      path: '/',
      name: constants.URL_TYPE.POST.MAIN,
      component: List,
    },
    {
      path : '/create',
      name : 'CreateView',
      component : CreateView,
    },
    {
      path: '/user/logout',
      name : constants.URL_TYPE.USER.LOGOUT,
      component : Logout,
      
    },
    {
      path : '/@:id',
      name : 'userPersonalPage',
      component : userPersonalPage,
      children : [
        {
          path : '',
          name : 'userPersonalPosts',
          component : userPersonalPosts
        },
        {
          path : 'intro',
          name : 'userPersonalIntro',
          component : userPersonalIntro
        },
      ]
    },
    {
        path : '/@:id/:pid',
        name : 'userPersonalPost',
        component : userPersonalPost,
    }
  ]
})
