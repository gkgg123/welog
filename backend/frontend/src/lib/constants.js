export default {
  TITLE: "SSAFY 개발블로그",
  baseUrl: "http://i3c106.p.ssafy.io:8080/",
  imageUrl: "https://d3v9ilm5vhs4go.cloudfront.net/",
  defalutText:
    '![Welog.gif](https://d3v9ilm5vhs4go.cloudfront.net/200820093456Welog.gif)  \n ::: tip Tip은 이렇게 쓸수 있습니다.  \n::: \n::: tip 제목을 고치기 \n  제목은 이렇게 고칠수 있습니다. \n::: \n::: warning\n:::\n::: danger\n:::\n아래와 같이 코드를 입력할 수 있습니다.\n코드블럭 안의 우측 위를 클릭시 code를 복사할수 있습니다.\n```javascript\nconsole.log("hi")\nvar i = 1;\nif(i === 1){\n alert("안녕하세요")\n} else {\n  console.err("여기는 welog입니다")\n}\n```\n```html\n<h1> 안녕하세요 </h1>\n<img src="https://pbs.twimg.com/profile_images/1210177470588055552/8oBQax1O_reasonably_small.jpg" />\n```"',
  ERROR: {
    FRONT_ERROR: "FE00",
  },
  URL_TYPE: {
    USER: {
      LOGIN: "login",
      JOIN: "join",
      LOGOUT: "logout",
      PROFILE: "profile",
      PROFILECONFIG: "profileconfig",
      RECEIVECONFIRM: "recevieConfirm",
      SENDCONFIRM: "sendConfirm",
    },
    MAIN: {
      MAIN: "main",
      LIST: "recentList",
      RECOMMAND: "recommandList",
      SEARCH: "search",
    },
    POST: {
      CREATE: "createView",
      BLOG: "userBlog",
      POSTITEMS: "userPostItems",
      BLOGINTRO: "userPersonalIntro",
      POST: "userPost",
    },
  },
};
