import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import VueCookies from "vue-cookies";
import VueMarkdownEditor from "@kangc/v-md-editor";
import "@kangc/v-md-editor/lib/style/base-editor.css";
import createCopyCodePlugin from "@kangc/v-md-editor/lib/plugins/copy-code/index";
import vuepressTheme from "@kangc/v-md-editor/lib/theme/vuepress.js";
import createEmojiPlugin from "@kangc/v-md-editor/lib/plugins/emoji/index";
import enUS from "@kangc/v-md-editor/lib/lang/en-US";
import store from "./store";

VueMarkdownEditor.lang.use("en-US", enUS);
VueMarkdownEditor.use(vuepressTheme);
VueMarkdownEditor.use(createCopyCodePlugin());
VueMarkdownEditor.use(createEmojiPlugin());
Vue.use(VueMarkdownEditor);

Vue.use(VueCookies);
/* eslint-disable no-new */
new Vue({
  el: "#app",
  router,
  components: { App },
  store,
  template: "<App/>",
});
